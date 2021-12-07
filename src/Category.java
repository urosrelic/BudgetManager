public enum Category {
    FOOD(1,"Food"),
    CLOTHES(2, "Clothes"),
    ENTERTAINMENT(3, "Entertainment"),
    OTHER(4,"Other");

    private int id;
    private String name;

    Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.name + ":";
    }

    public static Category returnCategory(int id) {
        if (id == 1) {
            return Category.FOOD;
        }
        if (id == 2) {
            return Category.CLOTHES;
        }
        if (id == 3) {
            return Category.ENTERTAINMENT;
        }
        else {
            return Category.OTHER;
        }

    }
}
