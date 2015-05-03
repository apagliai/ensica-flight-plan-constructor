

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class FondDeCarteTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class FondDeCarteTest
{
    /**
     * Default constructor for test class FondDeCarteTest
     */
    public FondDeCarteTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }


    @Test
    public void affichageSudFrance()
    {
        FondDeCarte fondDeCa1 = new FondDeCarte("France South.jpg", "Sud de la France",1);
    }

    @Test
    public void appartient()
    {
        FondDeCarte fondDeCa1 = new FondDeCarte();
        Waypoint waypoint1 = new Waypoint(400,500,"1", fondDeCa1);
        assertEquals(true, fondDeCa1.appartient(waypoint1));
    }

    @Test
    public void removeWaypoint()
    {
        FondDeCarte fondDeCa1 = new FondDeCarte();
        Waypoint waypoint1 = new Waypoint(200, 400, "1", fondDeCa1);
        Waypoint waypoint2 = new Waypoint(300, 500, "2", fondDeCa1);
        Waypoint waypoint3 = new Waypoint(450, 350, "3", fondDeCa1);
        System.out.println("Cliquez successivement sur chaque waypoint, une fois, de gauche à droite.");
        Leg leg1 = new Leg("TF", fondDeCa1);
        System.out.println("Cliquez sur le waypoint du milieu, puis celui de droite, puis clic droit");
        Leg leg2 = new Leg("TF", fondDeCa1);
        System.out.println("Cliquez sur le waypoint du milieu");
        fondDeCa1.removeWaypoint();
    }


    @Test
    public void deplacement()
    {
        FondDeCarte fondDeCa1 = new FondDeCarte();
        fondDeCa1.deplacementEtZoomClavier();
    }
}






