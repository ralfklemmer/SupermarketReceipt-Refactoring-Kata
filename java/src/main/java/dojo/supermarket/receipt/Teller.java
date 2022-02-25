package dojo.supermarket.receipt;

import dojo.supermarket.discount.Discount;
import dojo.supermarket.discount.SpecialOfferType;
import dojo.supermarket.product.Product;
import dojo.supermarket.shopping.ProductQuantity;
import dojo.supermarket.product.SupermarketCatalog;
import dojo.supermarket.shopping.ShoppingCart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Teller {

    private final SupermarketCatalog catalog;
    private Map<Product, Offer> offers = new HashMap<>();

    public Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    public void addSpecialOffer(SpecialOfferType offerType, Product product, double argument) {
        this.offers.put(product, new Offer(offerType, product, argument));
    }

    public Receipt checksOutArticlesFrom(ShoppingCart theCart) {
        ReceiptItemAdapter adapter = new ReceiptItemAdapter(catalog);
        List<ReceiptItem> items = theCart.getItems().stream()
                .map(adapter::toReceiptItem)
                .collect(Collectors.toList());

        Optional<Discount> discount = theCart.handleOffers(this.offers, this.catalog);

        Receipt receipt = new Receipt(items);
        discount.ifPresent(receipt::addDiscount);

        return receipt;
    }

    public class ReceiptItemAdapter {
        private final SupermarketCatalog catalog;

        public ReceiptItemAdapter(SupermarketCatalog catalog) {
            this.catalog = catalog;
        }

        public ReceiptItem toReceiptItem(ProductQuantity pq) {
            Product p = pq.getProduct();
            double quantity = pq.getQuantity();
            double unitPrice = catalog.getUnitPrice(p);
            double price = quantity * unitPrice;
            return new ReceiptItem(p, quantity, unitPrice, price);
        }
    }
}
