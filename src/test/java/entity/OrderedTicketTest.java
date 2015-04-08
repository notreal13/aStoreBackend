package entity;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
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
public class OrderedTicketTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private EntityTransaction tx;

    private static OrderedTicket testTicket;

    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("testPU");
        em = emf.createEntityManager();

        em.getTransaction().begin();
        testTicket = createOrderedTicket();

        Category cat = CategoryTest.createCategory();
        em.persist(cat);
        SupportingDocument doc = SupportingDocumentTest.createSupportingDocument();
        doc.setCategory(cat);
        em.persist(doc);

        Route route = RouteTest.createRoute();
        em.persist(route);
        CustomerOrder order = CustomerOrderTest.createCustomerOrder();
        order.setRoute(route);
        em.persist(order);

        Ticket ticket = TicketTest.createTicket();
        ticket.setCategory(cat);
        em.persist(ticket);

        testTicket.setSupportingDocument(doc);
        testTicket.setCustomerOrder(order);
        testTicket.setTicket(ticket);
        em.persist(testTicket);

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

    public static OrderedTicket createOrderedTicket() {
        OrderedTicket orderedTicket = new OrderedTicket();
        orderedTicket.setFirstName("fName");
        orderedTicket.setLastName("lName");
        orderedTicket.setMiddleName("mName");
        orderedTicket.setDob(new Date());
        orderedTicket.setSupportingDocumentData("test");
        orderedTicket.setLicensePlate("license");
        return orderedTicket;
    }

    @Test
    public void testFindAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(OrderedTicket.class));
        int count = em.createQuery(cq).getResultList().size();

        List<OrderedTicket> tickets = em.createNamedQuery("OrderedTicket.findAll").getResultList();
        Assert.assertEquals(count, tickets.size());
    }

    @Test
    public void testFindById() {
        List<OrderedTicket> tickets = em.createNamedQuery("OrderedTicket.findById")
                .setParameter("id", testTicket.getId())
                .getResultList();
        Assert.assertEquals(1, tickets.size());
    }

    @Test
    public void testFindBySupportingDocumentData() {
        List<OrderedTicket> tickets = em.createNamedQuery("OrderedTicket.findBySupportingDocumentData")
                .setParameter("supportingDocumentData", testTicket.getSupportingDocumentData())
                .getResultList();
        Assert.assertTrue(tickets.size() > 0);
    }

    @Test
    public void testFindByFirstName() {
        List<OrderedTicket> tickets = em.createNamedQuery("OrderedTicket.findByFirstName")
                .setParameter("firstName", testTicket.getFirstName())
                .getResultList();
        Assert.assertTrue(tickets.size() > 0);
    }

    @Test
    public void testFindByLastName() {
        List<OrderedTicket> tickets = em.createNamedQuery("OrderedTicket.findByLastName")
                .setParameter("lastName", testTicket.getLastName())
                .getResultList();
        Assert.assertTrue(tickets.size() > 0);
    }

    @Test
    public void testFindByMiddleName() {
        List<OrderedTicket> tickets = em.createNamedQuery("OrderedTicket.findByMiddleName")
                .setParameter("middleName", testTicket.getMiddleName())
                .getResultList();
        Assert.assertTrue(tickets.size() > 0);
    }

    @Test
    public void testFindByDOB() throws ParseException {
        List<OrderedTicket> tickets = em.createNamedQuery("OrderedTicket.findByDOB")
                .setParameter("dob", testTicket.getDob())
                .getResultList();
        Assert.assertTrue(tickets.size() > 0);
    }
    
    @Test
    public void testFindByLicensePlate() {
        List<OrderedTicket> tickets = em.createNamedQuery("OrderedTicket.findByLicensePlate")
                .setParameter("licensePlate", testTicket.getLicensePlate())
                .getResultList();
        System.out.println(tickets.size());
        Assert.assertTrue(tickets.size() > 0);
    }

    @Test
    public void testInsert() {
        Category cat = CategoryTest.createCategory();
        em.persist(cat);
        SupportingDocument doc = SupportingDocumentTest.createSupportingDocument();
        doc.setCategory(cat);
        em.persist(doc);

        Route route = RouteTest.createRoute();
        em.persist(route);
        CustomerOrder order = CustomerOrderTest.createCustomerOrder();
        order.setRoute(route);
        em.persist(order);

        Ticket ticket = TicketTest.createTicket();
        ticket.setCategory(cat);
        em.persist(ticket);

        OrderedTicket orderedTicket = new OrderedTicket();
        orderedTicket.setFirstName("fName");
        orderedTicket.setLastName("lName");
        orderedTicket.setMiddleName("mName");
        orderedTicket.setDob(new Date());
        orderedTicket.setLicensePlate("license");
        orderedTicket.setSupportingDocumentData("test");
        orderedTicket.setSupportingDocument(doc);
        orderedTicket.setCustomerOrder(order);
        orderedTicket.setTicket(ticket);

        em.persist(orderedTicket);

        OrderedTicket orderedTicket2 = em.find(OrderedTicket.class, orderedTicket.getId());

        Assert.assertNotNull(orderedTicket2);
    }

    @Test
    public void testDelete() {
        Category cat = CategoryTest.createCategory();
        em.persist(cat);
        SupportingDocument doc = SupportingDocumentTest.createSupportingDocument();
        doc.setCategory(cat);
        em.persist(doc);

        Route route = RouteTest.createRoute();
        em.persist(route);
        CustomerOrder order = CustomerOrderTest.createCustomerOrder();
        order.setRoute(route);
        em.persist(order);

        Ticket ticket = TicketTest.createTicket();
        ticket.setCategory(cat);
        em.persist(ticket);

        OrderedTicket orderedTicket = new OrderedTicket();
        orderedTicket.setFirstName("fName");
        orderedTicket.setLastName("lName");
        orderedTicket.setMiddleName("mName");
        orderedTicket.setDob(new Date());
        orderedTicket.setLicensePlate("license");
        orderedTicket.setSupportingDocumentData("test");
        orderedTicket.setSupportingDocument(doc);
        orderedTicket.setCustomerOrder(order);
        orderedTicket.setTicket(ticket);

        em.persist(orderedTicket);

        em.remove(orderedTicket);

        OrderedTicket orderedTicket2 = em.find(OrderedTicket.class, orderedTicket.getId());

        Assert.assertNull(orderedTicket2);
    }

    @Test
    public void testUpdate() {
        Category cat = CategoryTest.createCategory();
        em.persist(cat);
        SupportingDocument doc = SupportingDocumentTest.createSupportingDocument();
        doc.setCategory(cat);
        em.persist(doc);

        Route route = RouteTest.createRoute();
        em.persist(route);
        CustomerOrder order = CustomerOrderTest.createCustomerOrder();
        order.setRoute(route);
        em.persist(order);

        Ticket ticket = TicketTest.createTicket();
        ticket.setCategory(cat);
        em.persist(ticket);

        OrderedTicket orderedTicket = new OrderedTicket();
        orderedTicket.setFirstName("fName");
        orderedTicket.setLastName("lName");
        orderedTicket.setMiddleName("mName");
        orderedTicket.setDob(new Date());
        orderedTicket.setLicensePlate("plate");
        orderedTicket.setSupportingDocumentData("test");
        orderedTicket.setSupportingDocument(doc);
        orderedTicket.setCustomerOrder(order);
        orderedTicket.setTicket(ticket);

        em.persist(orderedTicket);

        int id = orderedTicket.getId();

        String firstName = "newName";
        orderedTicket.setFirstName(firstName);
        em.merge(orderedTicket);

        OrderedTicket orderedTicket2 = em.find(OrderedTicket.class, id);

        Assert.assertEquals(firstName, orderedTicket2.getFirstName());
    }

}
