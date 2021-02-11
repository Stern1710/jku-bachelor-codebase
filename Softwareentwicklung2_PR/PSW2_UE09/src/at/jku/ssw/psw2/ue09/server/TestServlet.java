package at.jku.ssw.psw2.ue09.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/TestServlet")
public final class TestServlet {

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from thread " + Thread.currentThread().getId();
    }
}
