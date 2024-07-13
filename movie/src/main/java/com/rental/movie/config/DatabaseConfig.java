package com.rental.movie.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.rental.movie.util.ZonedDateTimeReadConverter;
import com.rental.movie.util.ZonedDateTimeWriteConverter;

@Configuration
public class DatabaseConfig extends AbstractMongoClientConfiguration {

    @Autowired
    private AppConfig appConfig;

    @Override
    protected String getDatabaseName() {
        return appConfig.getDatabase();
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(appConfig.getUri());
    }

    @Override
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new ZonedDateTimeReadConverter());
        converters.add(new ZonedDateTimeWriteConverter());
        return new MongoCustomConversions(converters);
    }
}
