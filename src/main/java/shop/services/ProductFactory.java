package shop.services;

import shop.enums.AccessoryCategory;
import shop.enums.AmpTechnology;
import shop.enums.BassPickupType;
import shop.enums.GuitarPickupType;
import shop.enums.KeyAction;
import shop.enums.ShellMaterial;
import shop.models.Accessory;
import shop.models.Amplifier;
import shop.models.Bass;
import shop.models.Drumset;
import shop.models.Guitar;
import shop.models.Keyboard;

public class ProductFactory {
    private static int idCounter = 1;

    private static String generateId() {
        return "PRD-" + String.format("%06d", idCounter++);
    }

    public static Guitar createGuitar(String name, double price, int stockQuantity, String brand, String model, int numberOfStrings, GuitarPickupType pickupType) {
        return new Guitar(generateId(), name, price, stockQuantity, brand, model, numberOfStrings, pickupType);
    }

        public static Guitar createGuitar(String id, String name, double price, int stockQuantity, String brand, String model, int numberOfStrings, GuitarPickupType pickupType) {
        return new Guitar(id, name, price, stockQuantity, brand, model, numberOfStrings, pickupType);
    }

    public static Bass createBass(String name, double price, int stockQuantity, int stringCount, boolean isActive, BassPickupType pickupType) {
        return new Bass(generateId(), name, price, stockQuantity, stringCount, isActive, pickupType);
    }

    public static Bass createBass(String id, String name, double price, int stockQuantity, int stringCount, boolean isActive, BassPickupType pickupType) {
        return new Bass(id, name, price, stockQuantity, stringCount, isActive, pickupType);
    }

    public static Drumset createDrumset(String name, double price, int stockQuantity, int numberOfPieces, ShellMaterial shellMaterial, boolean includesCymbals) {
        return new Drumset(generateId(), name, price, stockQuantity, numberOfPieces, shellMaterial, includesCymbals);
    }

    public static Drumset createDrumset(String id, String name, double price, int stockQuantity, int numberOfPieces, ShellMaterial shellMaterial, boolean includesCymbals) {
        return new Drumset(id, name, price, stockQuantity, numberOfPieces, shellMaterial, includesCymbals);
    }

    public static Keyboard createKeyboard(String name, double price, int stockQuantity, int keyCount, boolean isDigital, KeyAction keyAction) {
        return new Keyboard(generateId(), name, price, stockQuantity, keyCount, isDigital, keyAction);
    }

    public static Keyboard createKeyboard(String id, String name, double price, int stockQuantity, int keyCount, boolean isDigital, KeyAction keyAction) {
        return new Keyboard(id, name, price, stockQuantity, keyCount, isDigital, keyAction);
    }

    public static Accessory createAccessory(String name, double price, int stockQuantity, AccessoryCategory type, String targetInstrument, int packQuantity) {
        return new Accessory(generateId(), name, price, stockQuantity, type, targetInstrument, packQuantity);
    }

    public static Accessory createAccessory(String id, String name, double price, int stockQuantity, AccessoryCategory type, String targetInstrument, int packQuantity) {
        return new Accessory(id, name, price, stockQuantity, type, targetInstrument, packQuantity);
    }

    public static Amplifier createAmplifier(String name, double price, int stockQuantity, String brand, int wattage, AmpTechnology technology, double speakerSize) {
        return new Amplifier(generateId(), name, price, stockQuantity, brand, wattage, technology, speakerSize);
    }

    public static Amplifier createAmplifier(String id, String name, double price, int stockQuantity, String brand, int wattage, AmpTechnology technology, double speakerSize) {
        return new Amplifier(id, name, price, stockQuantity, brand, wattage, technology, speakerSize);
    }
}
