CREATE TABLE trend_cocktail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    start_date VARCHAR(50),
    end_date VARCHAR(50),
    keyword VARCHAR(255),
    search_volume_two_month_ago DOUBLE,
    search_volume_one_month_ago DOUBLE
);