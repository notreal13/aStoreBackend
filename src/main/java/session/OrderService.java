package session;

import entity.CustomerOrder;
import entity.OrderedTicket;
import entity.Route;
import entity.SupportingDocument;
import entity.Ticket;
import entity.User;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import utils.AStoreException;

/**
 *
 * @author Notreal
 */
@Stateless
public class OrderService {

    @Inject
    private EntityManager em;

    @Resource
    private EJBContext context;

    @Inject
    RouteFacade routeFacade;

    @Inject
    UserFacade userFacade;

    @Inject
    CustomerOrderFacade orderFacade;

    @Inject
    OrderedTicketFacade orderedTicketFacade;

    @Inject
    TicketFacade ticketFacade;

    @Inject
    SupportingDocumentFacade supportingDocumentFacade;

    @Inject
    ConfirmationNumberService confirmationNumberService;

    public int placeOrder(CustomerOrder customerOrder, User user) throws AStoreException {
        try {
            List<OrderedTicket> orderedTickets = customerOrder.getOrderedTicketList();
            if (orderedTickets.isEmpty()) {
                throw new AStoreException("Purchase have no ticket.");
            }
            
            Route route = routeFacade.find(customerOrder.getRoute().getId());
            if (user == null) {
                user = userFacade.find(0);
            }
            CustomerOrder newCustomerOrder = new CustomerOrder();
            newCustomerOrder.setFirstName(customerOrder.getFirstName());
            newCustomerOrder.setLastName(customerOrder.getLastName());
            newCustomerOrder.setEmail(customerOrder.getEmail());
            newCustomerOrder.setPhone(customerOrder.getPhone());

            newCustomerOrder.setUser(user);
            newCustomerOrder.setRoute(route);
            newCustomerOrder.setAmount(getTolal(orderedTickets));

            int confirmationNumber = confirmationNumberService.get();
            newCustomerOrder.setConfirmationNumber(confirmationNumber);

            newCustomerOrder.setOrderedTicketList(Collections.EMPTY_LIST);

            em.persist(newCustomerOrder);
            addOrderedItems(newCustomerOrder, orderedTickets);
            
            return newCustomerOrder.getConfirmationNumber();

        } catch (AStoreException | ParseException e) {
            context.setRollbackOnly();
            throw new AStoreException("Purchase failure. " + e.getMessage(), e);
        }
    }

    private BigDecimal getTolal(List<OrderedTicket> orderedTickets) {
        BigDecimal total = new BigDecimal(0);
        for (OrderedTicket orderedTicket : orderedTickets) {
            total = total.add(orderedTicket.getTicket().getPrice());
        }
        return total;
    }

    private void addOrderedItems(CustomerOrder customerOrder, List<OrderedTicket> orderedTickets) throws ParseException, AStoreException {

        for (OrderedTicket orderedTicket : orderedTickets) {
            OrderedTicket newOrderedTicket = new OrderedTicket();
            Ticket ticket = ticketFacade.find(orderedTicket.getTicket().getId());
            SupportingDocument supportingDocument = supportingDocumentFacade.find(orderedTicket.getSupportingDocument().getId());

            newOrderedTicket.setFirstName(orderedTicket.getFirstName());
            newOrderedTicket.setLastName(orderedTicket.getLastName());
            newOrderedTicket.setMiddleName(orderedTicket.getMiddleName());
            // Date must be parsed from dobString
            if (orderedTicket.getDobString() != null) {
                DateFormat format = new SimpleDateFormat("yyyy.MM.dd");
                Date date = format.parse(orderedTicket.getDobString());
                newOrderedTicket.setDob(date);
            } 
            newOrderedTicket.setTicket(ticket);
            newOrderedTicket.setSupportingDocument(supportingDocument);
            newOrderedTicket.setSupportingDocumentData(orderedTicket.getSupportingDocumentData());
            newOrderedTicket.setLicensePlate(orderedTicket.getLicensePlate());
            newOrderedTicket.setCustomerOrder(customerOrder);

            em.persist(newOrderedTicket);
        }
    }

}
