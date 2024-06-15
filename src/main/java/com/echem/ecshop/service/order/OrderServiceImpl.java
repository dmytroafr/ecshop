package com.echem.ecshop.service.order;

import com.echem.ecshop.dao.OrderRepository;
import com.echem.ecshop.domain.Order;
import com.echem.ecshop.domain.OrderDetails;
import com.echem.ecshop.domain.OrderStatus;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.BucketDTO;
import com.echem.ecshop.dto.OrderDTO;
import com.echem.ecshop.service.bucket.BucketService;
import com.echem.ecshop.service.email.EmailService;
import com.echem.ecshop.service.product.ProductService;
import com.echem.ecshop.service.user.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    private final BucketService bucketService;
    private final EmailService emailService;
    private final UserService userService;
    private final ProductService productService;

    public OrderServiceImpl(OrderRepository orderRepository, BucketService bucketService, ProductService productService, EmailService emailService, UserService userService) {
        this.orderRepository = orderRepository;
        this.bucketService = bucketService;
        this.emailService = emailService;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public Order createOrder(OrderDTO orderDto, String username) {

        Order order = new Order();
        order.setDelivery(orderDto.getDelivery());
        order.setPayment(orderDto.getPayment());

        User user = userService.findByName(username);

        BucketDTO bucketDtoByUser = bucketService.getBucketDtoByUser(username);

        List<OrderDetails> details = bucketDtoByUser.productList.stream()
                .map(productDto -> {
                    OrderDetails detail = new OrderDetails();
                    detail.setProduct(productService.getProductRef(productDto.getProductId()));
                    detail.setAmount(productDto.getAmount());
                    detail.setPrice(productDto.getPrice());
                    detail.setOrder(order);
                    return detail;
                }).collect(Collectors.toList());

        order.setSum(new BigDecimal(bucketDtoByUser.sum));
        order.setUser(user);
        order.setDetails(details);
        order.setStatus(OrderStatus.NEW);

        bucketService.deleteBucketByUser(user);
        userService.save(user);

        Order save = orderRepository.save(order);
        String massage = "Ваше замовлення прийнято у роботу, Номер замовлення "+ save.getId();
        emailService.send(user.getEmail(),massage, "Ваше замовлення");

        return save;
    }

    @Override
    public Order getOrderById(Long orderId) {
        Order orderById = orderRepository.getOrderById(orderId);

        return orderRepository.getOrderById(orderId);
    }




}
