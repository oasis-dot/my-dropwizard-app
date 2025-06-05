package com.example.myapp;

import com.example.myapp.core.User;
import com.example.myapp.db.UserDAO;
import com.example.myapp.resources.UserResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MyApplication extends Application<MyApplicationConfiguration> {

    private final HibernateBundle<MyApplicationConfiguration> hibernateBundle =
        new HibernateBundle<MyApplicationConfiguration>(User.class) {
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
        bootstrap.addBundle(hibernateBundle); 

    }

    @Override
    public void run(MyApplicationConfiguration configuration, Environment environment) {
        final UserDAO userDAO = new UserDAO(hibernateBundle.getSessionFactory());

        final UserResource userResource = new UserResource(userDAO);
        environment.jersey().register(userResource);
    }
}
