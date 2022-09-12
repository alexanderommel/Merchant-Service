package com.tongue.merchantservice.auth;

import com.tongue.merchantservice.auth.dto.AuthenticationCredentials;
import com.tongue.merchantservice.auth.dto.GoogleAuthenticationCredentials;
import com.tongue.merchantservice.auth.dto.MerchantRegistrationForm;
import com.tongue.merchantservice.core.ApiResponse;
import com.tongue.merchantservice.services.MerchantAuthenticationService;
import com.tongue.merchantservice.services.MerchantManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Slf4j
@RestController
public class AuthController {

    private MerchantManagementService merchantManagementService;
    private MerchantAuthenticationService authenticationService;

    public AuthController(@Autowired MerchantManagementService merchantManagementService,
                          @Autowired MerchantAuthenticationService authenticationService){
        this.merchantManagementService=merchantManagementService;
        this.authenticationService=authenticationService;
    }

    @PostMapping("/auth/register")
    public ApiResponse registerMerchant(@RequestBody MerchantRegistrationForm form){
        log.info("Creating new Merchant account...");
        log.info("Payload: "+form.toString());
        merchantManagementService.createNewMerchantEnvironment(form);
        return ApiResponse.success("Merchant registered successfully");
    }

    @PostMapping("/auth/login")
    public ApiResponse login(@RequestBody AuthenticationCredentials authenticationCredentials){
        log.info("Authenticating user...");
        log.info("Payload: "+authenticationCredentials.toString());
        String jwt = authenticationService.authenticateAndReturnJwt(authenticationCredentials);
        log.info("Jwt generated: "+jwt);
        return ApiResponse.success(jwt);
    }

    @PostMapping("/auth/login/google")
    public ApiResponse login(@RequestBody GoogleAuthenticationCredentials authenticationCredentials)
            throws GeneralSecurityException, IOException {
        String jwt = authenticationService.authenticateWithGoogleAndReturnJwt(authenticationCredentials);
        return ApiResponse.success(jwt);
    }

}
