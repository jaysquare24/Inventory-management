package com.inventory.ManagementSystem.service;

import com.inventory.ManagementSystem.dto.EmailDetails;
import com.inventory.ManagementSystem.dto.IncomingOrderResponse;
import com.inventory.ManagementSystem.exception.UserNotFoundException;
import com.inventory.ManagementSystem.model.IncomingOrder;
import com.inventory.ManagementSystem.model.Product;
import com.inventory.ManagementSystem.model.Staff;
import com.inventory.ManagementSystem.model.Supplier;
import com.inventory.ManagementSystem.repository.IncomingOrderRepo;
import com.inventory.ManagementSystem.repository.ProductRepo;
import com.inventory.ManagementSystem.repository.StaffRepo;
import com.inventory.ManagementSystem.repository.SupplierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class IncomingOrderService {

    @Autowired
    private IncomingOrderRepo incomingOrderRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private SupplierRepo supplierRepo;
    @Autowired
    EmailService emailService;
    @Autowired
    StaffRepo staffRepo;

    public IncomingOrder newOrder(IncomingOrder order){
        Product product = productRepo.findById(order.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product does not exist"));

        Supplier supplier = supplierRepo.findById(order.getSupplier().getId())
                .orElseThrow(() -> new UserNotFoundException("Supplier does not exist"));

        Staff supervisor = staffRepo.findById(order.getSupervisor().getId())
                .orElseThrow(()-> new UserNotFoundException("Supervisor does not exist"));

        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(order.getQuantitySupply()));

        order.setTotalPrice(total);
        order.setProduct(product);
        order.setSupplier(supplier);

        int newQuantity = product.getQuantity() + order.getQuantitySupply();
        product.setQuantity(newQuantity);
        productRepo.save(product);

        IncomingOrder savedOrder = incomingOrderRepo.save(order);

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(supervisor.getEmail());
        emailDetails.setSubject("New Incoming Order: " + product.getName());
        emailDetails.setMessageBody(
                "A new incoming order has been placed.\n\n" +
                        "Product: " + product.getName() + "\n" +
                        "Supplier: " + supplier.getFirstname() + "\n" +
                        "Quantity: " + order.getQuantitySupply() + "\n" +
                        "Total Price: " + total + "\n" +
                        "Supply Date: " + savedOrder.getSupplyDate()
        );
        emailService.sendMail(emailDetails);

        return savedOrder;
    }

    public List<IncomingOrder> allOrders(){
        return incomingOrderRepo.findAll();
    }

    public IncomingOrder getOrder(int id){
        return incomingOrderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not available"));
    }

    public IncomingOrder updateOrder(int id, IncomingOrder incomingOrder){
        IncomingOrder existing = incomingOrderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if(incomingOrder.getSupplier() != null){
            existing.setSupplier(incomingOrder.getSupplier());
        }

        if(incomingOrder.getProduct() != null) {
            existing.setProduct(incomingOrder.getProduct());
        }

        if(incomingOrder.getSupervisor() != null){
            existing.setSupervisor(incomingOrder.getSupervisor());
        }

        if (incomingOrder.getQuantitySupply() > 0) {

            BigDecimal totalPrice = existing.getProduct().getPrice().multiply(BigDecimal.valueOf(incomingOrder.getQuantitySupply()));
            existing.setQuantitySupply(incomingOrder.getQuantitySupply());
            existing.setTotalPrice(totalPrice);
        }
        return incomingOrderRepo.save(existing);
    }

    public void deleteOrder(int id){
        IncomingOrder existing = incomingOrderRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Order not found"));
        incomingOrderRepo.delete(existing);
    }

    public List<IncomingOrder> getOrdersByProduct(int productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product does not exist"));

        return incomingOrderRepo.findByProductId(productId);
    }

    public List<IncomingOrderResponse> getOrdersBySupplier(int supplierId) {
        Supplier supplier = supplierRepo.findById(supplierId)
                .orElseThrow(()-> new UserNotFoundException("supplier does not exist"));

        return incomingOrderRepo.findBySupplierId(supplierId).stream()
                .map(supply -> IncomingOrderResponse.builder()
                        .supplyId(supply.getId())
                        .productId(supply.getProduct().getId())
                        .supplierId(supply.getSupplier().getId())
                        .supervisorId(supply.getSupervisor().getId())
                        .productName(supply.getProduct().getName())
                        .productDesc(supply.getProduct().getDescription())
                        .suppliedQuantity(supply.getQuantitySupply())
                        .productUnitPrice(supply.getProduct().getPrice())
                        .suppliedTotalPrice(supply.getTotalPrice())
                        .dateSupplied(supply.getSupplyDate())
                        .productCategoryName(supply.getProduct().getCategory().getCategoryName())
                        .build())
                .toList();
    }
}
