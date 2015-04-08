package entity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Notreal
 */
public class CustomerOrderTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private EntityTransaction tx;

    private static CustomerOrder testOrder;

    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("testPU");
        em = emf.createEntityManager();

        em.getTransaction().begin();
        testOrder = createCustomerOrder();
        
        Route route = RouteTest.createRoute();
        testOrder.setRoute(route);
        em.persist(route);
        
        em.persist(testOrder);
        em.getTransaction().commit();
    }

    @AfterClass
    public static void tearDownClass() {
        em.close();
        emf.close();
    }

    @Before
    public void setUp() {
        tx = em.getTransaction();
        tx.begin();
    }

    @After
    public void tearDown() {
        tx.commit();
    }

    public static CustomerOrder createCustomerOrder() {
        CustomerOrder order = new CustomerOrder();
        order.setAmount(new BigDecimal(777));
        order.setConfirmationNumber(777);
        order.setFirstName("fName");
        order.setLastName("lName");
        order.setEmail("email@email");
        order.setPhone("111222333");
        return order;
    }

    @Test
    public void testFindAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(CustomerOrder.class));
        int count = em.createQuery(cq).getResultList().size();

        List<CustomerOrder> orders = em.createNamedQuery("CustomerOrder.findAll").getResultList();
        Assert.assertEquals(count, orders.size());
    }

    @Test
    public void testFindById() {
        List<CustomerOrder> orders = em.createNamedQuery("CustomerOrder.findById")
                .setParameter("id", testOrder.getId())
                .getResultList();
        Assert.assertEquals(1, orders.size());
    }

    @Test
    public void testFindByAmount() {
        List<CustomerOrder> orders = em.createNamedQuery("CustomerOrder.findByAmount")
                .setParameter("amount", testOrder.getAmount())
                .getResultList();
        Assert.assertTrue(orders.size() > 0);
    }

    @Test
    public void testFindByDateCreated() {
        em.refresh(testOrder);

        List<CustomerOrder> orders = em.createNamedQuery("CustomerOrder.findByDateCreated")
                .setParameter("dateCreated", testOrder.getDateCreated())
                .getResultList();
        Assert.assertEquals(1, orders.size());
    }

    @Test
    public void testFindByConfirmationNumber() {
        List<CustomerOrder> orders = em.createNamedQuery("CustomerOrder.findByConfirmationNumber")
                .setParameter("confirmationNumber", testOrder.getConfirmationNumber())
                .getResultList();
        Assert.assertTrue(orders.size() > 0);
    }

    @Test
    public void testFindByFirstName() {
        List<CustomerOrder> orders = em.createNamedQuery("CustomerOrder.findByFirstName")
                .setParameter("firstName", testOrder.getFirstName())
                .getResultList();
        Assert.assertTrue(orders.size() > 0);
    }

    @Test
    public void testFindByLastName() {
        List<CustomerOrder> orders = em.createNamedQuery("CustomerOrder.findByLastName")
                .setParameter("lastName", testOrder.getLastName())
                .getResultList();
        Assert.assertTrue(orders.size() > 0);
    }

    @Test
    public void testFindByEmail() {
        List<CustomerOrder> orders = em.createNamedQuery("CustomerOrder.findByEmail")
                .setParameter("email", testOrder.getEmail())
                .getResultList();
        Assert.assertTrue(orders.size() > 0);
    }

    @Test
    public void testFindByPhone() {
        List<CustomerOrder> orders = em.createNamedQuery("CustomerOrder.findByPhone")
                .setParameter("phone", testOrder.getPhone())
                .getResultList();
        Assert.assertTrue(orders.size() > 0);
    }

    @Test
    public void testFindByUserId() {
        User user = UserTest.createUser();
        em.persist(user);
        testOrder.setUser(user);

        em.merge(testOrder);

        List<CustomerOrder> orders = em.createNamedQuery("CustomerOrder.findByUserId")
                .setParameter("userId", user.getId())
                .getResultList();
        Assert.assertEquals(1, orders.size());
    }

    @Test
    public void testFindByRouteId() {
        List<CustomerOrder> orders = em.createNamedQuery("CustomerOrder.findByRouteId")
                .setParameter("routeId", testOrder.getRoute().getId())
                .getResultList();
        Assert.assertEquals(1, orders.size());
    }

    @Test
    public void testInsert() {
        CustomerOrder order = createCustomerOrder();
        Route route = RouteTest.createRoute();
        order.setRoute(route);
        em.persist(route);
        em.persist(order);

        CustomerOrder order2 = em.find(CustomerOrder.class, order.getId());
        Assert.assertNotNull(order2);
    }

    @Test
    public void testDelete() {
        CustomerOrder order = createCustomerOrder();
        Route route = RouteTest.createRoute();
        order.setRoute(route);
        em.persist(route);        
        em.persist(order);
        
        em.remove(order);

        CustomerOrder order2 = em.find(CustomerOrder.class, order.getId());
        Assert.assertNull(order2);
    }

    @Test
    public void testUpdate() {
        CustomerOrder order = createCustomerOrder();
        Route route = RouteTest.createRoute();
        order.setRoute(route);
        em.persist(route);        
        em.persist(order);
        int id = order.getId();

        String email = "bla@bla";
        order.setEmail(email);
        em.merge(order);

        CustomerOrder order2 = em.find(CustomerOrder.class, id);

        Assert.assertEquals(email, order2.getEmail());
    }

    @Test
    public void testGetOrderedTicketList() {
        Category cat = CategoryTest.createCategory();
        em.persist(cat);

        SupportingDocument doc = SupportingDocumentTest.createSupportingDocument();
        doc.setCategory(cat);        
        em.persist(doc);
        
        Ticket ticket = TicketTest.createTicket();
        ticket.setCategory(cat);
        em.persist(ticket); 
        
        OrderedTicket orderedTicket = OrderedTicketTest.createOrderedTicket();
        orderedTicket.setSupportingDocument(doc);
        orderedTicket.setCustomerOrder(testOrder);
        orderedTicket.setTicket(ticket);
        em.persist(orderedTicket);

        em.refresh(testOrder);

        List<OrderedTicket> tickets = testOrder.getOrderedTicketList();
        Assert.assertEquals(1, tickets.size());
    }

    @Test
    public void testEmailValidation() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();

        CustomerOrder order = createCustomerOrder();
        char[] array = new char[255];
        Arrays.fill(array, ' ');
        String wrongEmail = new String(array);
        order.setEmail(wrongEmail);

        Set<ConstraintViolation<CustomerOrder>> violations = validator.validate(order);
        Assert.assertEquals(1, violations.size());
        Assert.assertEquals("size must be between 1 and 254", violations.iterator().next().getMessage());

    }
    
    @Test
    public void testFindMaxCustomerOrder() {
        Integer maxNumber = (Integer) em.createNamedQuery("CustomerOrder.findMaxConfirmationNumber")
                .getSingleResult();
        
        System.out.println("maxNumber=" + maxNumber);
        Assert.assertTrue(maxNumber == testOrder.getConfirmationNumber());
    }

}
