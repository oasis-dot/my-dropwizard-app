package com.example.myapp;

import com.example.myapp.core.User;
import com.example.myapp.db.UserDAO;
import com.example.myapp.resources.UserResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import com.fasterxml.jackson.databind.SerializationFeature; // Для налаштування Jackson

public class MyApplication extends Application<MyApplicationConfiguration> {

    // Ініціалізація HibernateBundle
    // Передайте сюди всі ваші @Entity класи
    private final HibernateBundle<MyApplicationConfiguration> hibernateBundle =
        new HibernateBundle<MyApplicationConfiguration>(User.class /*, OtherEntity.class, ... */) {
            @Override
            public DataSourceFactory getDataSourceFactory(MyApplicationConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        };

    public static void main(String[] args) throws Exception {
        new MyApplication().run(args);
    }

    @Override
    public String getName() {
        return "my-dropwizard-mysql-app";
    }

    @Override
    public void initialize(Bootstrap<MyApplicationConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle); // Додаємо HibernateBundle

        // Приклад налаштування ObjectMapper для Jackson (необов'язково)
        // bootstrap.getObjectMapper().enable(SerializationFeature.INDENT_OUTPUT); // "Pretty print" JSON
        // bootstrap.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // bootstrap.getObjectMapper().registerModule(new JavaTimeModule()); // Для Java 8 Date/Time API
    }

    @Override
    public void run(MyApplicationConfiguration configuration, Environment environment) {
        // Створюємо DAO
        final UserDAO userDAO = new UserDAO(hibernateBundle.getSessionFactory());

        // Реєструємо ресурс
        final UserResource userResource = new UserResource(userDAO);
        environment.jersey().register(userResource);

        // (Опціонально) Додайте Health Checks
        // final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
        // environment.healthChecks().register("template", healthCheck);

        // (Опціонально) Глобальні налаштування Jackson тут, якщо не зроблено в initialize
        // environment.getObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }
}
