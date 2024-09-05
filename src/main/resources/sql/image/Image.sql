CREATE TABLE images (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        image_name VARCHAR(255) NOT NULL,
                        image_path VARCHAR(255) NOT NULL,
                        image_type VARCHAR(50),
                        image_size VARCHAR(50),
                        image_use VARCHAR(100),
                        user_id BIGINT,
                        cocktail_id BIGINT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
                        CONSTRAINT fk_cocktail FOREIGN KEY (cocktail_id) REFERENCES craft_cocktails(id) ON DELETE SET NULL
);