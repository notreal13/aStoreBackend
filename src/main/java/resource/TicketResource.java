package resource;

import entity.Ticket;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.CategoryFacade;
import session.TicketFacade;
import utils.AStoreException;

/**
 * REST Web Service
 *
 * @author Notreal
 */
@Path("ticket")
public class TicketResource {

    @Inject
    TicketFacade ticketFacade;

    @Inject
    CategoryFacade categoryFacade;

    public TicketResource() {
    }

    @POST
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Ticket entity) {
        ticketFacade.create(entity);
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("id", entity.getId())
                .build();
        return Response.status(Response.Status.CREATED).entity(jsonObject).build();
    }

    @PUT
    @RolesAllowed("admin")
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void edit(@PathParam("id") Integer id, Ticket entity) {
        ticketFacade.edit(entity);
    }

    @DELETE
    @RolesAllowed("admin")
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) throws AStoreException {
        Ticket ticket = ticketFacade.find(id);
        if (ticket == null) {
            throw new AStoreException("Delete: Ticket with " + id + " not found");
        }

        try {
            ticketFacade.remove(ticket);
        } catch (Exception e) {
            Throwable t = e.getCause();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            String cause = sw.toString();
            
            throw new AStoreException("Delete failure. " + cause, e);
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Ticket find(@PathParam("id") Integer id) {
        return ticketFacade.find(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> findAll() {
        return ticketFacade.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Ticket> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return ticketFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(ticketFacade.count());
    }

    @GET
    @Path("category/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Ticket> findByCategory(@PathParam("id") Integer categoryId) {
        return ticketFacade.findTicketByCategory(categoryId);
    }

}
