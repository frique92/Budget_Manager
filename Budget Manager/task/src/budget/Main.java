package budget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        PurchaseManager purchaseManager = new PurchaseManager();
        purchaseManager.run();
    }
}

enum CategoriesPurchase {
    FOOD("Food"),
    CLOTHES("Clothes"),
    ENTERTAINMENT("Entertainment"),
    OTHER("Other"),
    ALL("All");

    private String name;

    CategoriesPurchase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

enum StateMenu {
    MAIN, PURCHASE, LIST
}

class Menu {
    private StateMenu currentState = StateMenu.MAIN;

    public void showMenu() {
        switch (currentState) {
            case MAIN:
                showMainMenu();
                break;
            case PURCHASE:
                showCategoriesForPurchase();
                break;
            case LIST:
                showCategoriesForList();
                break;
            default:
                System.out.println("Unknown action!");
        }
    }

    private void showMainMenu() {
        System.out.println("Choose your action:");
        System.out.println("1) Add income");
        System.out.println("2) Add purchase");
        System.out.println("3) Show list of purchases");
        System.out.println("4) Balance");
        System.out.println("5) Save");
        System.out.println("6) Load");
        System.out.println("0) Exit");
    }

    private void showCategoriesForPurchase() {
        System.out.println("Choose the type of purchase");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");
        System.out.println("5) Back");
    }

    private void showCategoriesForList() {
        System.out.println("Choose the type of purchase");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");
        System.out.println("5) ALL");
        System.out.println("6) Back");
    }

    public void setCurrentState(StateMenu currentState) {
        this.currentState = currentState;
    }

    public StateMenu getCurrentState() {
        return currentState;
    }
}

class PurchaseManager {
    private float balance;
    private final Scanner scanner = new Scanner(System.in);
    private Map<CategoriesPurchase, ArrayList<Purchase>> purchases = new LinkedHashMap<>();
    private boolean isWorking;
    private Menu menu = new Menu();
    private String fileName = "purchases.txt";

    public void run() {
        isWorking = true;

        boolean firstStart = true;
        while (isWorking) {
            if (!firstStart) System.out.println();
            if (firstStart) firstStart = false;

            menu.showMenu();
            String action = scanner.nextLine();
            System.out.println();
            selectAction(action);
        }
    }

    private void selectAction(String action) {
        switch (menu.getCurrentState()) {
            case MAIN:
                selectActionMain(action);
                break;
            case PURCHASE:
                selectActionPurchase(action);
                break;
            case LIST:
                selectActionList(action);
                break;
        }
    }

    private void selectActionMain(String action) {
        switch (action) {
            case "1":
                addIncome();
                break;
            case "2":
                menu.setCurrentState(StateMenu.PURCHASE);
                break;
            case "3":
                menu.setCurrentState(StateMenu.LIST);
                break;
            case "4":
                showBalance();
                break;
            case "5":
                savePurchases();
                break;
            case "6":
                loadPurchases();
                break;
            case "0":
                exit();
                break;
            default:
                System.out.println("Unknown action!");
        }
    }

    private void selectActionPurchase(String action) {
        CategoriesPurchase category = null;
        switch (action) {
            case "1":
                category = CategoriesPurchase.FOOD;
                break;
            case "2":
                category = CategoriesPurchase.CLOTHES;
                break;
            case "3":
                category = CategoriesPurchase.ENTERTAINMENT;
                break;
            case "4":
                category = CategoriesPurchase.OTHER;
                break;
            case "5":
                menu.setCurrentState(StateMenu.MAIN);
                break;
            default:
                System.out.println("Unknown action!");
        }

        if (category != null) addPurchase(category);
    }

    private void selectActionList(String action) {

        CategoriesPurchase category = null;
        switch (action) {
            case "1":
                category = CategoriesPurchase.FOOD;
                break;
            case "2":
                category = CategoriesPurchase.CLOTHES;
                break;
            case "3":
                category = CategoriesPurchase.ENTERTAINMENT;
                break;
            case "4":
                category = CategoriesPurchase.OTHER;
                break;
            case "5":
                category = CategoriesPurchase.ALL;
                break;
            case "6":
                menu.setCurrentState(StateMenu.MAIN);
                break;
            default:
                System.out.println("Unknown action!");
        }

        if (category != null) showListOfPurchase(category);
    }


