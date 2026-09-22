package com.example.marketplace.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.marketplace.entity.OrderItem;
import com.example.marketplace.repository.OrderItemRepository;
import com.example.marketplace.repository.OrderRepository;
import com.example.marketplace.repository.ProductRepository;
import com.example.marketplace.repository.SellerRepository;
import com.example.marketplace.util.ValidationUtil;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final ValidationUtil validationUtil;

    public OrderItemService(OrderItemRepository orderItemRepository, OrderRepository orderRepository, ProductRepository productRepository, SellerRepository sellerRepository, ValidationUtil validationUtil) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
        this.validationUtil = validationUtil;
    }


    private void validateForeignKeys(OrderItem orderItem) {
        if (!orderRepository.existsById(orderItem.getOrderId())) {
            validationUtil.fail("orderId", "references a record that does not exist");
        }
        if (!productRepository.existsById(orderItem.getProductId())) {
            validationUtil.fail("productId", "references a record that does not exist");
        }
        if (!sellerRepository.existsById(orderItem.getSellerId())) {
            validationUtil.fail("sellerId", "references a record that does not exist");
        }
    }
    public OrderItem createOrderItem(OrderItem orderItem) {
        validationUtil.validate(orderItem);
        validateForeignKeys(orderItem);
        return orderItemRepository.save(orderItem);
    }

    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public OrderItem getOrderItemById(int id) {
        return orderItemRepository.findById(id).orElseThrow(() -> new RuntimeException("OrderItem not found"));
    }

    public OrderItem updateOrderItem(int id, OrderItem orderItemDetails) {
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(() -> new RuntimeException("OrderItem not found"));
        orderItem.setOrderId(orderItemDetails.getOrderId());
        orderItem.setProductId(orderItemDetails.getProductId());
        orderItem.setSellerId(orderItemDetails.getSellerId());
        orderItem.setQuantity(orderItemDetails.getQuantity());
        orderItem.setPrice(orderItemDetails.getPrice());
        validationUtil.validate(orderItem);
        validateForeignKeys(orderItem);
        return orderItemRepository.save(orderItem);
    }

    public void deleteOrderItem(int id) {
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(() -> new RuntimeException("OrderItem not found"));
        orderItemRepository.delete(orderItem);
    }
}
