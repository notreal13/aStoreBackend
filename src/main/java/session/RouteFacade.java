package session;

import entity.Route;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author Notreal
 */
@Stateless
public class RouteFacade extends AbstractFacade<Route> {
    
    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public RouteFacade() {
        super(Route.class);
    }
    
}
