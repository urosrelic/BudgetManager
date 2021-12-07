import products.Product;

import java.io.*;
import java.util.*;

public class BudgetManager {
    // HashMap for saving items by category
    private static final HashMap<Category, ArrayList<Product>> map = new HashMap<>();

    // Txt file for loading and saving
    private static final File file = new File("resources/purchases.txt");

    // Variables for balance and total amount;
    private static double balance = 0.0;
    private static double foodTotal = 0.0;
    private static double clothesTotal = 0.0;
    private static double entertainmentTotal = 0.0;
    private static double otherTotal = 0.0;
    private static double total = 0.0;

    // Scanner for reading input
    private static final Scanner scanner = new Scanner(System.in);

    public void printMenu() {
        boolean end = false;
        while(!end) {
            printOptions();
            int option = Integer.parseInt(scanner.nextLine());
            System.out.println();
            switch (option) {
                case 1:
                    addIncome();
                    break;
                case 2:
                    int category = -1;
                    while (category != 5) {
                        System.out.println("Choose the type of purchase");
                        System.out.println("1) Food");
                        System.out.println("2) Clothes");
                        System.out.println("3) Entertainment");
                        System.out.println("4) Other");
                        System.out.println("5) Back");
                        category = Integer.parseInt(scanner.nextLine());
                        addItem(category);
                    }
                    break;
                case 3:
                    showListOfPurchase();
                    System.out.println();
                    break;
                case 4:
                    System.out.println("Balance: $" + String.format("%.2f", balance));
                    System.out.println();
                    break;
                case 5:
                    saveToFile();
                    break;
                case 6:
                    loadFromFile();
                    break;
                case 7:
                    int sortOption;
                    while(true) {
                        printSortOptions();
                        sortOption = Integer.parseInt(scanner.nextLine());
                        System.out.println();
                        if (sortOption == 1) {
                            sortAll();
                        } else if(sortOption == 2) {
                            sortByType();
                        } else if(sortOption == 3) {
                            sortByCertainType();
                        }
                        else if(sortOption == 4) {
                            break;
                        }
                    }
                    break;
                case 0:
                    end = true;
                    System.out.println("Bye!");
                    System.out.println();
                    break;
                default:
                    System.out.println();
                    break;
            }
        }
    }

    private static void sortByCertainType() {
        System.out.println("Choose the type of purchase");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");

        int option = Integer.parseInt(scanner.nextLine());
        if(option != 1 && option != 2 && option != 3 && option != 4) {
            System.out.println();
            return;
        }

        Category category = Category.returnCategory(option);
        if(!map.containsKey(category)) {
            System.out.println();
            System.out.println("Purchase list is empty");
        } else {
            System.out.println();
            System.out.println(category);
            ArrayList<Product> list = new ArrayList<>();
            list.addAll(map.get(category));
            list.sort(new PriceComparator());

            for(Product p : list) {
                System.out.println(p);
            }
            if(category.getId() == 1) {
                System.out.println("Total sum: $" + String.format("%.2f", foodTotal));
            } else if(category.getId() == 2) {
                System.out.println("Total sum: $" + String.format("%.2f", clothesTotal));
            }else if(category.getId() == 3) {
                System.out.println("Total sum: $" + String.format("%.2f", entertainmentTotal));
            }else if(category.getId() == 4) {
                System.out.println("Total sum: $" + String.format("%.2f", otherTotal));
            }
        }
        System.out.println();

    }

    private static void sortByType() {
        ArrayList<Type> typeList = new ArrayList<>();
        typeList.add(new Type("Food", foodTotal));
        typeList.add(new Type("Clothes", clothesTotal));
        typeList.add(new Type("Entertainment", entertainmentTotal));
        typeList.add(new Type("Other", otherTotal));

        Collections.sort(typeList);
        System.out.println("Types:");
        for(Type t : typeList) {
            System.out.println(t);
        }

        System.out.println("Total sum: $" + String.format("%.2f", total));
        System.out.println();
    }

    private static void sortAll() {
        ArrayList<Product> allProducts = new ArrayList<>();
        for(Category category : map.keySet()) {
            allProducts.addAll(map.get(category));
        }

        if(allProducts.isEmpty()) {
            System.out.println("The purchase list is empty!");
            System.out.println();
            return;
        }

        allProducts.sort(Collections.reverseOrder());
        System.out.println("All:");
        for(Product p : allProducts) {

            System.out.println(p);
        }
        System.out.println();
    }

