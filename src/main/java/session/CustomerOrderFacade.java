package session;

import entity.CustomerOrder;
import java.util.Collection;
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
public class CustomerOrderFacade extends AbstractFacade<CustomerOrder> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerOrderFacade() {
        super(CustomerOrder.class);
    }
    
    public List<CustomerOrder> findCustomerOrderByUser(int userId) {
        Query namedQuery = getEntityManager().createNamedQuery("CustomerOrder.findByUserId");
        namedQuery.setParameter("userId", userId);
        List<CustomerOrder> customerOrders = namedQuery.getResultList();
        return customerOrders;        
    }
    
    public Collection<CustomerOrder> findAll1() {
        return em.createNamedQuery("Order.findAll1").getResultList();
    }
    
}
