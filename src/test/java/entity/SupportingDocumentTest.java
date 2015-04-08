package entity;

import static entity.OrderedTicketTest.createOrderedTicket;
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
public class SupportingDocumentTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private EntityTransaction tx;
    
    private static SupportingDocument testDoc;
    
    public static SupportingDocument createSupportingDocument() {
        SupportingDocument doc = new SupportingDocument();
        doc.setName("testDoc");
        doc.setDocType(77);
        return doc;
    }

    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("testPU");
        em = emf.createEntityManager();
        
        em.getTransaction().begin();
        testDoc = createSupportingDocument();
        
        Category cat = CategoryTest.createCategory();
        em.persist(cat);

        testDoc.setCategory(cat);
        em.persist(testDoc);
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

    @Test
    public void testFindAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(SupportingDocument.class));
        int count = em.createQuery(cq).getResultList().size();        
        
        List<SupportingDocument> docs = em.createNamedQuery("SupportingDocument.findAll").getResultList();
        Assert.assertEquals(count, docs.size());
    }

    @Test
    public void testFindById() {
        List<SupportingDocument> docs = em.createNamedQuery("SupportingDocument.findById")
                .setParameter("id", testDoc.getId())
                .getResultList();
        Assert.assertEquals(1, docs.size());
    }

    @Test
    public void testFindByName() {
        List<SupportingDocument> docs = em.createNamedQuery("SupportingDocument.findByName")
                .setParameter("name", testDoc.getName())
                .getResultList();
        Assert.assertTrue(docs.size() > 0);
    }

    @Test
    public void testFindByDocType() {
        List<SupportingDocument> docs = em.createNamedQuery("SupportingDocument.findByDocType")
                .setParameter("docType", testDoc.getDocType())
                .getResultList();
        Assert.assertTrue(docs.size() > 0);
    }

    @Test
    public void testGetOrderedTicketList() {
        OrderedTicket orderedTicket = createOrderedTicket();
        Category cat = CategoryTest.createCategory();
        em.persist(cat);
        testDoc.setCategory(cat);
        
        Route route = RouteTest.createRoute();
        em.persist(route);
        CustomerOrder order = CustomerOrderTest.createCustomerOrder();
        order.setRoute(route);
        em.persist(order);
        
        Ticket ticket = TicketTest.createTicket();
        ticket.setCategory(cat);
        em.persist(ticket);
        
        orderedTicket.setSupportingDocument(testDoc);
        orderedTicket.setCustomerOrder(order);
        orderedTicket.setTicket(ticket);
        
        em.persist(orderedTicket);
        
        em.refresh(testDoc);
        
        List<OrderedTicket> tickets = testDoc.getOrderedTicketList();
        Assert.assertEquals(1, tickets.size());
    }

    @Test
    public void testInsert() {
        SupportingDocument doc = createSupportingDocument();

        Category cat = CategoryTest.createCategory();
        em.persist(cat);
        doc.setCategory(cat);
        
        em.persist(doc);

        SupportingDocument doc2 = em.find(SupportingDocument.class, doc.getId());
        Assert.assertNotNull(doc2);
    }

    @Test
    public void testDelete() {
        SupportingDocument doc = createSupportingDocument();
        Category cat = CategoryTest.createCategory();
        em.persist(cat);
        doc.setCategory(cat);

        em.persist(doc);
        
        em.remove(doc);

        SupportingDocument doc2 = em.find(SupportingDocument.class, doc.getId());
        Assert.assertNull(doc2);
    }

    @Test
    public void testUpdate() {
        SupportingDocument doc = createSupportingDocument();
        Category cat = CategoryTest.createCategory();
        em.persist(cat);
        doc.setCategory(cat);
        em.persist(doc);
        int id = doc.getId();
        
        String name = "newName";
        doc.setName(name);
        em.merge(doc);

        SupportingDocument doc2 = em.find(SupportingDocument.class, id);
        Assert.assertEquals(name, doc2.getName());
    }

}
