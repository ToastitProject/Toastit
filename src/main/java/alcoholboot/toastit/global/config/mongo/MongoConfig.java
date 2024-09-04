package alcoholboot.toastit.global.config.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

/**
 * MongoDB와 관련된 설정을 담당하는 구성 클래스.
 * MongoClient와 MongoDatabaseFactory를 빈으로 등록합니다.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Value("${spring.data.mongodb.database}")
    private String database;

    /**
     * MongoClient 빈을 생성합니다.
     * 지정된 MongoDB URI를 사용하여 MongoDB 클라이언트를 생성합니다.
     *
     * @return MongoClient 객체
     */
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(uri);
    }

    /**
     * MongoDatabaseFactory 빈을 생성합니다.
     * MongoClient와 데이터베이스 이름을 사용하여 MongoDatabaseFactory를 설정합니다.
     *
     * @param mongoClient MongoClient 객체
     * @return MongoDatabaseFactory 객체
     */
    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, database);
    }
}