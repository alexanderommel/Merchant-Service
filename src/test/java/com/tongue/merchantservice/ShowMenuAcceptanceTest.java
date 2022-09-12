package com.tongue.merchantservice;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class ShowMenuAcceptanceTest {

    /**

    String description = "Given that I have created a restaurant and some collections," +
            " When I watch the restaurant menu, Then it must return the created collections";

    public void test(){
        log.trace(description);

        // GIVEN
        MerchantStoreManagment storeManagment;
        Store store = storeManagment.getStore();
        storeManagment.createStoreVariant();
        StoreMenuManagment menuManagment;
        Collection c1;
        Collection c2;
        menuManagment.createCollection();
        menuManagment.createCollection();
        // WHEN
        StoreVariantCatalogue storeVariantCatalogue;
        List<Collection> collectionList =  storeVariantCatalogue.getMenu(storeVariantId);
        // THEN
        List<Collection> expected = Arrays.asList(
                c1,
                c2
        );
        for (Collection c: collectionList
        ) {
            expected = expected
                    .stream()
                    .filter(c1 -> c1.getId() == c.getId())
                    .collect(Collectors.toList());
        }
        assert expected.size() == 0;
    }

     **/

}
