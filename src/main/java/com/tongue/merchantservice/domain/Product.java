package com.tongue.merchantservice.domain;

import com.sun.istack.NotNull;
import com.tongue.merchantservice.domain.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Store store;

    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String productImageUrl;
    private Instant createdAt;
    private ProductStatus status;
    private String adjustments; // {price:25.50,date:25-06-2021}

}
