package com.inventory.ManagementSystem.service;

import com.inventory.ManagementSystem.dto.EmailDetails;
import com.inventory.ManagementSystem.model.IncomingOrder;
import com.inventory.ManagementSystem.model.Product;
import com.inventory.ManagementSystem.model.Supplier;
import com.inventory.ManagementSystem.repository.IncomingOrderRepo;
import com.inventory.ManagementSystem.repository.ProductRepo;
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

    public IncomingOrder newOrder(IncomingOrder order){
        Product product = productRepo.findById(order.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product does not exist"));

        Supplier supplier = supplierRepo.findById(order.getSupplier().getId())
                .orElseThrow(() -> new RuntimeException("Supplier does not exist"));

        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(order.getQuantitySupply()));

        order.setTotalPrice(total);
        order.setProduct(product);
        order.setSupplier(supplier);

        int newQuantity = product.getQuantity() + order.getQuantitySupply();
        product.setQuantity(newQuantity);
        productRepo.save(product);

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient("negritdigitalz@gmail.com");
        emailDetails.setSubject("New Incoming Order: " + product.getName());
        emailDetails.setMessageBody(
                "A new incoming order has been placed.\n\n" +
                        "Product: " + product.getName() + "\n" +
                        "Supplier: " + supplier.getUsername() + "\n" +
                        "Quantity: " + order.getQuantitySupply() + "\n" +
                        "Total Price: " + total + "\n" +
                        "Supply Date: " + order.getSupplyDate()
        );
        emailService.sendMail(emailDetails);

        return incomingOrderRepo.save(order);
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
        existing.setSupplier(incomingOrder.getSupplier());
        existing.setProduct(incomingOrder.getProduct());
        existing.setQuantitySupply(incomingOrder.getQuantitySupply());
        existing.setTotalPrice(incomingOrder.getTotalPrice());
        existing.setSupplyDate(incomingOrder.getSupplyDate());

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

    public List<IncomingOrder> getOrdersBySupplier(int supplierId) {
        Supplier supplier = supplierRepo.findById(supplierId)
                .orElseThrow(()-> new RuntimeException("supplier does not exist"));

        return incomingOrderRepo.findBySupplierId(supplierId);
    }
}
