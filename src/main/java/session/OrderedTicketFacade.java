package session;

import entity.OrderedTicket;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author Notreal
 */
@Stateless
public class OrderedTicketFacade extends AbstractFacade<OrderedTicket> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrderedTicketFacade() {
        super(OrderedTicket.class);
    }
   
}
