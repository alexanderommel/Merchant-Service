package com.tongue.merchantservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Store store;

    private String name;
    private String description;
    private Instant createdAt;
    private String categoryImageUrl;

}
