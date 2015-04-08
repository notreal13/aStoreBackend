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
public class UserTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private EntityTransaction tx;

    private static User testUser;

    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("testPU");
        em = emf.createEntityManager();

        em.getTransaction().begin();
        testUser = createUser();
        em.persist(testUser);
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

    public static User createUser() {
        User user = new User();
        user.setUsername("email@email");
        user.setFirstName("fName");
        user.setLastName("lName");
        user.setPhone("111222333");
        user.setActive(true);

        user.setSalt("salt");
        user.setToken("token");
        user.setPassword("password");
        return user;
    }

    @Test
    public void testFindAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(User.class));
        int count = em.createQuery(cq).getResultList().size();

        List<User> users = em.createNamedQuery("User.findAll").getResultList();
        Assert.assertEquals(count, users.size());
    }

    @Test
    public void testFindById() {
        List<User> users = em.createNamedQuery("User.findById")
                .setParameter("id", testUser.getId())
                .getResultList();
        Assert.assertEquals(1, users.size());
    }

    @Test
    public void testFindByEmail() {
        List<User> users = em.createNamedQuery("User.findByUsername")
                .setParameter("username", testUser.getUsername())
                .getResultList();
        Assert.assertTrue(users.size() > 1);
    }

    @Test
    public void testFindByPassword() {
        List<User> users = em.createNamedQuery("User.findByPassword")
                .setParameter("password", testUser.getPassword())
                .getResultList();
        Assert.assertTrue(users.size() > 0);
    }

    @Test
    public void testFindBySalt() {
        List<User> users = em.createNamedQuery("User.findBySalt")
                .setParameter("salt", testUser.getSalt())
                .getResultList();
        Assert.assertTrue(users.size() > 0);
    }

    @Test
    public void testFindByFirstName() {
        List<User> users = em.createNamedQuery("User.findByFirstName")
                .setParameter("firstName", testUser.getFirstName())
                .getResultList();
        Assert.assertTrue(users.size() > 0);
    }

    @Test
    public void testFindByLastName() {
        List<User> users = em.createNamedQuery("User.findByLastName")
                .setParameter("lastName", testUser.getLastName())
                .getResultList();
        Assert.assertTrue(users.size() > 0);
    }

    @Test
    public void testFindByPhone() {
        List<User> users = em.createNamedQuery("User.findByPhone")
                .setParameter("phone", testUser.getPhone())
                .getResultList();
        Assert.assertTrue(users.size() > 0);
    }

    @Test
    public void testFindByActive() {
        List<User> users = em.createNamedQuery("User.findByActive")
                .setParameter("active", testUser.isActive())
                .getResultList();
        Assert.assertTrue(users.size() > 0);
    }

    
    @Test
    public void testFindByDateCreated() {
        User user = createUser();
        em.persist(user);
        em.refresh(user);

        List<User> users = em.createNamedQuery("User.findByDateCreated")
                .setParameter("dateCreated", user.getDateCreated())
                .getResultList();
        Assert.assertTrue(users.size() > 0);
    }

    @Test
    public void testFindByToken() {
        List<User> users = em.createNamedQuery("User.findByToken")
                .setParameter("token", testUser.getToken())
                .getResultList();
        Assert.assertTrue(users.size() > 0);
    }

    @Test
    public void testFindByUsernameAndPassword() {
        List<User> users = em.createNamedQuery("User.findByUsernameAndPassword")
                .setParameter("username", testUser.getUsername())
                .setParameter("password", testUser.getPassword())
                .getResultList();
        Assert.assertTrue(users.size() > 0);
    }

    @Test
    public void testInsert() {
        User user = createUser();

        em.persist(user);

        User user2 = em.find(User.class, user.getId());

        Assert.assertNotNull(user2);
    }

    @Test
    public void testDelete() {
        User user = createUser();

        em.persist(user);

        em.remove(user);

        User user2 = em.find(User.class, user.getId());
        Assert.assertNull(user2);
    }

    @Test
    public void testUpdate() {
        User user = createUser();
        em.persist(user);
        int id = user.getId();

        String username = "bla@bla";
        user.setUsername(username);
        em.merge(user);

        User user2 = em.find(User.class, id);

        Assert.assertEquals(username, user2.getUsername());
    }

}
