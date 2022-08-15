package com.tongue.merchantservice.services;

import com.tongue.merchantservice.core.exceptions.CategoryAlreadyCreatedException;
import com.tongue.merchantservice.core.exceptions.StoreNotFoundException;
import com.tongue.merchantservice.domain.Category;
import com.tongue.merchantservice.domain.Merchant;
import com.tongue.merchantservice.domain.Store;
import com.tongue.merchantservice.repositories.CategoryRepository;
import com.tongue.merchantservice.repositories.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StoreManagementService {

    private StoreRepository storeRepository;
    private CategoryRepository categoryRepository;

    public StoreManagementService(@Autowired StoreRepository storeRepository,
                                  @Autowired CategoryRepository categoryRepository){
        this.storeRepository=storeRepository;
        this.categoryRepository=categoryRepository;
    }

    public Store getStoreByMerchantId(String id){
        Optional<Store> wrapper = storeRepository.findByMerchantId(Long.valueOf(id));
        if (wrapper.isEmpty())
            throw new StoreNotFoundException();
        return wrapper.get();
    }

    public Category createNewCategory(Category category){
        Optional<Store> wrapper = storeRepository.findById(category.getStore().getId());
        if (wrapper.isEmpty())
            throw new StoreNotFoundException();
        Store store = wrapper.get();
        List<Category> categoryList = categoryRepository.findAllByStoreId(store.getId());
        categoryList = categoryList
                .stream()
                .filter(c->c.getName().equalsIgnoreCase(category.getName()))
                .collect(Collectors.toList());
        if (categoryList.size()!=0)
            throw new CategoryAlreadyCreatedException(category.getName());
        Category newCategory = category;
        newCategory.setId(null);
        return categoryRepository.save(category);
    }

    public List<Category> findAllByStoreId(String id){
        return categoryRepository.findAllByStoreId(Long.valueOf(id));
    }

}
