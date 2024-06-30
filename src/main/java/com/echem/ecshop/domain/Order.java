package com.echem.ecshop.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="orders")
@ToString(exclude = "user")
public class Order {
    private static final String SEQ_NAME = "order_seq";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;
    
    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    private LocalDateTime closed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal sum;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderDetails> details;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String delivery;
    private String payment;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", created=" + created +
                ", updated=" + updated +
                ", closed=" + closed +
                ", user=" + user.getEmail() +
                ", sum=" + sum +
                ", details=" + details +
                ", status=" + status +
                ", delivery='" + delivery + '\'' +
                ", payment='" + payment + '\'' +
                '}';
    }
}