    private static void loadFromFile() {
        try(Scanner scanner = new Scanner(file)) {
            balance = Double.parseDouble(scanner.nextLine());
            foodTotal = Double.parseDouble(scanner.nextLine());
            clothesTotal = Double.parseDouble(scanner.nextLine());
            entertainmentTotal = Double.parseDouble(scanner.nextLine());
            otherTotal = Double.parseDouble(scanner.nextLine());
            total = Double.parseDouble(scanner.nextLine());

            while(scanner.hasNext()) {
                String[] tokens = scanner.nextLine().split(",");
                int categoryID = Integer.parseInt(tokens[0].trim());
                String itemName = tokens[1].trim();
                double price = Double.parseDouble(tokens[2].trim());

                Product product = new Product(itemName, price);
                Category category = Category.returnCategory(categoryID);

                if(!map.containsKey(category)) {
                    ArrayList<Product> products = new ArrayList<>();
                    products.add(product);
                    map.put(category, products);
                } else {
                    map.get(category).add(product);
                }
            }
            System.out.println("Purchases were loaded!");
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveToFile() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(balance + "\n");
            writer.write(foodTotal + "\n");
            writer.write(clothesTotal + "\n");
            writer.write(entertainmentTotal + "\n");
            writer.write(otherTotal + "\n");
            writer.write(total + "\n");

            for(Category category : map.keySet()) {
                for (Product product : map.get(category)) {
                    if(category.getId() == 1) {
                        writer.write(1 + "," + product.getName() + "," + product.getPrice() +
                                "\n");
                    } else if(category.getId() == 2) {
                        writer.write(2 + "," + product.getName() + "," + product.getPrice() +
                                "\n");
                    } else if(category.getId() == 3) {
                        writer.write(3 + "," + product.getName() + "," + product.getPrice() +
                                "\n");
                    } else if(category.getId() == 4) {
                        writer.write(4 + "," + product.getName() + "," + product.getPrice() +
                                "\n");
                    } else if(category.getId() == 5) {
                        writer.write(5 + "," + product.getName() + "," + product.getPrice() +
                                "\n");
                    }
                }
            }
            System.out.println("Purchases were saved");
            System.out.println();
        } catch (IOException e) {
            System.out.printf("An exception occurs %s", e.getMessage());
        }

    }
    private static void showListOfPurchase() {
        if (map.isEmpty()) {
            System.out.println("Product list is empty!");
            System.out.println();
            return;
        }

        while(true) {
            System.out.println("1) Food");
            System.out.println("2) Clothes");
            System.out.println("3) Entertainment");
            System.out.println("4) Other");
            System.out.println("5) All");
            System.out.println("6) Back");
            int categoryID = Integer.parseInt(scanner.nextLine());
            System.out.println();
            switch (categoryID) {
                case 1:
                    System.out.println("Food:");
                    if (!map.containsKey(Category.FOOD)) {
                        System.out.println("Product list is empty!");
                    }
                    for (Product product : map.get(Category.FOOD)) {
                        System.out.println(product);
                    }
                    System.out.println("Total sum: $" + String.format("%.2f", foodTotal));
                    System.out.println();
                    break;
                case 2:
                    System.out.println("Clothes:");
                    if (!map.containsKey(Category.CLOTHES)) {
                        System.out.println("Product list is empty!");
                    }
                    for (Product product : map.get(Category.CLOTHES)) {
                        System.out.println(product);
                    }
                    System.out.println("Total sum: $" +  String.format("%.2f", clothesTotal));
                    System.out.println();
                    break;
                case 3:
                    System.out.println("Entertainment:");
                    if (!map.containsKey(Category.ENTERTAINMENT)) {
                        System.out.println("Product list is empty!");
                    }
                    for (Product product : map.get(Category.ENTERTAINMENT)) {
                        System.out.println(product);
                    }
                    System.out.println("Total sum: $" +  String.format("%.2f", entertainmentTotal));
                    System.out.println();
                    break;
                case 4:
                    System.out.println("Other:");
                    if (!map.containsKey(Category.OTHER)) {
                        System.out.println("Product list is empty!");
                    }
                    for (Product product : map.get(Category.OTHER)) {
                        System.out.println(product);
                    }
                    System.out.println("Total sum: $" +  String.format("%.2f", otherTotal));
                    System.out.println();
                    break;
                case 5:
                    System.out.println("All:");
                    for (ArrayList<Product> products : map.values()) {
                        for (Product p : products) {
                            System.out.println(p);
                        }
                    }
                    System.out.println("Total sum: $" +  String.format("%.2f", total));
                    System.out.println();
                    break;
                case 6:
                    System.out.println();
                    return;
                default:
                    System.out.println();
            }
        }
    }

    private static void addItem(int categoryID) {
        Category category = Category.returnCategory(categoryID);

        if(categoryID != 1 && categoryID != 2 && categoryID != 3 && categoryID != 4) {
            System.out.println();
            return;
        }
        System.out.println();
        System.out.println("Enter purchase name: ");
        String name = scanner.nextLine();
        System.out.println("Enter its price: ");
        double price = Double.parseDouble(scanner.nextLine());

        Product newProduct = new Product(name, price);

        if(!map.containsKey(category)) {
            ArrayList<Product> productList = new ArrayList<>();
            productList.add(newProduct);
            map.put(category, productList);
        } else {
            map.get(category).add(newProduct);
        }

        if(categoryID == 1) {
            foodTotal += price;
        } else if(categoryID == 2) {
            clothesTotal += price;
        } else if(categoryID == 3) {
            entertainmentTotal += price;
        } else if(categoryID == 4) {
            otherTotal += price;
        }

        total += price;
        balance -= price;
        System.out.println("Product was added!");
        System.out.println();
    }

    private static void addIncome() {
        System.out.println("Enter income:");
        int income = Integer.parseInt(scanner.nextLine());
        balance += income;
        System.out.println("Income was added!");
        System.out.println();
    }

    private static void printSortOptions() {
        System.out.println("How do you want to sort?");
        System.out.println("1) Sort all purchases");
        System.out.println("2) Sort by type");
        System.out.println("3) Sort certain type");
        System.out.println("4) Back");
    }

    private static void printOptions() {
        System.out.println("Choose your action:");
        System.out.println("1) Add Income");
        System.out.println("2) Add Product");
        System.out.println("3) Show the list of purchases");
        System.out.println("4) Balance");
        System.out.println("5) Save");
        System.out.println("6) Load");
        System.out.println("7) Analyze (Sort)");
        System.out.println("0) Exit");
    }
}
