package resource;

import auth.AuthAccessElement;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import session.AuthService;

/**
 *
 * @author Notreal
 */
@Provider
public class AuthSecurityInterceptor implements ContainerRequestFilter {

    @Inject
    AuthService authService;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authToken = requestContext.getHeaderString(AuthAccessElement.PARAM_AUTH_TOKEN);

        Method methodInvoked = resourceInfo.getResourceMethod();

        if (methodInvoked.isAnnotationPresent(DenyAll.class)) {
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("User cannot access the resource.")
                    .build());
        }

        if (methodInvoked.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAllowedAnnotation = methodInvoked.getAnnotation(RolesAllowed.class);
            Set<String> rolesAllowed = new HashSet<>(Arrays.asList(rolesAllowedAnnotation.value()));
            boolean authorized = authService.isAuthorized(authToken, rolesAllowed);
            if (!authorized) {
                requestContext.abortWith(Response
                        .status(Response.Status.UNAUTHORIZED)
                        .entity("User cannot access the resource.")
                        .build());
            }
        }
    }
}