    private void addIncome() {
        System.out.println("Enter income:");
        balance += Float.parseFloat(scanner.nextLine());
        System.out.println("Income was added!");
    }

    private void addPurchase(CategoriesPurchase category) {
        System.out.println("Enter purchase name:");
        String name = scanner.nextLine();
        System.out.println("Enter its price:");
        float price = Float.parseFloat(scanner.nextLine());

        Purchase purchase = new Purchase.Builder()
                .setName(name)
                .setPrice(price)
                .setCategory(category)
                .build();

        if (purchases.containsKey(category)) {
            purchases.get(category).add(purchase);
        } else {
            ArrayList<Purchase> arr = new ArrayList<>();
            arr.add(purchase);
            purchases.put(category, arr);
        }


        System.out.println("Purchase was added!");

        balance -= price;
    }

    private void showListOfPurchase(CategoriesPurchase category) {
        System.out.println(category.getName() + ":");
        if (category == CategoriesPurchase.ALL) showAllList();
        else showCategoryList(category);
    }

    private void showAllList() {
        float total = 0;

        for (CategoriesPurchase category : purchases.keySet()) {
            for (Purchase purchase : purchases.get(category)) {
                total += purchase.getPrice();
                System.out.println(purchase);
            }
        }

        System.out.printf("Total sum: $%.2f\n", total);
    }

    private void showCategoryList(CategoriesPurchase category) {
        if (!purchases.containsKey(category)) {
            System.out.println("Purchase list is empty");
            return;
        }

        float total = 0;
        for (Purchase purchase : purchases.get(category)) {
            total += purchase.getPrice();
            System.out.println(purchase);
        }

        System.out.printf("Total sum: $%.2f\n", total);
    }

    private void showBalance() {
        System.out.printf("Balance: $%.2f\n", balance);
    }

    private void exit() {
        System.out.println("Bye!");
        isWorking = false;
    }

    private void loadPurchases() {
        purchases.clear();
        File file = new File(fileName);

        try (Scanner scannerFile = new Scanner(file)) {

            CategoriesPurchase category = null;
            ArrayList<Purchase> arr = new ArrayList<>();
            while (scannerFile.hasNext()) {
                String line = scannerFile.nextLine();
                if (line.matches("BALANCE.*")) {
                    balance = Float.parseFloat(line.split(":")[1]);
                } else if (line.matches("--.*")) {
                    CategoriesPurchase curCategory = CategoriesPurchase.valueOf(line.substring(2, line.length()));
                    if (category != null && curCategory != category) {
                        purchases.put(category, arr);
                        arr = new ArrayList<>();
                    }
                    category = curCategory;
                } else {
                    String[] dataPurchase = line.split(";");
                    Purchase purchase = new Purchase.Builder()
                            .setCategory(category)
                            .setName(dataPurchase[0])
                            .setPrice(Float.parseFloat(dataPurchase[1]))
                            .build();
                    arr.add(purchase);
                }
            }
            if (arr.size() > 0) purchases.put(category, arr);

            System.out.println("Purchases were loaded!");
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

    }

    private void savePurchases() {

        File file = new File(fileName);

        try (PrintWriter writer = new PrintWriter(file)) {

            for (Map.Entry<CategoriesPurchase, ArrayList<Purchase>> entry : purchases.entrySet()) {
                writer.println("--" + entry.getKey());

                for (Purchase purchase : entry.getValue()) {
                    writer.println(purchase.getName() + ";" + purchase.getPrice());
                }
            }

            writer.println("BALANCE:" + balance);

            System.out.println("Purchases were saved!");

        } catch (IOException e) {
            System.out.println("File not found.");
        }

    }

}

class Purchase {
    private String name;
    private float price;
    private CategoriesPurchase category;

    Purchase(String name, float price, CategoriesPurchase category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format("%s $%.2f", name, price);
    }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public CategoriesPurchase getCategory() {
        return category;
    }

    public static class Builder {
        private String name;
        private float price;
        private CategoriesPurchase category;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPrice(float price) {
            this.price = price;
            return this;
        }

        public Builder setCategory(CategoriesPurchase category) {
            this.category = category;
            return this;
        }

        public Purchase build() {
            return new Purchase(name, price, category);
        }
    }
}
