package filter;

import auth.AuthAccessElement;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Notreal
 */
@Provider
@PreMatching
public class NewCrossOriginResourceSharingFilter implements ContainerResponseFilter {
    
    private final static Logger log = Logger.getLogger(NewCrossOriginResourceSharingFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) {
        log.info("Executing CORS filter");
        
        response.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        response.getHeaders().putSingle("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
        String headers = "Content-Type";
        headers = headers + ", " + AuthAccessElement.PARAM_AUTH_TOKEN;
        response.getHeaders().putSingle("Access-Control-Allow-Headers", headers);
    }

}
