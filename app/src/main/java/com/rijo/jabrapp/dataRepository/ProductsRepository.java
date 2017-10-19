package com.rijo.jabrapp.dataRepository;

import com.rijo.jabrapp.domains.product.Item;
import com.rijo.jabrapp.domains.product.Products;

/**
 * Created by rijogeorge on 8/18/17.
 */

public interface ProductsRepository {
    Products getProductsFrom(String urlString);

    Item getProductDetailsFrom(String urlString);
}
