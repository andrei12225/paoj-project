package shop.services;

import java.util.Scanner;

import shop.data.StoreInventory;
import shop.enums.AmpTechnology;
import shop.enums.BassPickupType;
import shop.enums.GuitarPickupType;
import shop.enums.KeyAction;
import shop.enums.ShellMaterial;
import shop.models.Accessory;
import shop.models.Amplifier;
import shop.models.Bass;
import shop.models.Guitar;
import shop.models.Keyboard;

public class ProductInputHandler {
    StoreInventory inventory;
    Scanner scanner;

    public ProductInputHandler(StoreInventory inventory, Scanner scanner) {
        this.inventory = inventory;
        this.scanner = scanner; 

    }

    public void handleAddGuitar(String productName, double productPrice, int productStock) {
        System.out.println("Enter brand: ");
        String brand = scanner.nextLine();
        System.out.println("Enter model: ");
        String model = scanner.nextLine();
        System.out.println("Enter number of strings: ");
        int numberOfStrings = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter pickup type,");
        System.out.println("Available pickup types:");
        for (GuitarPickupType pickupType : GuitarPickupType.values()) {
            System.out.println("- " + pickupType);
        }
        String pickupTypeStr = scanner.nextLine();
        GuitarPickupType pickupType;
        try {
            pickupType = GuitarPickupType.valueOf(pickupTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid pickup type. Defaulting to SINGLE_COIL.");
            pickupType = GuitarPickupType.SINGLE_COIL;
        }
        Guitar newGuitar = ProductFactory.createGuitar(0, productName, productPrice, productStock, brand, model, numberOfStrings, pickupType);
        inventory.addProduct(newGuitar);
    }

    public void handleAddAmplifier(String productName, double productPrice, int productStock) {
        System.out.println("Enter brand: ");
        String brand = scanner.nextLine();
        System.out.println("Enter wattage: ");
        int wattage = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter technology,");
        System.out.println("Available technologies:");
        for (AmpTechnology tech : AmpTechnology.values()) {
            System.out.println("- " + tech);
        }
        String techStr = scanner.nextLine();
        AmpTechnology technology;
        try {            
            technology = AmpTechnology.valueOf(techStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid technology. Defaulting to TUBE.");
            technology = AmpTechnology.TUBE;
        }
        System.out.println("Enter speaker size: ");
        double speakerSize = Double.parseDouble(scanner.nextLine());
        Amplifier newAmplifier = ProductFactory.createAmplifier(0, productName, productPrice, productStock, brand, wattage, technology, speakerSize);
        inventory.addProduct(newAmplifier);
    }

    public void handleAddKeyboard(String productName, double productPrice, int productStock) {
        System.out.println("Enter key count: ");
        int keyCount = Integer.parseInt(scanner.nextLine());
        System.out.println("Is it digital? (yes/no): ");
        String isDigitalStr = scanner.nextLine();
        boolean isDigital = isDigitalStr.equalsIgnoreCase("yes");
        System.out.println("Enter key action,");
        System.out.println("Available key actions:");
        for (KeyAction action : KeyAction.values()) {
            System.out.println("- " + action);
        }
        String actionStr = scanner.nextLine();
        KeyAction keyAction;
        try {
            keyAction = KeyAction.valueOf(actionStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid key action. Defaulting to SYNTH_ACTION.");
            keyAction = KeyAction.SYNTH_ACTION;
        }
        Keyboard newKeyboard = ProductFactory.createKeyboard(0, productName, productPrice, productStock, keyCount, isDigital, keyAction);
        inventory.addProduct(newKeyboard);
    }

    public void handleAddBass(String productName, double productPrice, int productStock) {
        System.out.println("Enter string count: ");
        int stringCount = Integer.parseInt(scanner.nextLine());
        System.out.println("Is it active? (yes/no): ");
        String isActiveStr = scanner.nextLine();
        boolean isActive = isActiveStr.equalsIgnoreCase("yes");
        System.out.println("Enter pickup type,");
        System.out.println("Available pickup types:");
        for (BassPickupType pickupType : BassPickupType.values()) {
            System.out.println("- " + pickupType);
        }
        String pickupTypeStr = scanner.nextLine();
        BassPickupType pickupType;
        try {
            pickupType = BassPickupType.valueOf(pickupTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid pickup type. Defaulting to JAZZ.");
            pickupType = BassPickupType.JAZZ;
        }
        Bass newBass = ProductFactory.createBass(0, productName, productPrice, productStock, stringCount, isActive, pickupType);
        inventory.addProduct(newBass);
    }

    public void handleAddDrumSet(String productName, double productPrice, int productStock) {
        System.out.println("Enter piece count: ");
        int pieceCount = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter shell material,");
        System.out.println("Available shell materials:");
        for (ShellMaterial material : ShellMaterial.values()) {
            System.out.println("- " + material);
        }
        String materialStr = scanner.nextLine();
        ShellMaterial shellMaterial;
        try {
            shellMaterial = ShellMaterial.valueOf(materialStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid shell material. Defaulting to ALDER.");
            shellMaterial = ShellMaterial.ALDER;
        }
        System.out.println("Includes cymbals? (yes/no): ");
        String includesCymbalsStr = scanner.nextLine();
        boolean includesCymbals = includesCymbalsStr.equalsIgnoreCase("yes");
        shop.models.Drumset newDrumSet = ProductFactory.createDrumset(0, productName, productPrice, productStock, pieceCount, shellMaterial, includesCymbals);
        inventory.addProduct(newDrumSet);
    }

    public void handleAddAccessory(String productName, double productPrice, int productStock) {
        System.out.println("Enter accessory category,");
        System.out.println("Available categories:");
        for (shop.enums.AccessoryCategory category : shop.enums.AccessoryCategory.values()) {
            System.out.println("- " + category);
        }
        String categoryStr = scanner.nextLine();
        shop.enums.AccessoryCategory category;
        try {
            category = shop.enums.AccessoryCategory.valueOf(categoryStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid category. Defaulting to STRINGS.");
            category = shop.enums.AccessoryCategory.STRINGS;
        }
        System.out.println("Enter target instrument: ");
        String targetInstrument = scanner.nextLine();
        System.out.println("Enter pack quantity: ");
        int packQuantity = Integer.parseInt(scanner.nextLine());
        Accessory newAccessory = ProductFactory.createAccessory(0, productName, productPrice, productStock, category, targetInstrument, packQuantity);
        inventory.addProduct(newAccessory);
    }
}
