package entity;

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
public class RoleTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private EntityTransaction tx;
    
    private static Role testRole;

    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("testPU");
        em = emf.createEntityManager();
        
        em.getTransaction().begin();
        testRole = createRole();
        em.persist(testRole);
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
    
    public static Role createRole() {
        Role role = new Role();
        role.setName("testRole");
        return role;
    }    

    @Test
    public void testFindAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Role.class));
        int count = em.createQuery(cq).getResultList().size();
        
        List<Role> roles = em.createNamedQuery("Role.findAll").getResultList();
        Assert.assertEquals(count, roles.size());
    }

    @Test
    public void testFindById() {
        List<Role> roles = em.createNamedQuery("Role.findById")
                .setParameter("id", testRole.getId())
                .getResultList();
        Assert.assertEquals(1, roles.size());
    }

    @Test
    public void testFindByName() {
        List<Role> roles = em.createNamedQuery("Role.findByName")
                .setParameter("name", testRole.getName())
                .getResultList();
        Assert.assertTrue(roles.size() > 0);
    }

    @Test
    public void testInsert() {
        Role role = createRole();

        em.persist(role);

        Role role2 = em.find(Role.class, role.getId());
        Assert.assertNotNull(role2);
    }

    @Test
    public void testDelete() {
        Role role = createRole();
        
        em.persist(role);
        
        em.remove(role);
        
        Role role2 = em.find(Role.class, role.getId());
        Assert.assertNull(role2);
    }

    @Test
    public void testUpdate() {
        Role role = createRole();
        em.persist(role);
        int id = role.getId();

        String name = "testName";
        role.setName(name);
        em.merge(role);
        
        Role role2 = em.find(Role.class, id);
        Assert.assertEquals(name, role2.getName());
    }

}
