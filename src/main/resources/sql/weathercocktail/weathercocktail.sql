CREATE TABLE weather_response (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  base_date VARCHAR(50),     -- 날짜
                                  base_time VARCHAR(50),     -- 시간
                                  category VARCHAR(100),     -- 카테고리
                                  nx VARCHAR(50),            -- x좌표
                                  ny VARCHAR(50),            -- y좌표
                                  obsr_value VARCHAR(255)    -- 관측 값
);

CREATE TABLE weather_cocktails (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   weather VARCHAR(100) NOT NULL,    -- 날씨 상태 (추움, 시원함, 따뜻함, 더움 등)
                                   ingredient1 VARCHAR(255),
                                   ingredient2 VARCHAR(255),
                                   ingredient3 VARCHAR(255),
                                   ingredient4 VARCHAR(255),
                                   ingredient5 VARCHAR(255),
                                   ingredient6 VARCHAR(255),
                                   ingredient7 VARCHAR(255),
                                   ingredient8 VARCHAR(255),
                                   ingredient9 VARCHAR(255)
);

CREATE TABLE weather_area (
                              areacode VARCHAR(255) PRIMARY KEY,   -- 행정구역 코드
                              step1 VARCHAR(255),                  -- 시/도
                              step2 VARCHAR(255),                  -- 시/군/구
                              step3 VARCHAR(255),                  -- 읍/면/동
                              gridx VARCHAR(255),                  -- x좌표
                              gridy VARCHAR(255),                  -- y좌표
                              longitudeHour VARCHAR(50) NOT NULL,  -- 경도 시
                              longitudeMin VARCHAR(50) NOT NULL,   -- 경도 분
                              longitudeSec VARCHAR(50) NOT NULL,   -- 경도 초
                              latitudeHour VARCHAR(50) NOT NULL,   -- 위도 시
                              latitudeMin VARCHAR(50) NOT NULL,    -- 위도 분
                              latitudeSec VARCHAR(50) NOT NULL,    -- 위도 초
                              longitudeMs VARCHAR(50) NOT NULL,    -- 경도 ms
                              latitudeMs VARCHAR(50) NOT NULL      -- 위도 ms
);