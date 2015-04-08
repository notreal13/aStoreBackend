package resource;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import utils.AStoreException;

/**
 *
 * @author Notreal
 */
@Provider
public class AStoreExceptionHandler implements ExceptionMapper<AStoreException> {

    @Override
    public Response toResponse(AStoreException e) {
        return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
    
}
