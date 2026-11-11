package com.inventory.ManagementSystem.service;

import com.inventory.ManagementSystem.dto.EmailDetails;
import com.inventory.ManagementSystem.dto.OutgoingOrderRequest;
import com.inventory.ManagementSystem.dto.OutgoingOrderResponse;
import com.inventory.ManagementSystem.exception.InsufficientStockException;
import com.inventory.ManagementSystem.exception.ResourceNotFoundException;
import com.inventory.ManagementSystem.model.OrderItem;
import com.inventory.ManagementSystem.model.OutgoingOrder;
import com.inventory.ManagementSystem.model.Product;
import com.inventory.ManagementSystem.repository.OrderItemRepo;
import com.inventory.ManagementSystem.repository.OutgoingOrderRepo;
import com.inventory.ManagementSystem.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class OutgoingOrderService {

    @Autowired
    OutgoingOrderRepo outgoingOrderRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    EmailService emailService;

    @Autowired
    OrderItemRepo orderItemRepo;

    private OutgoingOrderResponse mapToResponse(OutgoingOrder order) {

        return new OutgoingOrderResponse(
                order.getId(),
                order.getItems(),
                order.getTotalPriceBeforeDiscount(),
                order.getTotalPriceAfterDiscount(),
                order.getDiscount(),
                order.getOrderDate()
        );
    }

    @Transactional
    public void createOrder(List<OutgoingOrderRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException(
                    "Order must contain at least one item."
            );
        }


        OutgoingOrder order = new OutgoingOrder();

        BigDecimal orderTotalPrice = BigDecimal.ZERO;

        int numOfUnavailableProduct = 0;

        for(OutgoingOrderRequest request : requests) {

            if (request.getOrderQuantity() <= 0) {

                continue;
            }

            Product product = productRepo.findById(Math.toIntExact(request.getProductId()))
                    .orElse(null);

            if (product == null){
                numOfUnavailableProduct++;
                continue;
            }

            if (request.getOrderQuantity() > product.getQuantity()) {
                numOfUnavailableProduct++;
                continue;
            }

            BigDecimal itemTotalPrice = product.getPrice().multiply(BigDecimal.valueOf(request.getOrderQuantity()));

            OrderItem item = new OrderItem();

            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(request.getOrderQuantity());
            item.setUnitPrice(product.getPrice());
            item.setTotalPrice(itemTotalPrice);

            order.getItems().add(item);
            orderTotalPrice = orderTotalPrice.add(itemTotalPrice);

            product.setQuantity(product.getQuantity() - request.getOrderQuantity());
            productRepo.save(product);
        }

        if (order.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "No valid items available to create the order."
            );
        }

        BigDecimal discount = orderTotalPrice
                .multiply(BigDecimal.valueOf(5))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal afterDiscount = orderTotalPrice.subtract(discount);
        order.setDiscount(discount);
        order.setTotalPriceBeforeDiscount(orderTotalPrice);
        order.setTotalPriceAfterDiscount(afterDiscount);

        OutgoingOrder savedOrder = outgoingOrderRepo.save(order);

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient("negritdigitalz@gmail.com");
        emailDetails.setSubject("New Outgoing Order: " + savedOrder.getId());
        emailDetails.setMessageBody(
                "An outgoing order has been dispatched.\n\n" +
                        "Order Id: " + savedOrder.getId() + "\n" +
                        "Unavailale product out or the order: " + numOfUnavailableProduct + "\n" +
                        "Price Before Discount: " + orderTotalPrice + "\n" +
                        "Discount: 500\n" +
                        "Price After Discount: " + afterDiscount + "\n" +
                        "Order Date: " + savedOrder.getOrderDate()
        );
        emailService.sendMail(emailDetails);
    }



    public List<OutgoingOrderResponse> getAllOutgoingOrder() {

         List<OutgoingOrder> orders =  outgoingOrderRepo.findAll();

         /*List<OutgoingOrderResponse> orderResponses = new ArrayList<>();

         for(OutgoingOrder order : orders){
             orderResponses.add(mapToResponse(order));
         }*/

         return orders.stream()
                 .map(this::mapToResponse)
                 .toList();
    }

    public OutgoingOrderResponse getOutgoingOrder(int id) {
        OutgoingOrder order = outgoingOrderRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Order not found"));

        return mapToResponse(order);
    }

    @Transactional
    public void editOutgoingOrder(int id, List<OutgoingOrderRequest> requests) {

        OutgoingOrder existingOrder = outgoingOrderRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException(
                    "Order must contain at least one item"
            );
        }

        // 1. Record the quantities in the existing order.
        Map<Long, Integer> oldQuantities = new HashMap<>();

        for (OrderItem item : existingOrder.getItems()) {
            int productId = item.getProduct().getId();

            oldQuantities.merge(
                    (long) productId,
                    item.getQuantity(),
                    Integer::sum
            );
        }

        // 2. Combine duplicate products in the new request.
        Map<Long, Integer> newQuantities = new HashMap<>();

        for (OutgoingOrderRequest request : requests) {

            if (request.getOrderQuantity() <= 0) {
                throw new IllegalArgumentException(
                        "Order quantity must be greater than zero"
                );
            }

            newQuantities.merge(
                    request.getProductId(),
                    request.getOrderQuantity(),
                    Integer::sum
            );
        }

        // 3. Include products being removed as well as products being ordered.(OLD AND NEW ORDERS)
        Set<Long> productIds = new HashSet<>(oldQuantities.keySet());
        productIds.addAll(newQuantities.keySet());

        Map<Long, Product> products = new HashMap<>();

        for (Long productId : productIds) {

            Product product = productRepo.findById(Math.toIntExact(productId))
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found"));

            int oldQuantity = oldQuantities.getOrDefault(productId, 0);
            int newQuantity = newQuantities.getOrDefault(productId, 0);

            // Current stock excludes the quantity already reserved
            // by the existing outgoing order.
            int availableAfterRestoringOldOrder =
                    product.getQuantity() + oldQuantity;

            if (newQuantity > availableAfterRestoringOldOrder) {
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + productId
                );
            }

            products.put(productId, product);
        }

        // 4. Adjust stock based on the difference between old and new orders.
        for (Long productId : productIds) {

            Product product = products.get(productId);

            int oldQuantity = oldQuantities.getOrDefault(productId, 0);
            int newQuantity = newQuantities.getOrDefault(productId, 0);

            product.setQuantity(
                    product.getQuantity() + oldQuantity - newQuantity
            );
        }

        // 5. Replace the existing items.
        // orphanRemoval = true removes the old item rows when flushed.
        existingOrder.getItems().clear();

        BigDecimal orderTotalPrice = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : newQuantities.entrySet()) {

            Product product = products.get(entry.getKey());
            int quantity = entry.getValue();

            BigDecimal itemTotalPrice = product.getPrice()
                    .multiply(BigDecimal.valueOf(quantity));

            OrderItem item = new OrderItem();

            item.setOrder(existingOrder);
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setUnitPrice(product.getPrice());
            item.setTotalPrice(itemTotalPrice);

            existingOrder.getItems().add(item);

            orderTotalPrice = orderTotalPrice.add(itemTotalPrice);
        }

        // 6. Recalculate the 5% discount and final price.
        BigDecimal discount = orderTotalPrice
                .multiply(BigDecimal.valueOf(5))
                .divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP
                );

        existingOrder.setTotalPriceBeforeDiscount(orderTotalPrice);
        existingOrder.setDiscount(discount);
        existingOrder.setTotalPriceAfterDiscount(
                orderTotalPrice.subtract(discount)
        );

        // The existing order is managed by JPA.
        // No explicit save() is required here.
    }

    public void deleteOutgoingOrder(int id) {
        outgoingOrderRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Order not found"));

        outgoingOrderRepo.deleteById(id);
    }

    public List<OutgoingOrderResponse> getAllProductOutgoingOrder(int id) {
        List<OutgoingOrder> productOrders = outgoingOrderRepo.findDistinctByItems_Product_Id(id);

        return productOrders.stream()
                .map(this::mapToResponse)
                .toList();

    }
}
