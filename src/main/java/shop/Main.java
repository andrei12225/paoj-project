package shop;

import shop.config.DatabaseInitializer;
import shop.data.StoreInventory;
import shop.enums.AccessoryCategory;
import shop.enums.AmpTechnology;
import shop.enums.BassPickupType;
import shop.enums.GuitarPickupType;
import shop.enums.KeyAction;
import shop.enums.ShellMaterial;
import shop.services.ConsoleUi;
import shop.services.PartnerFactory;
import shop.services.ProductFactory;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();

        StoreInventory inventory = StoreInventory.getInstance();

        seedInitialData(inventory);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Music Store Inventory System!");

        ConsoleUi ui = new ConsoleUi(inventory, scanner);
        ui.start();
    }

    private static void seedInitialData(StoreInventory inventory) {
        inventory.addProduct(ProductFactory.createGuitar("Gibson", 2000.0, 10, "Les Paul", "SG", 6, GuitarPickupType.HUMBUCKER));
        inventory.addProduct(ProductFactory.createAmplifier("Marshall", 1500.0, 5, "JCM800", 100, AmpTechnology.TUBE, 12.0));
        inventory.addProduct(ProductFactory.createKeyboard("Yamaha", 800.0, 20, 88, true, KeyAction.FULLY_WEIGHTED));
        inventory.addProduct(ProductFactory.createBass("Fender", 1200.0, 5, 4, true, BassPickupType.JAZZ));
        inventory.addProduct(ProductFactory.createDrumset("Pearl", 2500.0, 3, 5, ShellMaterial.MAPLE, true));
        inventory.addProduct(ProductFactory.createAccessory("Guitar Strings", 10.0, 100, AccessoryCategory.STRINGS, "Guitar", 6));

        inventory.addPartner(PartnerFactory.createPartner("Music World", "musicworld@example.com", 0.10));
    }
}
