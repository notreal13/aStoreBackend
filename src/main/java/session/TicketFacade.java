package session;

import entity.Ticket;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Notreal
 */
@Stateless
public class TicketFacade extends AbstractFacade<Ticket> {
    
    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TicketFacade() {
        super(Ticket.class);
    }
    
    public List<Ticket> findTicketByCategory(int categoryId) {
        Query namedQuery = getEntityManager().createNamedQuery("Ticket.findByCategoryId");
        namedQuery.setParameter("categoryId", categoryId);
        List<Ticket> tickets = namedQuery.getResultList();
        return tickets;
    }
            
    
}
