package com.tongue.merchantservice.unit;

import com.tongue.merchantservice.core.exceptions.CategoryAlreadyCreatedException;
import com.tongue.merchantservice.core.exceptions.StoreNotFoundException;
import com.tongue.merchantservice.domain.Category;
import com.tongue.merchantservice.domain.Store;
import com.tongue.merchantservice.repositories.CategoryRepository;
import com.tongue.merchantservice.repositories.MerchantRepository;
import com.tongue.merchantservice.repositories.StoreRepository;
import com.tongue.merchantservice.services.StoreManagementService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class StoreManagementServiceTest {

    @Mock
    StoreRepository storeRepository;
    @Mock
    CategoryRepository categoryRepository;
    StoreManagementService storeManagementService;
    ArrayList<Category> savedCategories;
    Store existingStore;

    @Before
    public void StoreManagementServiceTest(){

        existingStore = Store.builder()
                .id(1L)
                .build();

        savedCategories = new ArrayList<>();

        savedCategories.add(Category.builder()
                .id(1L)
                .name("c1")
                .description("d1")
                .store(existingStore)
                .build());

        savedCategories.add(
                Category.builder()
                        .id(2L)
                        .name("c2")
                        .description("d2")
                        .store(existingStore)
                        .build());

        savedCategories.add(Category.builder()
                .id(3L)
                .name("c3")
                .description("d3")
                .store(existingStore)
                .build());

        Mockito.when(categoryRepository.findAllByStoreId(existingStore.getId())).thenReturn(savedCategories);
        Mockito.when(storeRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        Mockito.when(storeRepository.findById(existingStore.getId())).thenReturn(Optional.of(existingStore));

        storeManagementService = new StoreManagementService(storeRepository,categoryRepository);
    }

    @Test
    public void givenThatInputDataIsOkWhenCreatingANewCategoryThenItMustBeInTheListOfCategories(){



        Category newCategory2 = Category.builder()
                .id(4L)
                .name("c54")
                .description("d5")
                .store(existingStore)
                .build();

        Mockito.when(categoryRepository.save(ArgumentMatchers.any())).thenReturn(newCategory2);

        Category recentlySavedCategory
                = storeManagementService.createNewCategory(newCategory2);

        savedCategories.add(newCategory2);

        List<Category> categories = categoryRepository.findAllByStoreId(existingStore.getId());

        categories = categories
                .stream()
                .filter(c->c.getId()== recentlySavedCategory.getId())
                .collect(Collectors.toList());

        assert categories.size() == 1;

    }

    @Test
    public void givenThatCategoryNameAlreadyExistsWhenCreatingANewOneThenThrowCategoryAlreadyCreatedException(){

        Boolean exceptionCaught = false;

        Category newCategory = Category.builder()
                .id(5L)
                .name("c3")
                .description("description 6")
                .store(existingStore)
                .build();

        try {
            storeManagementService.createNewCategory(newCategory);
        }catch (CategoryAlreadyCreatedException exception){
            exceptionCaught = true;
        }

        assert exceptionCaught == true;

    }

    @Test
    public void givenThatCategoryNameExistsInDifferentOwnerStoreWhenCreatingThenNoExceptionsMustBeThrown(){

        Boolean exceptionCaught = false;

        Store otherStore = Store.builder()
                .id(2L)
                .build();

        Category otherStoreCategory = Category.builder()
                .id(5L)
                .name("c3")
                .description("d3")
                .store(otherStore)
                .build();

        List<Category> categories = new ArrayList<>();

        Mockito.when(categoryRepository.findAllByStoreId(otherStore.getId())).thenReturn(categories);
        Mockito.when(storeRepository.findById(otherStore.getId())).thenReturn(Optional.of(otherStore));

        try {
            storeManagementService.createNewCategory(otherStoreCategory);
        }catch (Exception exception){
            log.info(exception.getMessage());
            exceptionCaught = true;
        }

        assert exceptionCaught == false;

    }

    @Test
    public void givenThatStoreDontExistsWhenCreatingNewCategoryThenThrowStoreNotFoundException(){

        Boolean exceptionCaught = false;

        Store otherStore = Store.builder()
                .id(5L)
                .build();

        Category category = Category.builder()
                .name("c77")
                .description("d3")
                .store(otherStore)
                .build();

        try {
            storeManagementService.createNewCategory(category);
        }catch (StoreNotFoundException exception){
            exceptionCaught = true;
        }

        assert exceptionCaught == true;

    }

}
