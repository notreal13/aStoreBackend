package entity;

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
public class CategoryTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private EntityTransaction tx;
    
    private static Category testCat;

    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("testPU");
        em = emf.createEntityManager();
        
        em.getTransaction().begin();
        testCat = createCategory();
        em.persist(testCat);
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
    
    public static Category createCategory() {
        Category category = new Category();
        category.setName("testCat");
        return category;
    }
    
    @Test
    public void testFindAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Category.class));
        int count = em.createQuery(cq).getResultList().size();
        
        List<Category> categories = em.createNamedQuery("Category.findAll").getResultList();
        Assert.assertEquals(count, categories.size());
    }

    @Test
    public void testFindById() {
        Category category = (Category) em.createNamedQuery("Category.findById")
                .setParameter("id", testCat.getId())
                .getSingleResult();
        Assert.assertNotNull(category);
    }

    @Test
    public void testFindByName() {
        List<Category> categories = em.createNamedQuery("Category.findByName")
                .setParameter("name", testCat.getName())
                .getResultList();
        Assert.assertEquals(1, categories.size());
    }

    @Test
    public void testInsert() {
        Category category = createCategory();

        em.persist(category);

        Category category2 = em.find(Category.class, category.getId());
        Assert.assertNotNull(category2);
    }

    @Test
    public void testDelete() {
        Category category = createCategory();

        em.persist(category);
        
        em.remove(category);

        Category category2 = em.find(Category.class, category.getId());
        Assert.assertNull(category2);
    }

    @Test
    public void testUpdate() {
        Category category = createCategory();
        em.persist(category);
        int id = category.getId();
        
        String name = "newName";
        category.setName(name);
        em.merge(category);

        Category category2 = em.find(Category.class, category.getId());

        Assert.assertEquals(name, category2.getName());
    }

    @Test
    public void testNameValidation() {

        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();

        Category category = new Category();
        category.setName("");

        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        Assert.assertEquals(1, violations.size());
        Assert.assertEquals("size must be between 1 and 45", violations.iterator().next().getMessage());
    }

    @Test
    public void testNameValidation2() {
        
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();

        Category category = new Category();
        char[] array = new char[46];
        Arrays.fill(array, ' ');
        String bigName = new String(array);
        category.setName(bigName);

        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        Assert.assertEquals(1, violations.size());
        Assert.assertEquals("size must be between 1 and 45", violations.iterator().next().getMessage());
    }
    
    @Test
    public void testGetTicketList() {
        Ticket ticket = TicketTest.createTicket();
        ticket.setCategory(testCat);
        em.persist(ticket);
        
        em.refresh(testCat);
        
        List<Ticket> tickets = testCat.getTicketList();
        Assert.assertTrue(tickets.size() > 0);
    }
    
    @Test
    public void testGetSupportingDocumentList() {
        SupportingDocument doc = SupportingDocumentTest.createSupportingDocument();
        doc.setCategory(testCat);
        em.persist(doc);
        
        em.refresh(testCat);
        
        List<SupportingDocument> docs = testCat.getSupportingDocumentList();
        Assert.assertTrue(docs.size() > 0);
    }
    

}
