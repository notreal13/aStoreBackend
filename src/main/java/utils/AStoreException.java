package utils;

import java.io.Serializable;

/**
 *
 * @author Notreal
 */
public class AStoreException extends Exception implements Serializable {
    private static final long serialVersionUID = 8419195702253024221L;    

    public AStoreException() {
        super();
    }

    public AStoreException(String string) {
        super(string);
    }

    public AStoreException(String string, Exception e) {
        super(string, e);
    }
    
}
