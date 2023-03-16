package com.tongue.merchantservice.unit;

import com.tongue.merchantservice.auth.dto.AuthenticationCredentials;
import com.tongue.merchantservice.auth.dto.MerchantRegistrationForm;
import com.tongue.merchantservice.core.exceptions.NoSuchEmailRegisteredException;
import com.tongue.merchantservice.core.exceptions.WrongPasswordException;
import com.tongue.merchantservice.domain.Merchant;
import com.tongue.merchantservice.repositories.MerchantRepository;
import com.tongue.merchantservice.services.MerchantAuthenticationService;
import com.tongue.merchantservice.services.MerchantManagementService;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MerchantAuthenticationServiceTest {

    @Autowired
    MerchantManagementService managementService;

    @Autowired
    MerchantAuthenticationService authenticationService;

    Merchant registeredMerchant;
    MerchantRegistrationForm registrationFormAlex;

    public MerchantAuthenticationServiceTest(){

        registrationFormAlex = MerchantRegistrationForm
                .builder()
                .name("Alexander")
                .storeName("Kfc")
                .email("alexanderrrr@gmail.com")
                .phoneNumber("0979533444")
                .address("Quito CCI")
                .latitude("1.01111")
                .longitude("2.000091")
                .password("secret")
                .ruc("1762416778003")
                .build();

    }

    @Test
    @Transactional
    public void shouldCreateGenerateAJwtGivenThatAuthenticationCredentialsAreOk(){

        registeredMerchant = managementService.createNewMerchantEnvironment(registrationFormAlex);

        AuthenticationCredentials credentials = AuthenticationCredentials.builder()
                .email(registeredMerchant.getEmail())
                .password(registeredMerchant.getPassword())
                .build();

        String jwt = authenticationService.authenticateAndReturnJwt(credentials);

        Merchant m = authenticationService.getMerchantFromJwt(jwt);

        assert m.getId() == registeredMerchant.getId();

    }

    @Test
    @Transactional
    public void shouldReturnMalformedJwtExceptionWhenJwtIsWrong(){

        registeredMerchant = managementService.createNewMerchantEnvironment(registrationFormAlex);

        Boolean exceptionCaught = false;

        try {

            String badJwt = "asdasdasdasdas";
            authenticationService.getMerchantFromJwt(badJwt);

        }catch (MalformedJwtException ex){
            exceptionCaught = true;
        }

        assert exceptionCaught == true;

    }

    @Test
    @Transactional
    public void shouldReturnNoSuchEmailRegisteredExceptionWhenAuthenticationEmailDoesntExists(){

        registeredMerchant = managementService.createNewMerchantEnvironment(registrationFormAlex);

        Boolean exceptionCaught = false;

        AuthenticationCredentials credentials = AuthenticationCredentials.builder()
                .email("mal@gmail.com")
                .password(registeredMerchant.getPassword())
                .build();

        try {
            authenticationService.authenticateAndReturnJwt(credentials);
        }catch (NoSuchEmailRegisteredException ex){
            exceptionCaught = true;
        }

        assert  exceptionCaught == true;

    }

    @Test
    @Transactional
    public void shouldThrowWrongPasswordExceptionWhenAuthenticationPasswordIsWrong(){

        registeredMerchant = managementService.createNewMerchantEnvironment(registrationFormAlex);

        Boolean exceptionCaught = false;

        AuthenticationCredentials credentials = AuthenticationCredentials.builder()
                .email(registeredMerchant.getEmail())
                .password("Bad_Password")
                .build();

        try {
            authenticationService.authenticateAndReturnJwt(credentials);
        }catch (WrongPasswordException ex){
            exceptionCaught = true;
        }

        assert  exceptionCaught == true;

    }

}
