package com.tongue.merchantservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Store {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(optional = false)
    private Merchant merchant;

    private String createdAt;
    private String name;
    private String ruc;

}
