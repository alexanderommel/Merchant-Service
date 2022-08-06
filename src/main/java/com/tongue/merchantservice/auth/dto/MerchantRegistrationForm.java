package com.tongue.merchantservice.auth.dto;

import com.tongue.merchantservice.domain.Address;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MerchantRegistrationForm {

    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String storeName;
    @NonNull
    private String address;
    @NonNull
    private String latitude;
    @NonNull
    private String longitude;
    @NonNull
    private String ruc;
    @NonNull
    private String password;

}
