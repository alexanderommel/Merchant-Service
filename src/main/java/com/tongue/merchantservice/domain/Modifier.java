package com.tongue.merchantservice.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Modifier {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private GroupModifier groupModifier;

    private String name;
    private BigDecimal price;

}
