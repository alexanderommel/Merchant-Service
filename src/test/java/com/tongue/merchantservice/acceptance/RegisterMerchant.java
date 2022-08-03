package com.tongue.merchantservice.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tongue.merchantservice.auth.dto.MerchantRegistrationForm;
import com.tongue.merchantservice.core.ApiResponse;
import com.tongue.merchantservice.domain.Address;
import com.tongue.merchantservice.domain.Merchant;
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
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Slf4j
@AutoConfigureMockMvc
public class RegisterMerchant {

    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;

    Gson gson = new Gson();

    @Before
    public void setUp(){

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

    }

    @Test
    public void givenThatRegistrationFormIsOkThenRegisterAndReturnHttp200() throws Exception {

        MerchantRegistrationForm registrationForm = MerchantRegistrationForm
                .builder()
                .name("Daniel")
                .storeName("Pizza Luna")
                .email("danielvalarezo122@gmail.com")
                .phoneNumber("0911133444")
                .address("Quito CCI2")
                .latitude("1.01111")
                .longitude("2.000091")
                .password("secret")
                .ruc("1831092986003")
                .build();

        MvcResult result = this.mvc.perform(get("/auth/register")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(registrationForm)))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        ApiResponse apiResponse = gson.fromJson(json,ApiResponse.class);
        assert apiResponse.getOk() == true;

    }

    @Test
    public void givenThatRegistrationFormRucIsAlreadyRegisteredThenNotReturnHttp200() throws Exception {

        MerchantRegistrationForm registrationFormAlex = MerchantRegistrationForm
                .builder()
                .name("Alex")
                .storeName("Kfc")
                .email("alexanderommelsw@gmail.com")
                .phoneNumber("0979533444")
                .address("Quito CCI")
                .latitude("1.01111")
                .longitude("2.000091")
                .password("secret")
                .ruc("1732416778003")
                .build();

        MerchantManagementService managementService;
        Merchant merchant = managementService.createNewMerchantEnvironment(registrationFormAlex);

        String repeatedRuc = registrationFormAlex.getRuc();

        MerchantRegistrationForm registrationFormDiana = MerchantRegistrationForm
                .builder()
                .name("Diana")
                .storeName("Pizza Sol")
                .email("dianavalarezo122@gmail.com")
                .phoneNumber("0911133444")
                .address("Quito CCI2")
                .latitude("1.01111")
                .longitude("2.000091")
                .password("secret")
                .ruc(repeatedRuc)
                .build();

        MvcResult result = this.mvc.perform(get("/auth/register")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(registrationFormDiana)))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        ApiResponse apiResponse = gson.fromJson(json,ApiResponse.class);
        log.trace(apiResponse.getError().getMessage());
        assert apiResponse.getOk() == false;

    }

}
