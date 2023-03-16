package com.tongue.merchantservice.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tongue.merchantservice.auth.dto.AuthenticationCredentials;
import com.tongue.merchantservice.auth.dto.MerchantRegistrationForm;
import com.tongue.merchantservice.core.ApiResponse;
import com.tongue.merchantservice.domain.Category;
import com.tongue.merchantservice.domain.Merchant;
import com.tongue.merchantservice.domain.Store;
import com.tongue.merchantservice.services.MerchantManagementService;
import com.tongue.merchantservice.services.StoreManagementService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Slf4j
@AutoConfigureMockMvc
public class CreateNewCategoryTest {

    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;
    @Autowired
    MerchantManagementService managementService;

    @Autowired
    StoreManagementService storeManagementService;

    Gson gson = new Gson();

    Merchant registeredMerchant;
    MerchantRegistrationForm registrationFormAlex;

    @Before
    public void setUp() {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        registrationFormAlex = MerchantRegistrationForm
                .builder()
                .name("Alejandro")
                .storeName("Kfc")
                .email("alejandro@gmail.com")
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
    public void givenThatIHaveARestaurantWhenCreatingANewCategoryThenItMustBeSavedSuccessfully() throws Exception {

        registeredMerchant = managementService.createNewMerchantEnvironment(registrationFormAlex);
        Store store = storeManagementService.getStoreByMerchantId(registeredMerchant.getId().toString());

        Category category0 = Category.builder()
                .name("Carnes")
                .description("Carnes a la brasa exclusiva para ti")
                .categoryImageUrl("https://freeimages.com/3")
                .store(store)
                .build();

        Category category1 = Category.builder()
                .name("Pizzas")
                .description("Pizzas a la brasa exclusiva para ti")
                .categoryImageUrl("https://freeimages.com/2")
                .store(store)
                .build();



        storeManagementService.createNewCategory(category0);
        storeManagementService.createNewCategory(category1);

        String storeId = store.getId().toString();

        Category category2 = Category.builder()
                .name("Hamburguesas")
                .description("Hamburguesas a la brasa exclusiva para ti")
                .categoryImageUrl("https://freeimages.com/1")
                .build();

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(registeredMerchant.getId().toString());

        MvcResult postResult = this.mvc.perform(post("/stores/"+storeId+"/categories")
                        .principal(mockPrincipal)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(category2)))
                .andReturn();

        String json = postResult.getResponse().getContentAsString();
        ApiResponse<Category> apiResponse = gson.fromJson(json,ApiResponse.class);
        String payloadJson = mapper.writeValueAsString(apiResponse.getSuccess().getPayload());
        Category persistedCategory = gson.fromJson(payloadJson,Category.class);

        MvcResult getResult = this.mvc.perform(get("/stores/"+storeId+"/categories")
                        .contentType("application/json"))
                .andReturn();

        String json2 = getResult.getResponse().getContentAsString();
        ApiResponse<List<Category>> apiResponse1 = gson.fromJson(json2,ApiResponse.class);
        String payloadJson2 = mapper.writeValueAsString(apiResponse1.getSuccess().getPayload());
        List<Category> existingCategories = mapper.readValue(payloadJson2,new TypeReference<>(){});

        existingCategories = existingCategories
                .stream()
                .filter(c->c.getId()==persistedCategory.getId())
                .collect(Collectors.toList());

        assert existingCategories.size()==1;

    }

    @Test
    @Transactional
    public void givenThatIHaveARestaurantWhenCreatingARepeatedCategoryThenReturnBadRequest() throws Exception {

        registeredMerchant = managementService.createNewMerchantEnvironment(registrationFormAlex);

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(registeredMerchant.getId().toString());

        Category category0 = Category.builder()
                .name("Carnes")
                .description("Carnes a la brasa exclusiva para ti")
                .categoryImageUrl("https://freeimages.com/3")
                .build();

        Store store = storeManagementService.getStoreByMerchantId(registeredMerchant.getId().toString());
        category0.setStore(store);
        storeManagementService.createNewCategory(category0);

        String storeId = store.getId().toString();

        MvcResult postResult = this.mvc.perform(post("/stores/"+storeId+"/categories")
                        .principal(mockPrincipal)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(category0)))
                .andReturn();


        assert postResult.getResponse().getStatus() == HttpServletResponse.SC_BAD_REQUEST;

    }

}
