CREATE TABLE craft_cocktails (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 name VARCHAR(255) NOT NULL,
                                 description TEXT NOT NULL,
                                 recipe TEXT NOT NULL,
                                 user_id BIGINT NOT NULL,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE ingredients (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             amount VARCHAR(100) NOT NULL,
                             unit VARCHAR(50) NOT NULL,
                             cocktail_id BIGINT,
                             CONSTRAINT fk_cocktail FOREIGN KEY (cocktail_id) REFERENCES craft_cocktails(id) ON DELETE SET NULL
);