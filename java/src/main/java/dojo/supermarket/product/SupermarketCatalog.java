package dojo.supermarket.product;

import dojo.supermarket.product.Product;

public interface SupermarketCatalog {
    void addProduct(Product product, double price);

    double getUnitPrice(Product product);

}
