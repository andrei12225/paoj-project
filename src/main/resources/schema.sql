CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'EMPLOYEE') NOT NULL,
    last_login DATETIME
);

CREATE TABLE IF NOT EXISTS user_permissions (
    user_id VARCHAR(50) NOT NULL,
    permission ENUM('MANAGE_USERS', 'MANAGE_PRODUCTS', 'MANAGE_PARTNERS') NOT NULL,
    PRIMARY KEY (user_id, permission),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS guitars (
    id VARCHAR(50) PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    number_of_strings INT NOT NULL DEFAULT 6,
    pickup_type ENUM('SINGLE_COIL', 'HUMBUCKER', 'P90', 'ACTIVE', 'PASSIVE') NOT NULL,
    FOREIGN KEY (id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS amplifiers (
    id VARCHAR(50) PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    wattage INT NOT NULL,
    technology ENUM('TUBE', 'SOLID_STATE', 'MODELING', 'HYBRID') NOT NULL,
    speaker_size DECIMAL(5, 2) NOT NULL,
    FOREIGN KEY (id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS drumsets (
    id VARCHAR(50) PRIMARY KEY,
    number_of_pieces INT NOT NULL,
    shell_material ENUM('MAPLE', 'BIRCH', 'MAHOGANY', 'ASH', 'ALDER', 'POPLAR') NOT NULL,
    includes_cymbals TINYINT(1) NOT NULL DEFAULT 0,
    FOREIGN KEY (id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS keyboards (
    id VARCHAR(50) PRIMARY KEY,
    key_count INT NOT NULL,
    is_digital TINYINT(1) NOT NULL DEFAULT 1,
    key_action ENUM('SYNTH_ACTION', 'SEMI_WEIGHTED', 'FULLY_WEIGHTED') NOT NULL,
    FOREIGN KEY (id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS basses (
    id VARCHAR(50) PRIMARY KEY,
    string_count INT NOT NULL DEFAULT 4,
    is_active TINYINT(1) NOT NULL DEFAULT 0,
    pickup_type ENUM('PRECISION', 'JAZZ', 'HUMBUCKER', 'SOAPBAR') NOT NULL,
    FOREIGN KEY (id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS accessories (
    id VARCHAR(50) PRIMARY KEY,
    category ENUM('STRINGS', 'PICKS', 'CABLES', 'STRAPS', 'CASES', 'STANDS', 'MAINTENANCE') NOT NULL,
    target_instrument VARCHAR(100),
    pack_quantity INT NOT NULL DEFAULT 1,
    FOREIGN KEY (id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS partners (
    partner_id VARCHAR(50) PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    discount_rate DECIMAL(5, 4) NOT NULL DEFAULT 0,
    total_spent DECIMAL(12, 2) NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS sale_periods (
    period_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    is_open TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS sale_records (
    transaction_id VARCHAR(50) PRIMARY KEY,
    period_id VARCHAR(50) NOT NULL,
    product_id VARCHAR(50) NOT NULL,
    partner_id VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    final_price_per_unit DECIMAL(10, 2) NOT NULL,
    sale_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (period_id) REFERENCES sale_periods(period_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (partner_id) REFERENCES partners(partner_id)
);
