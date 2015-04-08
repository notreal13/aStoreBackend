package session;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author Notreal
 */
@Singleton
public class ConfirmationNumberService {

    @Inject
    private EntityManager em;
    
    private int counter;

    @PostConstruct
    void init() {
        Integer maxConfirmationNumber = (Integer) em.createNamedQuery("CustomerOrder.findMaxConfirmationNumber")
                .getSingleResult();
        if (maxConfirmationNumber == null) {
            maxConfirmationNumber = 0;
        }
        counter = maxConfirmationNumber;
    }
    
    public int get() {
        return ++counter;
    }

}
