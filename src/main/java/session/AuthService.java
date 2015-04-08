package session;

import auth.AuthAccessElement;
import auth.AuthInfo;
import entity.Role;
import entity.User;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Notreal
 */
@Stateless
public class AuthService {

    @Inject
    private UserFacade userFacade;

    public AuthAccessElement login(AuthInfo authInfo) {
        User user = userFacade.findByUsernameAndPassword(authInfo.getUsername(), authInfo.getPassword());
        if (user != null) {
            if (!user.isActive()) {
                return null;
            }
            user.setToken(UUID.randomUUID().toString());
            userFacade.edit(user);

            Set<String> authPermissionSet = new HashSet<>();
            Collection<Role> roles = user.getRoleCollection();
            for (Role role : roles) {
                authPermissionSet.add(role.getName());
            }
            return new AuthAccessElement(user.getToken(), authPermissionSet);
        }
        return null;
    }

    public boolean isWaitForActivated(AuthInfo authInfo) {
        User user = userFacade.findByUsernameAndPassword(authInfo.getUsername(), authInfo.getPassword());
        if (user != null) {
            return !user.isActive();
        } else {
            return false;
        }
    }

    public boolean isAuthorized(String authToken, Set<String> rolesAllowed) {
        User user = userFacade.findByToken(authToken);
        if (user != null) {
            for (Role role : user.getRoleCollection()) {
                if (rolesAllowed.contains(role.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
