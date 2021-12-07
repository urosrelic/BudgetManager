public class Type implements Comparable<Type>{
    private final String type;
    private final double price;

    public Type(String type, double price) {
        this.type = type;
        this.price = price;
    }

    @Override
    public int compareTo(Type o) {
        return Double.compare(o.price, this.price);
    }

    @Override
    public String toString() {
        return type + " - $" + String.format("%.2f", price);
    }
}
