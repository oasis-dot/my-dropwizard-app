package com.example.myapp;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class oasisApplication extends Application<oasisConfiguration> {

    public static void main(final String[] args) throws Exception {
        new oasisApplication().run(args);
    }

    @Override
    public String getName() {
        return "oasis";
    }

    @Override
    public void initialize(final Bootstrap<oasisConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final oasisConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
