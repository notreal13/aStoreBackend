package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import session.AbstractFacade;
import session.ConfirmationNumberService;
import session.RouteFacade;
import utils.Resources;
import utils.TicketAudit;
import org.jboss.arquillian.junit.InSequence;

/**
 *
 * @author Notreal
 */
@RunWith(Arquillian.class)
public class ArqIT {

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(
                        Category.class,
                        CustomerOrder.class,
                        OrderedTicket.class,
                        Role.class,
                        Route.class,
                        SupportingDocument.class,
                        Ticket.class,
                        User.class,
                        AbstractFacade.class,
                        RouteFacade.class,
                        ConfirmationNumberService.class,
                        Resources.class,
                        TicketAudit.class)
                // enable JPA
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                // add sample data
                .addAsResource("import.sql")
                // enable CDI
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                // Deploy the test datasource
                .addAsWebInfResource("test-ds.xml");
    }

    private static final String[] ROUTE_NAMES = {
        "kavkaz_krym",
        "krym_kavkaz"
    };

    @Inject
    RouteFacade routeFacade;

    @Inject
    ConfirmationNumberService confirmationNumberService;

    @PersistenceContext
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Before
    public void preparePersistenceTest() throws Exception {
        startTransaction();
    }

    private void startTransaction() throws Exception {
        utx.begin();
        em.joinTransaction();
    }

    @After
    public void commitTransaction() throws Exception {
        utx.commit();
    }

    @Test
    public void shouldFindAllRoutesUsingJpqlQuery() throws Exception {
        String fetchingAllRoutesInJpql = "select r from Route r order by r.id";
        System.out.println("Selecting (using JPQL)...");
        List<Route> routes = em.createQuery(fetchingAllRoutesInJpql, Route.class).getResultList();

        System.out.println("Found " + routes.size() + " routes (using JPQL):");
        assertContainsAllRoutes(routes);
        Assert.assertEquals(2, routes.size());
    }

    private static void assertContainsAllRoutes(Collection<Route> retrievedRoutes) {
        Assert.assertEquals(ROUTE_NAMES.length, retrievedRoutes.size());
        final Set<String> retrievedRouteNames = new HashSet<>();
        for (Route route : retrievedRoutes) {
            System.out.println("* " + route);
            retrievedRouteNames.add(route.getName());
        }
        Assert.assertTrue(retrievedRouteNames.containsAll(Arrays.asList(ROUTE_NAMES)));
    }

    @Test
    @InSequence(1)
    public void shouldGetConfirmationNumber() {
        System.out.println("entring test");

        Integer currentConfirmationNumber = (Integer) em.createNamedQuery("CustomerOrder.findMaxConfirmationNumber")
                .getSingleResult();

        System.out.println("currentConfirmationNumber = " + currentConfirmationNumber);

        Integer nextConfirmationNumber = confirmationNumberService.get();

        System.out.println("nextConfirmationNumber = " + nextConfirmationNumber);
        Assert.assertTrue(nextConfirmationNumber == (currentConfirmationNumber + 1));
    }

    @Test
    @InSequence(2)
    public void shouldGetManyConfirmationNumbers() {
        final int NUM_OF_TASKS = 1000;
        final int NUM_OF_ITERATIONS = 100;
        CountDownLatch latch = new CountDownLatch(1);

        ExecutorService ex = Executors.newFixedThreadPool(NUM_OF_TASKS);

        List<Future<List<Integer>>> futureList = new ArrayList<>();

        for (int i = 0; i < NUM_OF_TASKS; i++) {
            Callable<List<Integer>> runner = new MyRunner(NUM_OF_ITERATIONS, latch);
            Future<List<Integer>> submit = ex.submit(runner);
            futureList.add(submit);
        }

        latch.countDown();

        Set<Integer> checkSet = new HashSet<>();

        for (Future<List<Integer>> f : futureList) {
            try {
                checkSet.addAll(f.get());
            } catch (InterruptedException | ExecutionException ex1) {
                Logger.getLogger(ArqIT.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        ex.shutdown();

        System.out.println("Found unique numbers: " + checkSet.size());
        Assert.assertEquals(NUM_OF_TASKS * NUM_OF_ITERATIONS, checkSet.size());
    }

    class MyRunner implements Callable<List<Integer>> {

        private final int iterations;
        private final CountDownLatch latch;

        MyRunner(int iterations, CountDownLatch latch) {
            this.iterations = iterations;
            this.latch = latch;
        }

        @Override
        public List<Integer> call() throws Exception {
            latch.await();
            List<Integer> numArray = new ArrayList<>();
            for (int i = 0; i < iterations; i++) {
                numArray.add(confirmationNumberService.get());
            }
            return numArray;
        }
    }

}
