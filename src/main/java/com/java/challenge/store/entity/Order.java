package com.java.challenge.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    private Long id;
    @Column("order_date")
    private LocalDateTime date;

    private BigDecimal total;

    private String status;
}
