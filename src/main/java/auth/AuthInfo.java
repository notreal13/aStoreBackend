package auth;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Notreal
 */
@XmlRootElement
public class AuthInfo implements Serializable {
    
    private static final long serialVersionUID = -3518050979917025503L;
    
    private String username;
    private String password;

    public AuthInfo() {
    }
    
    public AuthInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
