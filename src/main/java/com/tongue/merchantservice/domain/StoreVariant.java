package com.tongue.merchantservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tongue.merchantservice.domain.enums.PaymentType;
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
public class StoreVariant {

    @Id @GeneratedValue
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Store store;
    @ManyToOne(optional = false)
    private StoreType type;
    @ManyToOne
    private Address address;

    private String name;
    private String storeImageUrl;
    private PaymentType paymentType;
    private String contactPhone;
    private String contactEmail;
    private Instant createdAt;

}
