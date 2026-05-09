package shop.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import shop.config.DatabaseConnection;
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
import shop.models.Product;
import shop.services.ProductFactory;

public class ProductRepository {
    public void create(Product product) {
        String sql = "INSERT INTO products (id, name, price, stock_quantity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getStockQuantity());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create product", exception);
        }

        if (product instanceof Guitar) {
            insertGuitar((Guitar) product);
        } else if (product instanceof Bass) {
            insertBass((Bass) product);
        } else if (product instanceof Keyboard) {
            insertKeyboard((Keyboard) product);
        } else if (product instanceof Amplifier) {
            insertAmplifier((Amplifier) product);
        } else if (product instanceof Accessory) {
            insertAccessory((Accessory) product);
        } else if (product instanceof Drumset) {
            insertDrumset((Drumset) product);
        } else {
            throw new IllegalArgumentException("Unknown product type: " + product.getClass().getName());
        }
    }

    private void insertGuitar(Guitar guitar) {
        String sql = "INSERT INTO guitars (id, brand, model, number_of_strings, pickup_type) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, guitar.getId());
            stmt.setString(2, guitar.getBrand());
            stmt.setString(3, guitar.getModel());
            stmt.setInt(4, guitar.getNumberOfStrings());
            stmt.setString(5, guitar.getPickupType().name());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create guitar", exception);
        }
    }

    private void insertKeyboard(Keyboard keyboard) {
        String sql = "INSERT INTO keyboards (id, key_count, is_digital, key_action) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, keyboard.getId());
            stmt.setInt(2, keyboard.getKeyCount());
            stmt.setBoolean(3, keyboard.getIsDigital());
            stmt.setString(4, keyboard.getKeyAction().name());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create keyboard", exception);
        }
    }

    private void insertBass(Bass bass) {
        String sql = "INSERT INTO basses (id, string_count, is_active, pickup_type) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, bass.getId());
            stmt.setInt(2, bass.getStringCount());
            stmt.setBoolean(3, bass.getIsActive());
            stmt.setString(4, bass.getPickupType().name());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create bass", exception);
        }
    }

    private void insertAmplifier(Amplifier amplifier) {
        String sql = "INSERT INTO amplifiers (id, brand, wattage, technology, speaker_size) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, amplifier.getId());
            stmt.setString(2, amplifier.getBrand());
            stmt.setDouble(3, amplifier.getWattage());
            stmt.setString(4, amplifier.getTechnology().name());
            stmt.setDouble(5, amplifier.getSpeakerSize());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create amplifier", exception);
        }
    }

    private void insertAccessory(Accessory accessory) {
        String sql = "INSERT INTO accessories (id, category, target_instrument, pack_quantity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, accessory.getId());
            stmt.setString(2, accessory.getCategory().name());
            stmt.setString(3, accessory.getTargetInstrument());
            stmt.setInt(4, accessory.getPackQuantity());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create accessory", exception);
        }
    }

    private void insertDrumset(Drumset drumset) {
        String sql = "INSERT INTO drumsets (id, number_of_pieces, shell_material, includes_cymbals) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, drumset.getId());
            stmt.setInt(2, drumset.getNumberOfPieces());
            stmt.setString(3, drumset.getShellMaterial().name());
            stmt.setBoolean(4, drumset.getIncludesCymbals());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create drumset", exception);
        }
    }

    public void delete(Product product) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, product.getId());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to delete product", exception);
        }
    }

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();

        products.addAll(findAllGuitars());
        products.addAll(findAllBasses());
        products.addAll(findAllDrumsets());
        products.addAll(findAllKeyboards());
        products.addAll(findAllAccessories());
        products.addAll(findAllAmplifiers());

        return products;
    }

    public List<Product> findAllGuitars() {
        String sql = "SELECT p.*, g.brand, g.model, g.number_of_strings, g.pickup_type FROM products p JOIN guitars g ON p.id = g.id";
        List<Product> guitars = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            var resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int stockQuantity = resultSet.getInt("stock_quantity");
                String brand = resultSet.getString("brand");
                String model = resultSet.getString("model");
                int numberOfStrings = resultSet.getInt("number_of_strings");
                String pickupTypeStr = resultSet.getString("pickup_type");
                GuitarPickupType pickupType = GuitarPickupType.valueOf(pickupTypeStr);

                Guitar guitar = ProductFactory.createGuitar(id, name, price, stockQuantity, brand, model, numberOfStrings, pickupType);
                guitars.add(guitar);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find all guitars", exception);
        }

        return guitars;
    }

    public List<Product> findAllBasses() {
        String sql = "SELECT p.*, b.string_count, b.is_active, b.pickup_type FROM products p JOIN basses b ON p.id = b.id";
        List<Product> basses = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            var resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int stockQuantity = resultSet.getInt("stock_quantity");
                int stringCount = resultSet.getInt("string_count");
                boolean isActive = resultSet.getBoolean("is_active");
                String pickupTypeStr = resultSet.getString("pickup_type");
                BassPickupType pickupType = BassPickupType.valueOf(pickupTypeStr);

                Bass bass = ProductFactory.createBass(id, name, price, stockQuantity, stringCount, isActive, pickupType);
                basses.add(bass);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find all basses", exception);
        }

        return basses;
    }

    public List<Product> findAllDrumsets() {
        String sql = "SELECT p.*, d.number_of_pieces, d.shell_material, d.includes_cymbals FROM products p JOIN drumsets d ON p.id = d.id";
        List<Product> drumsets = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            var resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int stockQuantity = resultSet.getInt("stock_quantity");
                int numberOfPieces = resultSet.getInt("number_of_pieces");
                String shellMaterialStr = resultSet.getString("shell_material");
                ShellMaterial shellMaterial = ShellMaterial.valueOf(shellMaterialStr);
                boolean includesCymbals = resultSet.getBoolean("includes_cymbals");

                Drumset drumset = ProductFactory.createDrumset(id, name, price, stockQuantity, numberOfPieces, shellMaterial, includesCymbals);
                drumsets.add(drumset);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find all drumsets", exception);
        }

        return drumsets;
    }

    public List<Product> findAllKeyboards() {
        String sql = "SELECT p.*, k.key_count, k.is_digital, k.key_action FROM products p JOIN keyboards k ON p.id = k.id";
        List<Product> keyboards = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            var resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int stockQuantity = resultSet.getInt("stock_quantity");
                int keyCount = resultSet.getInt("key_count");
                boolean isDigital = resultSet.getBoolean("is_digital");
                String keyActionStr = resultSet.getString("key_action");
                KeyAction keyAction = KeyAction.valueOf(keyActionStr);

                Keyboard keyboard = ProductFactory.createKeyboard(id, name, price, stockQuantity, keyCount, isDigital, keyAction);
                keyboards.add(keyboard);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find all keyboards", exception);
        }

        return keyboards;
    }

    public List<Product> findAllAccessories() {
        String sql = "SELECT p.*, a.category, a.target_instrument, a.pack_quantity FROM products p JOIN accessories a ON p.id = a.id";
        List<Product> accessories = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            var resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int stockQuantity = resultSet.getInt("stock_quantity");
                String categoryStr = resultSet.getString("category");
                AccessoryCategory category = AccessoryCategory.valueOf(categoryStr);
                String targetInstrument = resultSet.getString("target_instrument");
                int packQuantity = resultSet.getInt("pack_quantity");

                Accessory accessory = ProductFactory.createAccessory(id, name, price, stockQuantity, category, targetInstrument, packQuantity);
                accessories.add(accessory);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find all accessories", exception);
        }

        return accessories;
    }

    public List<Product> findAllAmplifiers() {
        String sql = "SELECT p.*, a.brand, a.wattage, a.technology, a.speaker_size FROM products p JOIN amplifiers a ON p.id = a.id";
        List<Product> amplifiers = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            var resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int stockQuantity = resultSet.getInt("stock_quantity");
                String brand = resultSet.getString("brand");
                int wattage = resultSet.getInt("wattage");
                String technologyStr = resultSet.getString("technology");
                AmpTechnology technology = AmpTechnology.valueOf(technologyStr);
                double speakerSize = resultSet.getDouble("speaker_size");

                Amplifier amplifier = ProductFactory.createAmplifier(id, name, price, stockQuantity, brand, wattage, technology, speakerSize);
                amplifiers.add(amplifier);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find all amplifiers", exception);
        }

        return amplifiers;
    }

    public void update(Product product) {
        if (product instanceof Guitar) {
            updateGuitar((Guitar) product);
        } else if (product instanceof Bass) {
            updateBass((Bass) product);
        } else if (product instanceof Keyboard) {
            updateKeyboard((Keyboard) product);
        } else if (product instanceof Amplifier) {
            updateAmplifier((Amplifier) product);
        } else if (product instanceof Accessory) {
            updateAccessory((Accessory) product);
        } else if (product instanceof Drumset) {
            updateDrumset((Drumset) product);
        } else {
            throw new IllegalArgumentException("Unknown product type: " + product.getClass().getName());
        }
    }

    public void updateGuitar(Guitar guitar) {
        String sql = "UPDATE products p JOIN guitars g ON p.id = g.id SET p.name = ?, p.price = ?, p.stock_quantity = ?, g.brand = ?, g.model = ?, g.number_of_strings = ?, g.pickup_type = ? WHERE p.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, guitar.getName());
            stmt.setDouble(2, guitar.getPrice());
            stmt.setInt(3, guitar.getStockQuantity());
            stmt.setString(4, guitar.getBrand());
            stmt.setString(5, guitar.getModel());
            stmt.setInt(6, guitar.getNumberOfStrings());
            stmt.setString(7, guitar.getPickupType().name());
            stmt.setString(8, guitar.getId());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to update guitar", exception);
        }
    }

    public void updateBass(Bass bass) {
        String sql = "UPDATE products p JOIN basses b ON p.id = b.id SET p.name = ?, p.price = ?, p.stock_quantity = ?, b.string_count = ?, b.is_active = ?, b.pickup_type = ? WHERE p.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, bass.getName());
            stmt.setDouble(2, bass.getPrice());
            stmt.setInt(3, bass.getStockQuantity());
            stmt.setInt(4, bass.getStringCount());
            stmt.setBoolean(5, bass.getIsActive());
            stmt.setString(6, bass.getPickupType().name());
            stmt.setString(7, bass.getId());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to update bass", exception);
        }
    }

    public void updateKeyboard(Keyboard keyboard) {
        String sql = "UPDATE products p JOIN keyboards k ON p.id = k.id SET p.name = ?, p.price = ?, p.stock_quantity = ?, k.key_count = ?, k.is_digital = ?, k.key_action = ? WHERE p.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, keyboard.getName());
            stmt.setDouble(2, keyboard.getPrice());
            stmt.setInt(3, keyboard.getStockQuantity());
            stmt.setInt(4, keyboard.getKeyCount());
            stmt.setBoolean(5, keyboard.getIsDigital());
            stmt.setString(6, keyboard.getKeyAction().name());
            stmt.setString(7, keyboard.getId());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to update keyboard", exception);
        }
    }

    public void updateAmplifier(Amplifier amplifier) {
        String sql = "UPDATE products p JOIN amplifiers a ON p.id = a.id SET p.name = ?, p.price = ?, p.stock_quantity = ?, a.brand = ?, a.wattage = ?, a.technology = ?, a.speaker_size = ? WHERE p.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, amplifier.getName());
            stmt.setDouble(2, amplifier.getPrice());
            stmt.setInt(3, amplifier.getStockQuantity());
            stmt.setString(4, amplifier.getBrand());
            stmt.setDouble(5, amplifier.getWattage());
            stmt.setString(6, amplifier.getTechnology().name());
            stmt.setDouble(7, amplifier.getSpeakerSize());
            stmt.setString(8, amplifier.getId());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to update amplifier", exception);
        }
    }

    public void updateAccessory(Accessory accessory) {
        String sql = "UPDATE products p JOIN accessories a ON p.id = a.id SET p.name = ?, p.price = ?, p.stock_quantity = ?, a.category = ?, a.target_instrument = ?, a.pack_quantity = ? WHERE p.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, accessory.getName());
            stmt.setDouble(2, accessory.getPrice());
            stmt.setInt(3, accessory.getStockQuantity());
            stmt.setString(4, accessory.getCategory().name());
            stmt.setString(5, accessory.getTargetInstrument());
            stmt.setInt(6, accessory.getPackQuantity());
            stmt.setString(7, accessory.getId());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to update accessory", exception);
        }
    }

    public void updateDrumset(Drumset drumset) {
        String sql = "UPDATE products p JOIN drumsets d ON p.id = d.id SET p.name = ?, p.price = ?, p.stock_quantity = ?, d.number_of_pieces = ?, d.shell_material = ?, d.includes_cymbals = ? WHERE p.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, drumset.getName());
            stmt.setDouble(2, drumset.getPrice());
            stmt.setInt(3, drumset.getStockQuantity());
            stmt.setInt(4, drumset.getNumberOfPieces());
            stmt.setString(5, drumset.getShellMaterial().name());
            stmt.setBoolean(6, drumset.getIncludesCymbals());
            stmt.setString(7, drumset.getId());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to update drumset", exception);
        }
    }
}