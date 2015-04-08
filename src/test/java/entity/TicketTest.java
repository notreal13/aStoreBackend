package entity;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import org.joda.time.DateTime;
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
public class TicketTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private EntityTransaction tx;
    
    private static Ticket testTicket;

    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("testPU");
        em = emf.createEntityManager();
        
        em.getTransaction().begin();
        testTicket = createTicket();
        
        Category cat = CategoryTest.createCategory();
        em.persist(cat);
        
        testTicket.setCategory(cat);
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
    
    public static Ticket createTicket() {
        Ticket ticket = new Ticket();
        ticket.setName("testTicket");
        ticket.setPrice(BigDecimal.ZERO);
        ticket.setDescription("desc");
        ticket.setDataLabel("");
        return ticket;
    }

    @Test
    public void testFindAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Ticket.class));
        int count = em.createQuery(cq).getResultList().size();        
        
        List<Ticket> tickets = em.createNamedQuery("Ticket.findAll").getResultList();
        Assert.assertEquals(count, tickets.size());
    }

    @Test
    public void testFindById() {
        List<Ticket> tickets = em.createNamedQuery("Ticket.findById")
                .setParameter("id", testTicket.getId())
                .getResultList();
        Assert.assertEquals(1, tickets.size());
    }

    @Test
    public void testFindByName() {
        List<Ticket> tickets = em.createNamedQuery("Ticket.findByName")
                .setParameter("name", testTicket.getName())
                .getResultList();
        Assert.assertTrue(tickets.size() > 0);
    }

    @Test
    public void testFindByPrice() {
        List<Ticket> tickets = em.createNamedQuery("Ticket.findByPrice")
                .setParameter("price", testTicket.getPrice())
                .getResultList();
        Assert.assertTrue(tickets.size() > 0);
    }

    @Test
    public void testFindByDescription() {
        List<Ticket> tickets = em.createNamedQuery("Ticket.findByDescription")
                .setParameter("description", testTicket.getDescription())
                .getResultList();
        Assert.assertTrue(tickets.size() > 0);
    }

    @Test
    public void testFindByLastUpdate() {
        List<Ticket> tickets = em.createNamedQuery("Ticket.findByLastUpdate")
                .setParameter("lastUpdate", testTicket.getLastUpdate())
                .getResultList();
        Assert.assertTrue(tickets.size() > 0);
    }

    @Test
    public void testFindByDataLabel() {
        List<Ticket> tickets = em.createNamedQuery("Ticket.findByDataLabel")
                .setParameter("dataLabel", testTicket.getDataLabel())
                .getResultList();
        Assert.assertTrue(tickets.size() > 0);
    }

    @Test
    public void testInsert() {
        Category cat = CategoryTest.createCategory();
        em.persist(cat);
        Ticket ticket = createTicket();
        ticket.setCategory(cat);

        em.persist(ticket);

        Ticket ticket2 = em.find(Ticket.class, ticket.getId());

        Assert.assertNotNull(ticket2);
    }

    @Test
    public void testDelete() {
        Category cat = CategoryTest.createCategory();
        em.persist(cat);
        Ticket ticket = createTicket();
        ticket.setCategory(cat);

        em.persist(ticket);
        
        em.remove(ticket);

        Ticket ticket2 = em.find(Ticket.class, ticket.getId());

        Assert.assertNull(ticket2);
    }

    @Test
    public void testUpdate() {
        Category cat = CategoryTest.createCategory();
        em.persist(cat);
        Ticket ticket = createTicket();
        ticket.setCategory(cat);
        em.persist(ticket);
        int id = ticket.getId();
        
        String name = "newName";
        ticket.setName(name);
        em.merge(ticket);

        Ticket ticket2 = em.find(Ticket.class, ticket.getId());

        Assert.assertEquals(name, ticket2.getName());

    }

    @Test
    public void testLastUpdate() {
        Category cat = CategoryTest.createCategory();
        em.persist(cat);
        Ticket ticket = createTicket();
        ticket.setCategory(cat);
        em.persist(ticket);

        DateTime dt = new DateTime(ticket.getLastUpdate());
        Assert.assertNotNull(dt);
        System.out.println("Joda time = " + dt);
        
        ticket.setDataLabel("newLabel1");
        em.merge(ticket);
        tx.commit();
        tx.begin();
        
        DateTime dtUpdate = new DateTime(ticket.getLastUpdate());
        System.out.println("Update time = " + dtUpdate);

        Assert.assertTrue(dt.isBefore(dtUpdate));
    }

}
