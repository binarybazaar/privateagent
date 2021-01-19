package com.binarybazaar.privateagent.main.configuration;

import org.aeonbits.owner.ConfigFactory;

public class Config {

    private static final MainConfiguration MAIN_CONFIG = ConfigFactory.create(MainConfiguration.class);

    public static MainConfiguration get() {
        return MAIN_CONFIG;

    }
}
