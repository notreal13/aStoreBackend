package resource;

import auth.AuthAccessElement;
import entity.CustomerOrder;
import entity.User;
import java.text.ParseException;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.OrderService;
import session.UserFacade;
import utils.AStoreException;

/**
 * REST Web Service
 *
 * @author Notreal
 */
@Path("purchase")
public class PurchaseResource {

    @Inject
    private OrderService orderService;

    @Inject
    private UserFacade userFacade;

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response placeOrder(@Context HttpServletRequest request, CustomerOrder customerOrder) throws AStoreException, ParseException {
        String authToken = request.getHeader(AuthAccessElement.PARAM_AUTH_TOKEN);
        User user = userFacade.findByToken(authToken);

        int confirmationNumber = orderService.placeOrder(customerOrder, user);

        JsonObject jsonObject = Json.createObjectBuilder()
                .add("orderId", confirmationNumber)
                .build();
        return Response.status(Response.Status.CREATED).entity(jsonObject).build();
    }
}
