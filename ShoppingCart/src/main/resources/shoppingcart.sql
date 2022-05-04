-- MySQL Workbench Forward Engineering

SET
@OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET
@OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET
@OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema shoppingcart
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema shoppingcart
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `shoppingcart` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
USE
`shoppingcart`;

-- -----------------------------------------------------
-- Table `shoppingcart`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shoppingcart`.`users`
(
    `username` VARCHAR
(
    255
) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
    `password` VARCHAR
(
    255
) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
    `email` VARCHAR
(
    255
) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
    `address` VARCHAR
(
    255
) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
    `phone_number` VARCHAR
(
    255
) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
    PRIMARY KEY
(
    `username`
),
    UNIQUE INDEX `username`
(
    `username` ASC
) VISIBLE,
    UNIQUE INDEX `email`
(
    `email` ASC
) VISIBLE
    )
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `shoppingcart`.`carts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shoppingcart`.`carts`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    `username`
    VARCHAR
(
    255
) NULL DEFAULT NULL,
    PRIMARY KEY
(
    `id`
),
    FOREIGN KEY
(
    `username`
)
    REFERENCES `shoppingcart`.`users`
(
    `username`
)
    )
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `shoppingcart`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shoppingcart`.`categories`
(
    `code` VARCHAR
(
    10
) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
    `name` VARCHAR
(
    255
) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
    PRIMARY KEY
(
    `code`
)
    )
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `shoppingcart`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shoppingcart`.`products`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    `name`
    VARCHAR
(
    255
) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
    `price` DOUBLE NOT NULL,
    `color` VARCHAR
(
    255
) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL,
    `brand` VARCHAR
(
    255
) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL DEFAULT NULL,
    `category_code` VARCHAR
(
    10
) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
    `quantity` INT NOT NULL DEFAULT '0',
    PRIMARY KEY
(
    `id`
),
    INDEX `category_code`
(
    `category_code` ASC
) VISIBLE,
    CONSTRAINT `products_ibfk_1`
    FOREIGN KEY
(
    `category_code`
)
    REFERENCES `shoppingcart`.`categories`
(
    `code`
)
    )
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `shoppingcart`.`cart_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shoppingcart`.`cart_item`
(
    `id`
    INT
    NOT
    NULL,
    `product_id`
    INT
    NOT
    NULL,
    `cart_id`
    INT
    NOT
    NULL,
    `quantity`
    INT
    NOT
    NULL,
    PRIMARY
    KEY
(
    `id`
),
    INDEX `fk_cart_item_order_product_idx`
(
    `product_id` ASC
) VISIBLE,
    INDEX `fk_cart_item_cart1_idx`
(
    `cart_id` ASC
) VISIBLE,
    CONSTRAINT `FK_cart_item_carts`
    FOREIGN KEY
(
    `cart_id`
)
    REFERENCES `shoppingcart`.`carts`
(
    `id`
),
    CONSTRAINT `FK_cart_item_products`
    FOREIGN KEY
(
    `product_id`
)
    REFERENCES `shoppingcart`.`products`
(
    `id`
)
    )
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_bin;


-- -----------------------------------------------------
-- Table `shoppingcart`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shoppingcart`.`orders`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    `username`
    VARCHAR
(
    255
) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
    `date_created` DATETIME NOT NULL,
    `total_cost` FLOAT NOT NULL,
    `state` VARCHAR
(
    50
) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL,
    PRIMARY KEY
(
    `id`
),
    INDEX `user_id` USING BTREE
(
    `username`
) VISIBLE,
    CONSTRAINT `orders_ibfk_1`
    FOREIGN KEY
(
    `username`
)
    REFERENCES `shoppingcart`.`users`
(
    `username`
)
    )
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `shoppingcart`.`orders_products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `shoppingcart`.`orders_products`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    `order_id`
    INT
    NOT
    NULL,
    `product_id`
    INT
    NOT
    NULL,
    `quantity`
    INT
    NOT
    NULL,
    PRIMARY
    KEY
(
    `id`
),
    INDEX `orders_products_ibfk_1`
(
    `order_id` ASC
) VISIBLE,
    INDEX `orders_products_ibfk_2`
(
    `product_id` ASC
) VISIBLE,
    CONSTRAINT `orders_products_ibfk_1`
    FOREIGN KEY
(
    `order_id`
)
    REFERENCES `shoppingcart`.`orders`
(
    `id`
),
    CONSTRAINT `orders_products_ibfk_2`
    FOREIGN KEY
(
    `product_id`
)
    REFERENCES `shoppingcart`.`products`
(
    `id`
)
    )
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;


SET
SQL_MODE = @OLD_SQL_MODE;
SET
FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET
UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
