package com.binarybazaar.privateagent.main.apis.v1;

import com.binarybazaar.privateagent.main.apis.ApiBase;
import static com.binarybazaar.privateagent.main.apis.ApiBase.API_PATH_V1;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path(API_PATH_V1 + "test")
public class TestApi extends ApiBase {

    @Context
    SecurityContext securityContext;

    @GET
    @Path("ping")
    @Produces(MediaType.APPLICATION_JSON)
    public Response ping() {
        return Response.ok("hej").build();
    }

}
