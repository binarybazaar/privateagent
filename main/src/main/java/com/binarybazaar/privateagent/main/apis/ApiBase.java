package com.binarybazaar.privateagent.main.apis;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public abstract class ApiBase {

    public static final String API_PATH_V1 = "v1/";

    @Context
    private SecurityContext securityContext;

    public ApiBase() {
    }

    protected SecurityContext getSecurityContext() {
        return securityContext;
    }

}
