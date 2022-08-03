package com.tongue.merchantservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuSection {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Category category;

    @OneToMany
    private List<Product> products;

}
