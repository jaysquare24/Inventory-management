package com.inventory.ManagementSystem.service;

import com.inventory.ManagementSystem.dto.*;

import com.inventory.ManagementSystem.model.OutgoingOrder;
import com.inventory.ManagementSystem.model.Product;
import com.inventory.ManagementSystem.repository.OutgoingOrderRepo;
import com.inventory.ManagementSystem.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    ProductRepo productRepo;
    @Autowired
    OutgoingOrderRepo outgoingOrderRepo;

    SalesReportResponse salesReportResponse;

    private static final int LOW_STOCK_THRESHOLD = 5;

    public List<InventoryReportResponse> getInventory() {
        List<Product> products = productRepo.findAll();

       /* List<InventoryReportResponse> inventory = new ArrayList<>();

        for(Product product : products){
            inventory.add(new InventoryReportResponse(
                    product.getName(),
                    product.getCategory().getCategoryName(),
                    product.getQuantity(),
                    product.getPrice(),
                    product.getQuantity() * product.getPrice()

            ));
        }

        return inventory;*/

        return products.stream()
                .map(product -> {
                    BigDecimal inventoryValue = product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity()));
                    return new InventoryReportResponse(
                            product.getName(),
                            product.getCategory().getCategoryName(),
                            product.getQuantity(),
                            product.getPrice(),
                            inventoryValue

                    );
                })
                .toList();
    }

    public List<SalesReportResponse> getSales() {
        List<OutgoingOrder> outgoingOrders =outgoingOrderRepo.findAll();
        List<SalesReportResponse> sales = new ArrayList<SalesReportResponse>();


        for(OutgoingOrder order: outgoingOrders){
             boolean isFound = false;

              for (SalesReportResponse sale : sales) {
                  if (sale.getProductId() == order.getProduct().getId()) {
                      sale.setQuantitySold(sale.getQuantitySold() + order.getOrderQuantity());
                      sale.setTotalSales(sale.getTotalSales().add(order.getTotalPriceAfterDiscount()));
                      isFound = true;
                      break;
                  }
              }

              if(!isFound){
                  sales.add(
                          new SalesReportResponse(
                          order.getProduct().getId(),
                          order.getProduct().getName(),
                          order.getOrderQuantity(),
                          order.getTotalPriceAfterDiscount()
                          )
                  );
              }
        }
        return sales;

    }

    public List<TopProductReportResponse> getTopProducts() {

        return getSales().stream()
                .sorted(Comparator.comparing(SalesReportResponse::getQuantitySold)
                               .reversed()
                               .thenComparing(SalesReportResponse::getProductName)
                )
                .limit(5)
                .map(product -> new TopProductReportResponse(
                        product.getProductId(),
                        product.getProductName(),
                        product.getQuantitySold()
                ))
                .toList();
    }

    public List<LowStockResponse> lowStocks() {

        return productRepo.findAll()
                  .stream()
                  .filter(product -> product.getQuantity() <= LOW_STOCK_THRESHOLD)
                  .sorted(Comparator.comparing(Product::getQuantity))
                  .map(product -> new LowStockResponse(
                          product.getId(),
                          product.getName(),
                          product.getQuantity()
                  )).toList();

    }

    public RevenueResponse getRevenue() {
        List<OutgoingOrder>outgoingOrders = outgoingOrderRepo.findAll();

        BigDecimal totalRevenue = outgoingOrders.stream()
                .map(OutgoingOrder::getTotalPriceAfterDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalOrder= outgoingOrders.size();

        return new RevenueResponse(totalRevenue, totalOrder);
    }
}
