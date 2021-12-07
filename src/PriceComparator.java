import products.Product;

import java.util.Comparator;

class PriceComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        return -Double.compare(p1.getPrice(), p2.getPrice());
    }
}
