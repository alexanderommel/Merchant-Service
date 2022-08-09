package com.tongue.merchantservice.unit;

import com.tongue.merchantservice.domain.Category;
import com.tongue.merchantservice.domain.Store;
import com.tongue.merchantservice.repositories.CategoryRepository;
import com.tongue.merchantservice.repositories.MerchantRepository;
import com.tongue.merchantservice.repositories.StoreRepository;
import lombok.extern.slf4j.Slf4j;
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
    List<Category> savedCategories;
    Store existingStore;

    public StoreManagementServiceTest(){

        existingStore = Store.builder()
                .id(1L)
                .build();

        savedCategories = Arrays.asList(
                Category.builder()
                        .name("c1")
                        .description("d1")
                        .store(existingStore)
                        .build(),
                Category.builder()
                        .name("c2")
                        .description("d2")
                        .store(existingStore)
                        .build(),
                Category.builder()
                        .name("c3")
                        .description("d3")
                        .store(existingStore)
                        .build()
        );

        Mockito.when(categoryRepository.findAllByStoreId(existingStore.getId())).thenReturn(savedCategories);
        Mockito.when(storeRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(null));
        Mockito.when(storeRepository.findById(existingStore.getId())).thenReturn(Optional.of(existingStore));

        storeManagementService = new StoreManagementService(categoryRepository);
    }

    @Test
    public void givenThatInputDataIsOkWhenCreatingANewCategoryThenItMustBeInTheListOfCategories(){



        Category newCategory = Category.builder()
                .name("c4")
                .description("d5")
                .store(existingStore)
                .build();

        Mockito.when(categoryRepository.save(ArgumentMatchers.any())).thenReturn(newCategory);

        Category recentlySavedCategory
                = storeManagementService.createNewCategory(newCategory);

        List<Category> categories = categoryRepository.findAllByStoreId();

        categories = categories
                .stream()
                .filter(c->c.getId()== newCategory.getId())
                .collect(Collectors.toList());

        assert categories.size() == 1;

    }

    @Test
    public void givenThatCategoryNameAlreadyExistsWhenCreatingANewOneThenThrowCategoryAlreadyCreatedException(){

        Boolean exceptionCaught = false;

        Category newCategory = Category.builder()
                .name("c3")
                .description("description 6")
                .store(existingStore)
                .build();

        Mockito.when(categoryRepository.save(ArgumentMatchers.any())).thenReturn(newCategory);

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
                .name("c3")
                .description("d3")
                .store(otherStore)
                .build();

        List<Category> categories = new ArrayList<>();

        Mockito.when(categoryRepository.findAllByStoreId(otherStore.getId())).thenReturn(categories);
        Mockito.when(storeRepository.findById(otherStore.getId())).thenReturn(Optional.of(existingStore));

        try {
            storeManagementService.createNewCategory(otherStoreCategory);
        }catch (Exception exception){
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
