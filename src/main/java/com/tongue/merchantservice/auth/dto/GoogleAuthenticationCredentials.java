package com.tongue.merchantservice.auth.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoogleAuthenticationCredentials {

    @NonNull
    private String email;
    @NonNull
    private String idToken;

}
