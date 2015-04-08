package resource;

import entity.SupportingDocument;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.SupportingDocumentFacade;
import utils.AStoreException;

/**
 *
 * @author Notreal
 */
@Path("document")
public class SupportingDocumentResource {
    
    @Inject 
    SupportingDocumentFacade supportingDocumentFacade;

    public SupportingDocumentResource() {
    }
    
    @POST
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(SupportingDocument entity) {
        supportingDocumentFacade.create(entity);
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("id", entity.getId())
                .build();
        return Response.status(Response.Status.CREATED).entity(jsonObject).build();
    }

    @PUT
    @RolesAllowed("admin")
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void edit(@PathParam("id") Integer id, SupportingDocument entity) {
        supportingDocumentFacade.edit(entity);
    }

    @DELETE
    @RolesAllowed("admin")
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) throws AStoreException {
        SupportingDocument supportingDocument = supportingDocumentFacade.find(id);
        if (supportingDocument == null) {
            throw new AStoreException("Delete: SupportingDocument with " + id + " not found");
        }

        try {
            supportingDocumentFacade.remove(supportingDocument);
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
    public SupportingDocument find(@PathParam("id") Integer id) {
        return supportingDocumentFacade.find(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SupportingDocument> findAll() {
        return supportingDocumentFacade.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SupportingDocument> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return supportingDocumentFacade.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(supportingDocumentFacade.count());
    }
}

