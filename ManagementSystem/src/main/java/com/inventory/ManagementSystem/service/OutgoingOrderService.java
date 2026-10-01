package com.inventory.ManagementSystem.service;

import com.inventory.ManagementSystem.dto.EmailDetails;
import com.inventory.ManagementSystem.dto.OutgoingOrderRequest;
import com.inventory.ManagementSystem.dto.OutgoingOrderResponse;
import com.inventory.ManagementSystem.exception.InsufficientStockException;
import com.inventory.ManagementSystem.exception.ResourceNotFoundException;
import com.inventory.ManagementSystem.model.OutgoingOrder;
import com.inventory.ManagementSystem.model.Product;
import com.inventory.ManagementSystem.repository.OutgoingOrderRepo;
import com.inventory.ManagementSystem.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OutgoingOrderService {

    @Autowired
    OutgoingOrderRepo outgoingOrderRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    EmailService emailService;

    private OutgoingOrderResponse mapToResponse(OutgoingOrder order) {

        return new OutgoingOrderResponse(
                order.getId(),
                order.getProduct().getName(),
                order.getOrderQuantity(),
                order.getTotalPriceBeforeDiscount(),
                order.getTotalPriceAfterDiscount(),
                order.getDiscount(),
                order.getOrderDate()
        );
    }


    public void createOrder(OutgoingOrderRequest request) {

        Product product = productRepo.findById(
                        Math.toIntExact(request.getProductId()))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if(request.getOrderQuantity() > product.getQuantity()){
            throw new InsufficientStockException("Insufficient stock");
        }

        BigDecimal beforeDiscount = product.getPrice().multiply(BigDecimal.valueOf(request.getOrderQuantity()));
        BigDecimal afterDiscount = beforeDiscount.subtract(BigDecimal.valueOf(500));

        OutgoingOrder order = new OutgoingOrder();

        order.setProduct(product);
        order.setOrderQuantity(request.getOrderQuantity());
        order.setDiscount(BigDecimal.valueOf(500));
        order.setTotalPriceBeforeDiscount(beforeDiscount);
        order.setTotalPriceAfterDiscount(afterDiscount);

        product.setQuantity(
                product.getQuantity() - request.getOrderQuantity()
        );

        productRepo.save(product);
        outgoingOrderRepo.save(order);

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient("negritdigitalz@gmail.com");
        emailDetails.setSubject("New Outgoing Order: " + product.getName());
        emailDetails.setMessageBody(
                "An outgoing order has been dispatched.\n\n" +
                        "Product: " + product.getName() + "\n" +
                        "Quantity: " + request.getOrderQuantity() + "\n" +
                        "Price Before Discount: " + beforeDiscount + "\n" +
                        "Discount: 500\n" +
                        "Price After Discount: " + afterDiscount + "\n" +
                        "Order Date: " + order.getOrderDate()
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

    public void editOutgoingOder(int id, OutgoingOrderRequest request) {
        OutgoingOrder existingOrder = outgoingOrderRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Order not found"));

        Product product = productRepo.findById(Math.toIntExact(request.getProductId()))
                .orElseThrow(()-> new ResourceNotFoundException("Product not found"));

        if(request.getOrderQuantity() > product.getQuantity()){
            throw new InsufficientStockException("Insufficient stock");
        }

       BigDecimal beforeDiscount = product.getPrice().multiply(BigDecimal.valueOf(request.getOrderQuantity()));
       BigDecimal afterDiscount = beforeDiscount.subtract(BigDecimal.valueOf(500));

        existingOrder.setOrderQuantity(request.getOrderQuantity());
        existingOrder.setProduct(product);
        existingOrder.setDiscount(BigDecimal.valueOf(500));
        existingOrder.setTotalPriceBeforeDiscount(beforeDiscount);
        existingOrder.setTotalPriceAfterDiscount(afterDiscount);

        outgoingOrderRepo.save(existingOrder);
    }

    public void deleteOutgoingOrder(int id) {
        outgoingOrderRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Order not found"));

        outgoingOrderRepo.deleteById(id);
    }

    public List<OutgoingOrderResponse> getAllProductOutgoingOrder(int id) {
        List<OutgoingOrder> productOrders = outgoingOrderRepo.findByProductId(id);

        return productOrders.stream()
                .map(this::mapToResponse)
                .toList();

    }
}
