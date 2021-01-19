package com.binarybazaar.privateagent.main.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({
    "file:privateagent.configuration"
})
public interface MainConfiguration extends Config {

    @Key("server.http.port")
    @DefaultValue("8383")
    int port();

    @Key("server.http.logging")
    @DefaultValue("true")
    boolean httpLogging();

    @Key("webapp.basepath")
    @DefaultValue("/")
    String basePath();

}
