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

    public OrderServiceImpl(OrderRepository orderRepository,
                            BucketService bucketService,
                            EmailService emailService,
                            UserService userService,
                            ProductService productService) {
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

        BucketDTO bucketDto = bucketService.getBucketDtoByUserId(userDTO.id);
        List<OrderDetails> details = getOrderDetails(bucketDto, order);
        order.setDetails(details);
        order.setSum(new BigDecimal(bucketDto.sum));
        log.info("Set Order Details to new Order");

        order.setDelivery(orderRequest.getDelivery());
        order.setPayment(orderRequest.getPayment());
        log.info("Set Delivery and Payment to new Order");

        orderRepository.save(order);
        log.info("Order with id {} was created and saved into DB", order.getId());
        
        // Оновлення рейтингу товарів
        updateProductRatings(details);

        orderInform(userDTO, order);
        bucketService.clearBucket(bucketDto.getId());
        log.info("Bucket was cleared");
        return order;
    }

    public void orderInform(UserDTO userDTO, Order order) {
        String massage = "Ваше замовлення прийнято у роботу, Номер замовлення "+ order.getId();
        emailService.send(userDTO.getEmail(),massage, "Ваше замовлення");
        
        // Формуємо красивий HTML email для адміністратора
        String adminEmailBody = buildAdminOrderEmail(order);
        emailService.send("sales@e-chem.com.ua", adminEmailBody, "🛒 Нове замовлення #" + order.getId());

        log.info("Order with id {} was sent by Emails", order.getId());
    }
    
    private String buildAdminOrderEmail(Order order) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'><style>");
        html.append("body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }");
        html.append(".container { background-color: #ffffff; border-radius: 10px; padding: 30px; max-width: 800px; margin: 0 auto; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }");
        html.append(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 10px 10px 0 0; margin: -30px -30px 20px -30px; text-align: center; }");
        html.append(".header h1 { margin: 0; font-size: 28px; }");
        html.append(".info-section { background-color: #f8f9fa; padding: 15px; border-radius: 8px; margin-bottom: 20px; }");
        html.append(".info-label { font-weight: bold; color: #495057; }");
        html.append(".info-value { color: #212529; margin-left: 10px; }");
        html.append(".status-badge { display: inline-block; padding: 5px 15px; border-radius: 20px; font-weight: bold; background-color: #17a2b8; color: white; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        html.append("th { background-color: #343a40; color: white; padding: 12px; text-align: left; }");
        html.append("td { padding: 12px; border-bottom: 1px solid #dee2e6; }");
        html.append(".total-row { background-color: #e9ecef; font-weight: bold; font-size: 18px; }");
        html.append(".footer { text-align: center; margin-top: 30px; color: #6c757d; font-size: 12px; }");
        html.append("</style></head><body>");
        html.append("<div class='container'>");
        html.append("<div class='header'><h1>🛒 Нове замовлення #").append(order.getId()).append("</h1></div>");
        
        // Загальна інформація
        html.append("<div class='info-section'>");
        html.append("<h3>📋 Загальна інформація</h3>");
        html.append("<p><span class='info-label'>📅 Дата створення:</span><span class='info-value'>").append(order.getCreated()).append("</span></p>");
        html.append("<p><span class='info-label'>👤 Клієнт:</span><span class='info-value'>").append(order.getUser().getUsername())
            .append(" (").append(order.getUser().getEmail()).append(")</span></p>");
        html.append("<p><span class='info-label'>📱 Телефон:</span><span class='info-value'>").append(order.getUser().getPhone()).append("</span></p>");
        html.append("<p><span class='info-label'>🚚 Доставка:</span><span class='info-value'>").append(order.getDelivery()).append("</span></p>");
        html.append("<p><span class='info-label'>💳 Оплата:</span><span class='info-value'>").append(order.getPayment()).append("</span></p>");
        html.append("<p><span class='info-label'>📊 Статус:</span> <span class='status-badge'>").append(order.getStatus().name()).append("</span></p>");
        html.append("</div>");
        
        // Товари в замовленні
        html.append("<h3>🛍️ Товари в замовленні</h3>");
        html.append("<table>");
        html.append("<thead><tr><th>Назва товару</th><th style='text-align: center;'>Кількість</th><th style='text-align: right;'>Ціна за од.</th><th style='text-align: right;'>Сума</th></tr></thead>");
        html.append("<tbody>");
        
        for (OrderDetails detail : order.getDetails()) {
            html.append("<tr>");
            html.append("<td>").append(detail.getProduct().getTitle()).append("</td>");
            html.append("<td style='text-align: center;'>").append(detail.getAmount()).append("</td>");
            html.append("<td style='text-align: right;'>").append(detail.getPrice()).append(" грн</td>");
            html.append("<td style='text-align: right;'>").append(detail.getAmount().multiply(detail.getPrice())).append(" грн</td>");
            html.append("</tr>");
        }
        
        html.append("<tr class='total-row'>");
        html.append("<td colspan='3' style='text-align: right;'>Загальна сума:</td>");
        html.append("<td style='text-align: right; color: #28a745;'>").append(order.getSum()).append(" грн</td>");
        html.append("</tr>");
        html.append("</tbody></table>");
        
        html.append("<div class='footer'>");
        html.append("<p>Цей email згенеровано автоматично системою ЕКОХІМ</p>");
        html.append("</div>");
        html.append("</div></body></html>");
        
        return html.toString();
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
        log.debug("Get order details for {} private method", order.getId());
        return bucketDTO.productList.stream()
                .map(productDto -> {
                    OrderDetails detail = new OrderDetails();
                    detail.setProduct(productService.getProduct(productDto.getProductId()));
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
    
    @Override
    public List<OrderDTO> findOrdersByUsername(String username) {
        log.info("Returning list of orders for user {}", username);
        List<Order> orders = orderRepository.findByUsername(username);
        return orders.stream().map(mapper::orderToOrderDTO).collect(Collectors.toList());
    }
    
    private void updateProductRatings(List<OrderDetails> details) {
        details.forEach(detail -> {
            var product = detail.getProduct();
            if (product != null) {
                Long currentCount = product.getOrderCount() != null ? product.getOrderCount() : 0L;
                product.setOrderCount(currentCount + detail.getAmount().longValue());
                log.debug("Updated order count for product {} to {}", product.getId(), product.getOrderCount());
            }
        });
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        log.info("Updating order {} status to {}", orderId, newStatus);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + orderId));
        
        order.setStatus(newStatus);
        orderRepository.save(order);
        log.info("Order {} status updated successfully", orderId);
    }
}
