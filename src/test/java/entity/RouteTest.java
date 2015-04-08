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
public class RouteTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private EntityTransaction tx;

    private static Route testRoute;

    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("testPU");
        em = emf.createEntityManager();

        em.getTransaction().begin();
        testRoute = createRoute();
        em.persist(testRoute);
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

    public static Route createRoute() {
        Route route = new Route();
        route.setName("testRoute");
        return route;
    }

    @Test
    public void testFindAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Route.class));
        int count = em.createQuery(cq).getResultList().size();

        List<Route> routes = em.createNamedQuery("Route.findAll").getResultList();
        Assert.assertEquals(count, routes.size());
    }

    @Test
    public void testFindById() {
        Route route = (Route) em.createNamedQuery("Route.findById")
                .setParameter("id", testRoute.getId())
                .getSingleResult();
        Assert.assertNotNull(route);
    }

    @Test
    public void testFindByName() {
        List<Route> routes = em.createNamedQuery("Route.findByName")
                .setParameter("name", testRoute.getName())
                .getResultList();
        Assert.assertTrue(routes.size() > 0);
    }

    @Test
    public void testInsert() {
        Route route = createRoute();

        em.persist(route);

        Route route2 = em.find(Route.class, route.getId());
        Assert.assertNotNull(route2);
    }

    @Test
    public void testDelete() {
        Route route = createRoute();

        em.persist(route);

        em.remove(route);

        Route route2 = em.find(Route.class, route.getId());
        Assert.assertNull(route2);
    }

    @Test
    public void testUpdate() {
        Route route = createRoute();
        em.persist(route);
        int id = route.getId();

        String name = "newName";
        route.setName(name);
        em.merge(route);

        Route route2 = em.find(Route.class, route.getId());

        Assert.assertEquals(name, route2.getName());
    }

    @Test
    public void testNameValidation() {

        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();

        Route route = new Route();
        route.setName("");

        Set<ConstraintViolation<Route>> violations = validator.validate(route);
        Assert.assertEquals(1, violations.size());
        Assert.assertEquals("size must be between 1 and 45", violations.iterator().next().getMessage());
    }

    @Test
    public void testNameValidation2() {

        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();

        Route route = new Route();
        char[] array = new char[46];
        Arrays.fill(array, ' ');
        String bigName = new String(array);
        route.setName(bigName);

        Set<ConstraintViolation<Route>> violations = validator.validate(route);
        Assert.assertEquals(1, violations.size());
        Assert.assertEquals("size must be between 1 and 45", violations.iterator().next().getMessage());
    }

    @Test
    public void testGetCustomerOrderList() {
        CustomerOrder order = CustomerOrderTest.createCustomerOrder();
        order.setRoute(testRoute);
        em.persist(order);
        
        em.refresh(testRoute);
        
        List<CustomerOrder> orders = testRoute.getCustomerOrderList();
        Assert.assertEquals(1, orders.size());
    }

}
