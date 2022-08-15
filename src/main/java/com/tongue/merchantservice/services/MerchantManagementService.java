package com.tongue.merchantservice.services;

import com.tongue.merchantservice.auth.dto.MerchantRegistrationForm;
import com.tongue.merchantservice.core.exceptions.EmailAlreadyRegisteredException;
import com.tongue.merchantservice.core.exceptions.RucAlreadyRegisteredException;
import com.tongue.merchantservice.core.exceptions.StoreNameAlreadyRegisteredException;
import com.tongue.merchantservice.domain.*;
import com.tongue.merchantservice.domain.enums.PaymentType;
import com.tongue.merchantservice.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class MerchantManagementService {

    private MerchantRepository merchantRepository;
    private StoreRepository storeRepository;
    private StoreVariantRepository storeVariantRepository;
    private AddressRepository addressRepository;
    private MenuRepository menuRepository;

    public MerchantManagementService(@Autowired MerchantRepository merchantRepository,
                                     @Autowired StoreRepository storeRepository,
                                     @Autowired StoreVariantRepository storeVariantRepository,
                                     @Autowired AddressRepository addressRepository,
                                     @Autowired MenuRepository menuRepository){

        this.merchantRepository=merchantRepository;
        this.storeRepository=storeRepository;
        this.storeVariantRepository=storeVariantRepository;
        this.addressRepository=addressRepository;
        this.menuRepository=menuRepository;

    }

    public Merchant createNewMerchantEnvironment(MerchantRegistrationForm form){

        log.debug("Creating Merchant Environment");

        log.debug("Validating email uniqueness...");

        Optional<Merchant> wrapperM = merchantRepository.findByEmail(form.getEmail());
        if (wrapperM.isPresent())
            throw new EmailAlreadyRegisteredException();

        log.debug("Validating store name uniqueness...");

        Optional<Store> wrapperS = storeRepository.findByName(form.getStoreName());
        if (wrapperS.isPresent())
            throw new StoreNameAlreadyRegisteredException(form.getStoreName());

        log.debug("Validating store ruc uniqueness");

        Optional<Store> wrapperSr = storeRepository.findByRuc(form.getRuc());
        if (wrapperSr.isPresent())
            throw new RucAlreadyRegisteredException();

        log.debug("Saving Merchant object with the repository...");

        Merchant merchant = Merchant.builder()
                .name(form.getName())
                .email(form.getEmail())
                .password(form.getPassword())
                .phoneNumber(form.getPhoneNumber())
                .build();

        merchant = merchantRepository.save(merchant);

        log.debug("Creating a Store and saving it with the repository...");

        Store store = Store.builder()
                .merchant(merchant)
                .name(form.getStoreName())
                .ruc(form.getRuc())
                .createdAt(Instant.now().toString())
                .build();

        store = storeRepository.save(store);

        log.debug("Creating a default Store Variant for that store and saving it with the repository...");

        StoreVariant storeVariant = StoreVariant.builder()
                .store(store)
                .name(form.getStoreName())
                .createdAt(Instant.now())
                .address(new Address(null,form.getAddress(),form.getLatitude(),form.getLongitude()))
                .paymentType(PaymentType.CREDIT_CARD)
                .build();

        storeVariant =  storeVariantRepository.save(storeVariant);

        log.debug("Creating a default store variant menu and saving...");

        Menu menu = Menu.builder()
                .storeVariant(storeVariant)
                .build();

        menu = menuRepository.save(menu);

        log.debug("Ok");

        return merchant;
    }

}
