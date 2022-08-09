package com.tongue.merchantservice.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tongue.merchantservice.auth.dto.AuthenticationCredentials;
import com.tongue.merchantservice.auth.dto.GoogleAuthenticationCredentials;
import com.tongue.merchantservice.auth.dto.MerchantRegistrationForm;
import com.tongue.merchantservice.core.ApiResponse;
import com.tongue.merchantservice.domain.Merchant;
import com.tongue.merchantservice.services.MerchantAuthenticationService;
import com.tongue.merchantservice.services.MerchantManagementService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Slf4j
@AutoConfigureMockMvc
public class LoginMerchantTest {

    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;

    @Autowired
    MerchantManagementService managementService;
    @Autowired
    MerchantAuthenticationService authenticationService;

    Gson gson = new Gson();

    Merchant registeredMerchant;
    MerchantRegistrationForm registrationFormAlex;

    @Before
    public void setUp(){

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        registrationFormAlex = MerchantRegistrationForm
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

    }

    @Test
    @Transactional
    public void givenThatAMerchantAccountExistsWhenLoginCredentialsAreOkThenReturnValidJwt() throws Exception {

        registeredMerchant = managementService.createNewMerchantEnvironment(registrationFormAlex);

        AuthenticationCredentials credentials = AuthenticationCredentials.builder()
                .email(registeredMerchant.getEmail())
                .password(registeredMerchant.getPassword())
                .build();

        MvcResult result = this.mvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(credentials)))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        ApiResponse<String> apiResponse = gson.fromJson(json,ApiResponse.class);
        String jwt = apiResponse.getSuccess().getPayload();

        Merchant merchant = authenticationService.getMerchantFromJwt(jwt);

        assert merchant.getId()==registeredMerchant.getId();

    }

    @Test
    @Transactional
    public void givenThatLoginPasswordIsWrongWhenAuthenticatingThenReturnWrongPasswordMessage() throws Exception  {

        registeredMerchant = managementService.createNewMerchantEnvironment(registrationFormAlex);

        String expected = "Wrong Password";
        AuthenticationCredentials credentials = AuthenticationCredentials.builder()
                .email(registeredMerchant.getEmail())
                .password("1111111111111")
                .build();

        MvcResult result = this.mvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(credentials)))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        ApiResponse<String> apiResponse = gson.fromJson(json,ApiResponse.class);
        assert apiResponse.getError().getMessage().equalsIgnoreCase(expected);

    }

    @Test
    @Transactional
    public void givenThatLoginEmailDoesntExistWhenAuthenticatingThenReturnNoSuchMerchantMessage() throws Exception {

        registeredMerchant = managementService.createNewMerchantEnvironment(registrationFormAlex);

        String expected = "No such Merchant with that email";

        AuthenticationCredentials credentials = AuthenticationCredentials.builder()
                .email("fake_email@gmail.com")
                .password(registeredMerchant.getPassword())
                .build();

        MvcResult result = this.mvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(credentials)))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        ApiResponse<String> apiResponse = gson.fromJson(json,ApiResponse.class);
        assert apiResponse.getError().getMessage().equalsIgnoreCase(expected);

    }

    @Test
    @Transactional
    public void givenThatGoogleJwtIsNotValidWhenAuthenticatingThenReturnBadRequest() throws Exception {

        registeredMerchant = managementService.createNewMerchantEnvironment(registrationFormAlex);

        GoogleAuthenticationCredentials credentials = GoogleAuthenticationCredentials.builder()
                .email(registeredMerchant.getEmail())
                .idToken("invalid-token")
                .build();

        MvcResult result = this.mvc.perform(post("/auth/login/google")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(credentials)))
                .andReturn();

        assert result.getResponse().getStatus() == HttpServletResponse.SC_BAD_REQUEST;

    }

}
