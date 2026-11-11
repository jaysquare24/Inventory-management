package com.inventory.ManagementSystem.service;

import com.inventory.ManagementSystem.dto.*;

import com.inventory.ManagementSystem.model.OrderItem;
import com.inventory.ManagementSystem.model.OutgoingOrder;
import com.inventory.ManagementSystem.model.Product;
import com.inventory.ManagementSystem.repository.OutgoingOrderRepo;
import com.inventory.ManagementSystem.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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

        Map<Integer, SalesReportResponse> salesMap = new LinkedHashMap<>();

        for(OutgoingOrder order: outgoingOrders){

            for(OrderItem item: order.getItems()) {

                int productId = item.getProduct().getId();

                SalesReportResponse sale = salesMap.get(productId);


                if (sale != null){

                    sale.setQuantitySold(sale.getQuantitySold() + item.getQuantity());
                    sale.setTotalSales(sale.getTotalSales().add(item.getTotalPrice()));

                }else {
                   salesMap.put(productId,
                           new SalesReportResponse(
                                   item.getProduct().getId(),
                                   item.getProduct().getName(),
                                   item.getQuantity(),
                                   item.getTotalPrice()
                           )
                   );
                }
            }
        }
        return new ArrayList<>(salesMap.values());

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
