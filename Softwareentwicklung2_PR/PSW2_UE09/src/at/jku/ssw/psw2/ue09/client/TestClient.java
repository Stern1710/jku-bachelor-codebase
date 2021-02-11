package at.jku.ssw.psw2.ue09.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public final class TestClient {
    
    private static final String BASE_URL = "http://localhost:8080/PSW2_UE09_war_exploded/test/TestServlet";
    
    public static void main(String[] args) {
        final Client client = ClientBuilder.newClient();
        final WebTarget baseTarget = client.target(BASE_URL);
        
        final Response response = baseTarget.path("/hello").request().get();
        
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.printf("Server said: \"%s\"%n", response.readEntity(String.class));
        } else {
            System.out.printf("Received status code %d (\"%s\")%n", response.getStatus(), Response.Status.fromStatusCode(response.getStatus()).toString());
        }
    }
}
