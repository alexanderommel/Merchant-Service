package com.tongue.merchantservice.resources;

import com.tongue.merchantservice.core.ApiResponse;
import com.tongue.merchantservice.domain.Category;
import com.tongue.merchantservice.domain.Store;
import com.tongue.merchantservice.services.StoreManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
public class CategoryRestController {

    private StoreManagementService storeManagementService;

    public CategoryRestController(@Autowired StoreManagementService storeManagementService){
        this.storeManagementService=storeManagementService;
    }

    @PostMapping("/stores/{storeId}/categories")
    public ApiResponse createNewCategory(@PathVariable(name = "storeId") String storeId,
                                         @RequestBody Category category,
                                         Principal principal){
        Store store = storeManagementService.getStoreByMerchantId(principal.getName());
        if (!store.getId().toString().equalsIgnoreCase(storeId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You are trying to access a resource that's not of your own");
        category.setStore(store);
        category = storeManagementService.createNewCategory(category);
        return ApiResponse.success(category);
    }

    @GetMapping("/stores/{storeId}/categories")
    public ApiResponse getAllCategories(@PathVariable(name = "storeId") String storeId){
        List<Category> categoryList = storeManagementService.findAllByStoreId(storeId);
        return ApiResponse.success(categoryList);
    }

}
