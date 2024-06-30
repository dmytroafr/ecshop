package com.echem.ecshop.service.order;

import com.echem.ecshop.dao.OrderRepository;
import com.echem.ecshop.domain.Order;
import com.echem.ecshop.domain.OrderDetails;
import com.echem.ecshop.domain.OrderStatus;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.BucketDTO;
import com.echem.ecshop.dto.OrderDTO;
import com.echem.ecshop.dto.OrderRequest;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.mapper.OrderMapper;
import com.echem.ecshop.service.bucket.BucketService;
import com.echem.ecshop.service.email.EmailService;
import com.echem.ecshop.service.product.ProductService;
import com.echem.ecshop.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    private final BucketService bucketService;
    private final EmailService emailService;
    private final UserService userService;
    private final ProductService productService;

    private final OrderMapper mapper = OrderMapper.MAPPER;

    public OrderServiceImpl(OrderRepository orderRepository, BucketService bucketService, ProductService productService, EmailService emailService, UserService userService) {
        this.orderRepository = orderRepository;
        this.bucketService = bucketService;
        this.emailService = emailService;
        this.userService = userService;
        this.productService = productService;
    }


    @Transactional
    @Override
    public Order makeOrder (OrderRequest orderRequest, UserDTO userDTO) {

        Order order = createEmptyOrder(userDTO);
        log.info("Created order and set User and Order Status");

        BucketDTO bucketDto = bucketService.getBucketDtoByUser(userDTO.getId());
        List<OrderDetails> details = getOrderDetails(bucketDto, order);
        order.setDetails(details);
        order.setSum(new BigDecimal(bucketDto.sum));
        log.info("Set Order Details to new Order");

        order.setDelivery(orderRequest.getDelivery());
        order.setPayment(orderRequest.getPayment());
        log.info("Set Delivery and Payment to new Order");

        orderRepository.save(order);
        log.info("Order with id {} was created and saved into DB", order.getId());

        orderInform(userDTO, order);
        bucketService.clearBucket(bucketDto.getId());
        return order;
    }

    private void orderInform (UserDTO userDTO, Order order) {
        String massage = "Ваше замовлення прийнято у роботу, Номер замовлення "+ order.getId();
        emailService.send(userDTO.getEmail(),massage, "Ваше замовлення");
        emailService.send("sales@e-chem.com.ua", order.toString(), "#" + order.getId());

        log.info("Order with id {} was sent by Emails", order.getId());
    }

    private Order createEmptyOrder(UserDTO userDTO) {
        Order order = new Order();
        User user = userService.getUserByUsername(userDTO.getUsername());
        order.setUser(user);
        order.setStatus(OrderStatus.NEW);
        log.info("Set User {} to new Order", userDTO.getUsername());
        return order;
    }

    private List<OrderDetails> getOrderDetails(BucketDTO bucketDTO, Order order) {
        return bucketDTO.productList.stream()
                .map(productDto -> {
                    OrderDetails detail = new OrderDetails();
                    detail.setProduct(productService.getProductRef(productDto.getProductId()));
                    detail.setAmount(productDto.getAmount());
                    detail.setPrice(productDto.getPrice());
                    detail.setOrder(order);
                    return detail;
                }).collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order with id {} din not found", orderId);
                    return new NoSuchElementException("Order with id " + orderId + " din not found");
                });

        return mapper.orderToOrderDTO(order);
    }

    @Override
    public List<OrderDTO> findAll() {
        log.info("Returning list of orders");
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(mapper::orderToOrderDTO).collect(Collectors.toList());
    }
}
