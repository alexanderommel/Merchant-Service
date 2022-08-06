package com.tongue.merchantservice.unit;

import com.tongue.merchantservice.auth.dto.MerchantRegistrationForm;
import com.tongue.merchantservice.domain.Merchant;
import com.tongue.merchantservice.repositories.MerchantRepository;
import com.tongue.merchantservice.services.MerchantManagementService;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
public class MerchantAuthenticationServiceTest {

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    MerchantManagementService managementService;

    @Autowired
    MerchantAuthenticationService authenticationService;

    Merchant registeredMerchant;

    @Before
    public void setUp(){

        MerchantRegistrationForm registrationFormAlex = MerchantRegistrationForm
                .builder()
                .name("Alexander")
                .storeName("Kfc")
                .email("alexanderommelsw@gmail.com")
                .phoneNumber("0979533444")
                .address("Quito CCI")
                .latitude("1.01111")
                .longitude("2.000091")
                .password("secret")
                .ruc("1762416778003")
                .build();

        registeredMerchant = managementService.createNewMerchantEnvironment(registrationFormAlex);

    }

    @Test
    public void shouldCreateGenerateAJwtGivenThatAuthenticationCredentialsAreOk(){

        AuthenticationCredentials credentials = AuthenticationCredentials.builder()
                .email(registeredMerchant.getEmail())
                .password(registeredMerchant.getPassword())
                .build();

        String jwt = authenticationService.authenticateAndReturnJwt(credentials);

        Merchant m = authenticationService.getMerchantFromJwt(jwt);

        assert m.getId() == registeredMerchant.getId();

    }

    @Test
    public void shouldReturnMalformedJwtExceptionWhenJwtIsWrong(){

        Boolean exceptionCaught = false;

        try {

            String badJwt = "asdasdasdasdas";
            authenticationService.getMerchantFromJwt(badJwt);

        }catch (MalformedJwtException ex){
            exceptionCaught = true;
        }

        assert exceptionCaught == true;

    }

    @Test void shouldReturnNoSuchEmailRegisteredExceptionWhenAuthenticationEmailDoesntExists(){

        Boolean exceptionCaught = false;

        AuthenticationCredentials credentials = AuthenticationCredentials.builder()
                .email("doesnt_exists@gmail.com")
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
    public void shouldThrowWrongPasswordExceptionWhenAuthenticationPasswordIsWrong(){

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
