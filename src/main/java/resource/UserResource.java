package resource;

import auth.AuthAccessElement;
import auth.AuthInfo;
import auth.Salt;
import entity.User;
import java.net.URI;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import session.AuthService;
import session.UserFacade;


/**
 *
 * @author Notreal
 */
@Path("user")
public class UserResource {

    @Inject
    AuthService authService;

    @Inject
    UserFacade userFacade;

    @Context
    UriInfo uriInfo;

    public UserResource() {
    }

    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signup(User user) {
        Salt salt = userFacade.createUser(user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getPhone());

        if (salt != null) {
            return Response.ok(salt).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("email already exist").build();
        }
    }

    @POST
    @Path("password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPassword(AuthInfo authInfo) {
        if (userFacade.setUserPassword(authInfo.getUsername(), authInfo.getPassword())) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("add password fail").build();
        }
    }

    @GET
    @Path("salt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalt(@QueryParam("username") String username) {
        Salt salt = userFacade.getSalt(username);
        return Response.ok(salt).build();
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpServletRequest request, AuthInfo authInfo) {
        AuthAccessElement authAccessElement = authService.login(authInfo);
        if (authAccessElement != null) {
            request.getSession().setAttribute(AuthAccessElement.PARAM_AUTH_TOKEN, authAccessElement.getToken());
        } else {
            if (authService.isWaitForActivated(authInfo)) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(authAccessElement).build();
    }

    @GET
    @Path("info")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserInfo(@Context HttpServletRequest request) {
        String token = request.getHeader(AuthAccessElement.PARAM_AUTH_TOKEN);
        User user = userFacade.findByToken(token);
        return user;
    }

    @GET
    @Path("activation/{link}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setActivation(@PathParam("link") String link) {
        User user = userFacade.findByToken(link);
        URI uri = uriInfo.getBaseUri();
        if ((user != null) && (!user.isActive())) {
            user.setActive(true);
            userFacade.edit(user);
            uri = URI.create(uri + "test/u");
            return Response.temporaryRedirect(uri).build();
        } else {
            uri = URI.create(uri + "test/bad");
            return Response.seeOther(uri).build();
        }
    }
}
