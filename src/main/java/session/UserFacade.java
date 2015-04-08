package session;

import auth.Salt;
import entity.Role;
import entity.User;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author Notreal
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {
    
    @Inject
    private Logger log;    

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }

    public User findByUsername(String username) {
        List<User> users = em.createNamedQuery("User.findByUsername", User.class)
                .setParameter("username", username)
                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    public User findByUsernameAndPassword(String username, String password) {
        List<User> users = em.createNamedQuery("User.findByUsernameAndPassword", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    public User findByToken(String authToken) {
        List<User> users = em.createNamedQuery("User.findByToken", User.class)
                .setParameter("token", authToken)
                .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    public Salt createUser(String firstName, String lastName, String username, String phone) {
        try {
            User user = findByUsername(username);
            if (user == null) {
                user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setUsername(username);
                user.setPhone(phone);
                Salt salt = new Salt();
                user.setSalt(salt.toString());
                Collection<Role> roles = em.createNamedQuery("Role.findByName", Role.class)
                        .setParameter("name", "user")
                        .getResultList();
                user.setRoleCollection(roles);
                user.setToken(UUID.randomUUID().toString());
                em.persist(user);
                em.flush();
                return salt;
            } else {
                return null;
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean setUserPassword(String username, String password) {
        boolean result = false;
        try {
            User user = findByUsername(username);
            if (user != null) {
                if (user.getPassword() != null) {
                    return result;
                }
                user.setPassword(password);
                em.merge(user);
                result = true;
            }

        } catch (Exception ex) {
            log.log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public Salt getSalt(String username) {
        User user = findByUsername(username);
        Salt salt;
        if (user != null) {
            salt = new Salt(user.getSalt());
        } else {
            salt = new Salt();
            salt.setConstFakeSalt(username);
        }
        return salt;
    }
    
    public String getToken(String username) {
        User user = findByUsername(username);
        if (!user.isActive()) {
            return "";
        } else {
            return user.getToken();
        }
    }

}
