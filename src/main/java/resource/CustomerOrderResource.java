package resource;

import auth.AuthAccessElement;
import entity.CustomerOrder;
import entity.User;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import session.CustomerOrderFacade;
import session.UserFacade;

/**
 *
 * @author Notreal
 */
@Path("order")
public class CustomerOrderResource {

    @Inject
    CustomerOrderFacade customerOrderFacade;

    @Inject
    UserFacade userFacade;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<CustomerOrder> findAll(@HeaderParam(AuthAccessElement.PARAM_AUTH_TOKEN) String authToken) {
        User user = userFacade.findByToken(authToken);
        if (user != null) {
            return customerOrderFacade.findCustomerOrderByUser(user.getId());
        } else {
            return Collections.emptyList();
        }
    }

    @GET
    @Path("xdrlkjhgksdtjhkjkthlkhkrthprtfhrturtyuhjrtujrtudrhywrtuwr6ju")
    @Produces({MediaType.APPLICATION_JSON})
    public Collection<CustomerOrder> findByRoute() {
        return customerOrderFacade.findAll1();
    }
    
}
