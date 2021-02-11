package at.jku.ssw.psw2.ue09.client;

import at.jku.ssw.psw2.ue09.model.InventoryException;
import at.jku.ssw.psw2.ue09.model.InventoryItem;
import at.jku.ssw.psw2.ue09.model.InventoryModel;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.rmi.RemoteException;
import java.util.List;

public final class ClientModel implements InventoryModel {

    private static final String BASE_URL = "http://localhost:8080/PSW2_UE09_war_exploded/rest/";

    private Client client;
    private WebTarget baseTarget;

    public ClientModel() {
    }

    @Override
    public void open() {
        client = ClientBuilder.newClient();
        baseTarget = client.target(BASE_URL);
    }

    @Override
    public List<InventoryItem> getItems() throws InventoryException {
        Response r = baseTarget.path("/inventory/all")
                .request()
                .get();
        if (r.getStatus() != Response.Status.OK.getStatusCode()) {
            analyzeResponseError(r);
        }
        return r.readEntity(new GenericType<List<InventoryItem>>() {});
    }

    @Override
    public int createItem(String name) throws InventoryException {
        if (name == null || name.equalsIgnoreCase("")) {
            throw new InventoryException("Name was null or empty");
        }

        Response r = baseTarget.path("/inventory/new")
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.text(name));
        if (r.getStatus() != Response.Status.OK.getStatusCode()) {
            analyzeResponseError(r);
        }
        return r.readEntity(Integer.class);
    }

    @Override
    public InventoryItem getItem(int itemId) throws InventoryException {
        Response r = baseTarget.path("/inventory/"+itemId)
                .request(MediaType.APPLICATION_XML)
                .get();
        if (r.getStatus() != Response.Status.OK.getStatusCode()) {
            analyzeResponseError(r); //always throws exception
        }
        return r.readEntity(InventoryItem.class);
    }

    @Override
    public void setDescription(int itemId, String description) throws InventoryException {
        if (description == null) {
            throw new InventoryException("Description was null or empty");
        }

        Response r = baseTarget.path("/inventory/"+itemId+"/description")
                .request()
                .post(Entity.text(description));
        if (r.getStatus() != Response.Status.OK.getStatusCode()) {
           analyzeResponseError(r);
        }
    }

    @Override
    public void changeQuantity(int itemId, int difference) throws InventoryException {
        Response r = baseTarget.path("/inventory/"+itemId+"/quantity")
                .request()
                .post(Entity.text(difference));
        if (r.getStatus() != Response.Status.OK.getStatusCode()) {
           analyzeResponseError(r);
        }
    }

    @Override
    public void deleteItem(int itemId) throws InventoryException {
        Response r = baseTarget.path("/inventory/"+itemId+"/delete")
                .request()
                .delete();
        if (r.getStatus() != Response.Status.OK.getStatusCode()) {
            analyzeResponseError(r);
        }
    }

    @Override
    public void close() {
        baseTarget = null;
        client.close();
        client = null;
    }

    private boolean analyzeResponseError(Response r) throws InventoryException {
        int status = r.getStatus();
        //String statusDescription = Response.Status.fromStatusCode(status).toString();
        String statusDescription = r.readEntity(String.class);
        throw new InventoryException("Status code:" + status + " (" + statusDescription + ")");
    }
}
