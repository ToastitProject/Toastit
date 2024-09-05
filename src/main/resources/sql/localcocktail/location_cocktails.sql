CREATE TABLE location_cocktails (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    si VARCHAR(100),           -- 시 정보
                                    deo VARCHAR(100),          -- 도 정보
                                    ingredient1 VARCHAR(255),  -- 첫 번째 재료
                                    ingredient2 VARCHAR(255),  -- 두 번째 재료
                                    ingredient3 VARCHAR(255),  -- 세 번째 재료
                                    ingredient4 VARCHAR(255)   -- 네 번째 재료
);