package budget;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PurchaseManager purchaseManager = new PurchaseManager();
        purchaseManager.run();
    }
}

class PurchaseManager {
    private float balance;
    private final Scanner scanner = new Scanner(System.in);
    private List<Purchase> purchases = new ArrayList<>();
    private boolean isWorking;

    public void run() {
        isWorking = true;

        while (isWorking) {
            showMenu();
            String action = scanner.nextLine();
            System.out.println();
            selectAction(action);
            System.out.println();
        }
    }

    private void showMenu() {
        System.out.println("Choose your action:");
        System.out.println("1) Add income");
        System.out.println("2) Add purchase");
        System.out.println("3) Show list of purchases");
        System.out.println("4) Balance");
        System.out.println("0) Exit");
    }

    private void selectAction(String action) {
        switch (action) {
            case "1":
                addIncome();
                break;
            case "2":
                addPurchase();
                break;
            case "3":
                showListOfPurchase();
                break;
            case "4":
                showBalance();
                break;
            case "0":
                exit();
                break;
            default:
                System.out.println("Unknown action!");
        }
    }

    private void addIncome() {
        System.out.println("Enter income:");
        balance += Float.parseFloat(scanner.nextLine());
        System.out.println("Income was added!");
    }

    private void addPurchase() {
        System.out.println("Enter purchase name:");
        String name = scanner.nextLine();
        System.out.println("Enter its price:");
        float price = Float.parseFloat(scanner.nextLine());

        Purchase purchase = new Purchase(name, price);
        purchases.add(purchase);

        System.out.println("Purchase was added!");

        balance -= price;
    }

    private void showListOfPurchase() {
        if (purchases.size() == 0) {
            System.out.println("Purchase list is empty");
            return;
        }

        float total = 0;
        for (Purchase purchase : purchases) {
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

}

class Purchase {
    private String name;
    private float price;

    Purchase(String name, float price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", name, price);
    }

    public float getPrice() {
        return price;
    }
}
