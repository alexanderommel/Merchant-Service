package com.tongue.merchantservice.unit;

import com.tongue.merchantservice.auth.dto.MerchantRegistrationForm;
import com.tongue.merchantservice.domain.Merchant;
import com.tongue.merchantservice.domain.Store;
import com.tongue.merchantservice.repositories.MerchantRepository;
import com.tongue.merchantservice.repositories.StoreRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MerchantManagementService {

    MerchantManagementService managementService;
    MerchantRepository merchantRepository;
    StoreRepository storeRepository;

    String des_test1 = "Dado que el formulario de registro es correcto, cuando" +
            "registro un nuevo negociante, entonces al obtener la lista de negociantes" +
            "el email del negociante recietemente creado debe aparecer en la lista";

    public void test1() {
        log.trace(des_test1);
        MerchantRegistrationForm registrationForm = MerchantRegistrationForm.builder().build();
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

    public void test2() {
        log.trace(des_test2);
        MerchantRegistrationForm registrationForm = MerchantRegistrationForm.builder().build();
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

    public void test3() {
        log.trace(des_test3);
        Boolean exceptionCaught = false;

        try {
            MerchantRegistrationForm registrationForm = MerchantRegistrationForm.builder().build();
            Merchant merchant = managementService.createNewMerchantEnvironment(registrationForm);
        } catch (EmailAlreadyRegisteredException e) {
            exceptionCaught = true;
        }
        assert exceptionCaught == true;

    }

    String des_test4 = "Dado que ya existe una tienda registrada con el nombre 'Pavos al horno'," +
            "cuando creo una nueva cuenta de negociante con el negocio 'Pavos al horno', entonces" +
            "se debe lanzar una excepción StoreNameAlreadyRegisteredException";

    public void test4(){
        log.trace(des_test4);
        Boolean exceptionCaught = false;

        try{
            MerchantRegistrationForm registrationForm = MerchantRegistrationForm.builder().build();
            Merchant merchant = managementService.createNewMerchantEnvironment(registrationForm);
        }catch (StoreNameAlreadyRegisteredException e){
            exceptionCaught = true;
        }
        assert exceptionCaught == true;

    }

    String des_test5 = "Dado que ya existe una tienda registrada con el ruc '176241607003'," +
            "cuando creo una nueva cuenta de negociante con el ruc '176241607003', entonces" +
            "se debe lanzar una excepción RucAlreadyRegisteredException";

    public void test5(){
        log.trace(des_test5);
        Boolean exceptionCaught = false;

        try{
            MerchantRegistrationForm registrationForm = MerchantRegistrationForm.builder().build();
            Merchant merchant = managementService.createNewMerchantEnvironment(registrationForm);
        }catch (RucAlreadyRegisteredException e){
            exceptionCaught = true;
        }
        assert exceptionCaught == true;

    }

}
