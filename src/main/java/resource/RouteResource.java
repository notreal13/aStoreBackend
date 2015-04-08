package resource;

import entity.Route;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import session.RouteFacade;

/**
 * REST Web Service
 *
 * @author Notreal
 */
@Path("route")
public class RouteResource {
    
    @Inject
    RouteFacade routeFacade;

    public RouteResource() {
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Route> findAll() {
        return routeFacade.findAll();
    }
}
