package com.echem.ecshop.service.order;

import com.echem.ecshop.dao.OrderRepository;
import com.echem.ecshop.domain.Order;
import com.echem.ecshop.domain.OrderDetails;
import com.echem.ecshop.domain.OrderStatus;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.BucketDTO;
import com.echem.ecshop.dto.OrderDTO;
import com.echem.ecshop.dto.UserDTO;
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

    private UserDTO getByUserName(String username) {
        return userService.findByUserName(username);
    }

    @Override
    public Order createOrder(OrderDTO orderDto, Long userId) {

        Order order = new Order();
        order.setDelivery(orderDto.getDelivery());
        order.setPayment(orderDto.getPayment());

        User refById = userService.getRefById(userId);

        UserDTO userDTO = getByUserName(refById.getUsername());
//        Long userId = userDTO.id();

        BucketDTO bucketDto = bucketService.getBucketDtoByUser(userId);

        List<OrderDetails> details = bucketDto.productList.stream()
                .map(productDto -> {
                    OrderDetails detail = new OrderDetails();
                    detail.setProduct(productService.getProductRef(productDto.getProductId()));
                    detail.setAmount(productDto.getAmount());
                    detail.setPrice(productDto.getPrice());
                    detail.setOrder(order);
                    return detail;
                }).collect(Collectors.toList());

        order.setSum(new BigDecimal(bucketDto.sum));

        // TODO  щось тут не так
        order.setUser(refById);
        order.setDetails(details);
        order.setStatus(OrderStatus.NEW);

        Order savedOrder = orderRepository.save(order);

        bucketService.clearBucket(bucketDto.getId());

        String massage = "Ваше замовлення прийнято у роботу, Номер замовлення "+ savedOrder.getId();


        emailService.send(userDTO.email(),massage, "Ваше замовлення");

        return savedOrder;
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.getOrderById(orderId);
    }
}
