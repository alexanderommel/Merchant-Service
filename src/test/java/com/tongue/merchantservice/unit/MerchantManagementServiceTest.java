package com.tongue.merchantservice.unit;

import com.tongue.merchantservice.auth.dto.MerchantRegistrationForm;
import com.tongue.merchantservice.core.exceptions.EmailAlreadyRegisteredException;
import com.tongue.merchantservice.core.exceptions.RucAlreadyRegisteredException;
import com.tongue.merchantservice.core.exceptions.StoreNameAlreadyRegisteredException;
import com.tongue.merchantservice.domain.Merchant;
import com.tongue.merchantservice.domain.Store;
import com.tongue.merchantservice.repositories.MerchantRepository;
import com.tongue.merchantservice.repositories.StoreRepository;
import com.tongue.merchantservice.services.MerchantManagementService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@RunWith(SpringRunner.class)
//@AutoConfigureDataJpa
//@AutoConfigureTestDatabase
//@AutoConfigureTestEntityManager
public class MerchantManagementServiceTest {

    @Autowired
    MerchantManagementService managementService;
    @Autowired
    MerchantRepository merchantRepository;
    @Autowired
    StoreRepository storeRepository;

    String des_test1 = "Dado que el formulario de registro es correcto, cuando" +
            "registro un nuevo negociante, entonces al obtener la lista de negociantes" +
            "el email del negociante recientemente creado debe aparecer en la lista";

    @Test
    @Transactional
    public void test1() {
        log.trace(des_test1);

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

        Merchant merchant = managementService.createNewMerchantEnvironment(registrationForm);
        List<Merchant> merchants = merchantRepository.findAll();
        merchants = merchants
                .stream()
                .filter(m -> m.getEmail() == merchant.getEmail())
                .collect(Collectors.toList());

        assert merchants.size() == 1;
    }

    String des_test2 = "Dado que el formulario de registro es correcto, cuando" +
            "registro un nuevo negociante, entonces se debe crear una tienda asociada a" +
            "el negociante recientemente creado";

    @Test
    @Transactional
    public void test2() {
        log.trace(des_test2);

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

        Merchant merchant = managementService.createNewMerchantEnvironment(registrationForm);
        List<Store> stores = storeRepository.findAll();
        stores = stores
                .stream()
                .filter(s -> s.getMerchant().getId() == merchant.getId())
                .collect(Collectors.toList());
        assert stores.size() == 1;
    }

    String des_test3 = "Dado que ya existe un negociante con el email 'alexanderommelsw@gmail.com', " +
            "cuando quiero registrar un nuevo negociante con el mismo correo, entonces" +
            "se debe lanzar una excepción del tipo EmailAlreadyRegisteredException";

    @Test
    @Transactional
    public void test3() {
        log.trace(des_test3);
        Boolean exceptionCaught = false;

        MerchantRegistrationForm registrationForm1 = MerchantRegistrationForm
                .builder()
                .name("Daniel")
                .storeName("Pizza Luna")
                .email("alexanderommelsw@gmail.com")
                .phoneNumber("0911133444")
                .address("Quito CCI2")
                .latitude("1.01111")
                .longitude("2.000091")
                .password("secret")
                .ruc("1831092986003")
                .build();

        MerchantRegistrationForm registrationForm2 = MerchantRegistrationForm
                .builder()
                .name("Alexander")
                .storeName("Pizza Luna")
                .email("alexanderommelsw@gmail.com")
                .phoneNumber("0911133444")
                .address("Quito CCI2")
                .latitude("1.01111")
                .longitude("2.000091")
                .password("secret")
                .ruc("3436092986003")
                .build();

        Merchant registered = managementService.createNewMerchantEnvironment(registrationForm1);

        try {
            Merchant merchant = managementService.createNewMerchantEnvironment(registrationForm2);
        } catch (EmailAlreadyRegisteredException e) {
            exceptionCaught = true;
        }
        assert exceptionCaught == true;

    }

    String des_test4 = "Dado que ya existe una tienda registrada con el nombre 'Pavos al horno'," +
            "cuando creo una nueva cuenta de negociante con el negocio 'Pavos al horno', entonces" +
            "se debe lanzar una excepción StoreNameAlreadyRegisteredException";

    @Test
    @Transactional
    public void test4(){
        log.trace(des_test4);

        Boolean exceptionCaught = false;

        MerchantRegistrationForm registrationForm1 = MerchantRegistrationForm
                .builder()
                .name("Alexander1")
                .storeName("Pavos al horno")
                .email("alexanderommelsw1@gmail.com")
                .phoneNumber("0911133444")
                .address("Quito CCI2")
                .latitude("1.01111")
                .longitude("2.000091")
                .password("secret")
                .ruc("1831092986003")
                .build();

        MerchantRegistrationForm registrationForm2 = MerchantRegistrationForm
                .builder()
                .name("Alexander2")
                .storeName("Pavos al horno")
                .email("alexanderommelsw2@gmail.com")
                .phoneNumber("0911133444")
                .address("Quito CCI2")
                .latitude("1.01111")
                .longitude("2.000091")
                .password("secret")
                .ruc("1131092986003")
                .build();

        Merchant registered = managementService.createNewMerchantEnvironment(registrationForm1);

        try{
            Merchant merchant = managementService.createNewMerchantEnvironment(registrationForm2);
        }catch (StoreNameAlreadyRegisteredException e){
            exceptionCaught = true;
        }
        assert exceptionCaught == true;

    }

    String des_test5 = "Dado que ya existe una tienda registrada con el ruc '176241607003'," +
            "cuando creo una nueva cuenta de negociante con el ruc '176241607003', entonces" +
            "se debe lanzar una excepción RucAlreadyRegisteredException";

    @Test
    @Transactional
    public void test5(){
        log.trace(des_test5);
        Boolean exceptionCaught = false;

        MerchantRegistrationForm registrationForm1 = MerchantRegistrationForm
                .builder()
                .name("Alexander1")
                .storeName("Pavos al horno")
                .email("alexanderommelsw1@gmail.com")
                .phoneNumber("0911133444")
                .address("Quito CCI2")
                .latitude("1.01111")
                .longitude("2.000091")
                .password("secret")
                .ruc("176241607003")
                .build();

        MerchantRegistrationForm registrationForm2 = MerchantRegistrationForm
                .builder()
                .name("Alexander2")
                .storeName("Pavos a la brasa")
                .email("alexanderommelsw2@gmail.com")
                .phoneNumber("0911133444")
                .address("Quito CCI2")
                .latitude("1.01111")
                .longitude("2.000091")
                .password("secret")
                .ruc("176241607003")
                .build();

        Merchant registered = managementService.createNewMerchantEnvironment(registrationForm1);

        try{

            Merchant merchant = managementService.createNewMerchantEnvironment(registrationForm2);
        }catch (RucAlreadyRegisteredException e){
            exceptionCaught = true;
        }
        assert exceptionCaught == true;

    }

}
