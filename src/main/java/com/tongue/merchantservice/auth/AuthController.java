package com.tongue.merchantservice.auth;

import com.tongue.merchantservice.auth.dto.MerchantRegistrationForm;
import com.tongue.merchantservice.core.ApiResponse;
import com.tongue.merchantservice.services.MerchantManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {

    private MerchantManagementService merchantManagementService;

    public AuthController(@Autowired MerchantManagementService merchantManagementService){
        this.merchantManagementService=merchantManagementService;
    }

    @PostMapping("/auth/register")
    public ApiResponse registerMerchant(@RequestBody MerchantRegistrationForm form){
        merchantManagementService.createNewMerchantEnvironment(form);
        return ApiResponse.success("Merchant registered successfully");
    }

}
