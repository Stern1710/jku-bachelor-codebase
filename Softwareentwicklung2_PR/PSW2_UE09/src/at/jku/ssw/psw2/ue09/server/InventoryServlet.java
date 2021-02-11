package at.jku.ssw.psw2.ue09.server;

import at.jku.ssw.psw2.ue09.model.InventoryException;
import at.jku.ssw.psw2.ue09.model.InventoryItem;
import at.jku.ssw.psw2.ue09.model.InventoryModel;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

@Path("/inventory")
public final class InventoryServlet {

    private static final InventoryModel dataStore;

    static {
        dataStore = new ServerModel();
        try {
            dataStore.open();
        } catch (InventoryException e) {
            throw new RuntimeException("Failed to initialize inventory server", e);
        }
    }

    @GET
    @Path("/all")
    public Response getAllItems() throws InventoryException {
        List<InventoryItem> items = dataStore.getItems();
        GenericEntity<List<InventoryItem>> entity = new GenericEntity<List<InventoryItem>>(new ArrayList<>(items)){};
        return Response.ok().entity(entity).build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/new")
    public int addItem(String name) throws InventoryException {
        return dataStore.createItem(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("/{itemId}")
    public InventoryItem getItemByName(@PathParam("itemId") int id) throws InventoryException {
        return dataStore.getItem(id);
    }

    @POST
    @Path("/{itemId}/description")
    public Response updateDescription(@PathParam("itemId") int id, String descr) throws InventoryException {
        dataStore.setDescription(id, descr);
        return Response.ok().build();
    }

    @POST
    @Path("/{itemId}/quantity")
    public Response updateQuantity(@PathParam("itemId") int id, int quantity) throws InventoryException {
        dataStore.changeQuantity(id, quantity);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{itemId}/delete")
    public Response deleteItem(@PathParam("itemId") int id) throws InventoryException {
        dataStore.deleteItem(id);
        return Response.ok().build();
    }

    @Provider
    public static class InventoryExceptionMapper implements ExceptionMapper<InventoryException> {

        @Override
        public Response toResponse(InventoryException exception) {
            if (exception instanceof InventoryException.NoSuchItem) {
                return Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage()).build();
            }
            else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Inventory error: " + exception.getMessage()).build();
            }
        }
    }
}
