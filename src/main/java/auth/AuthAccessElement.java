package auth;

import java.io.Serializable;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Notreal
 */
@XmlRootElement
public class AuthAccessElement implements Serializable {
    
    private static final long serialVersionUID = -2425372121964189536L;
    
    public static final String PARAM_AUTH_TOKEN = "aStore-auth-token";
 
    private String token;
    private Set<String> permissionsSet;
 
    public AuthAccessElement() {
    }

    public AuthAccessElement(String token, Set<String> permissionsSet) {
        this.token = token;
        this.permissionsSet = permissionsSet;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getPermissionsSet() {
        return permissionsSet;
    }

    public void setPermissionsSet(Set<String> permissionsSet) {
        this.permissionsSet = permissionsSet;
    }
}
