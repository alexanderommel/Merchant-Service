package com.tongue.merchantservice.auth.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationCredentials {

    @NonNull
    private String email;
    @NonNull
    private String password;

}
