package auth;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Notreal
 */
@XmlRootElement
public class Salt implements Serializable {
    
    private static final long serialVersionUID = 2044997916304712447L;
    
    private String salt;
    
    public Salt() {
        this.salt = UUID.randomUUID().toString();
    }

    public Salt(String uuid) {
        this.salt = uuid;
    }
    
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    public void setConstFakeSalt(String email) {
        byte[] bytes = email.getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            for(int i = 0; i < 1031; i++)
                md.update(bytes);
            setSalt(UUID.nameUUIDFromBytes(md.digest()).toString());
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Salt.class.getName()).log(Level.SEVERE, null, ex);
            setSalt(UUID.nameUUIDFromBytes(bytes).toString());
        }
    }

    @Override
    public String toString() {
        return salt;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.salt);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Salt other = (Salt) obj;
        return Objects.equals(this.salt, other.salt);
    }

}
