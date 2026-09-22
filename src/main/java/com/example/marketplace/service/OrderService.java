package com.example.marketplace.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.marketplace.entity.Order;
import com.example.marketplace.repository.OrderRepository;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.repository.AddressRepository;
import com.example.marketplace.util.ValidationUtil;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ValidationUtil validationUtil;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, AddressRepository addressRepository, ValidationUtil validationUtil) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.validationUtil = validationUtil;
    }


    private void validateForeignKeys(Order order) {
        if (!userRepository.existsById(order.getUserId())) {
            validationUtil.fail("userId", "references a record that does not exist");
        }
        if (!addressRepository.existsById(order.getAddressId())) {
            validationUtil.fail("addressId", "references a record that does not exist");
        }
    }
    public Order createOrder(Order order) {
        validationUtil.validate(order);
        validateForeignKeys(order);
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(int id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order updateOrder(int id, Order orderDetails) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setUserId(orderDetails.getUserId());
        order.setAddressId(orderDetails.getAddressId());
        order.setCouponCode(orderDetails.getCouponCode());
        order.setTotalAmount(orderDetails.getTotalAmount());
        order.setOrderStatus(orderDetails.getOrderStatus());
        order.setPaymentStatus(orderDetails.getPaymentStatus());
        validationUtil.validate(order);
        validateForeignKeys(order);
        return orderRepository.save(order);
    }

    public void deleteOrder(int id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        orderRepository.delete(order);
    }
}
