

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class LegTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class LegTest
{
    /**
     * Default constructor for test class LegTest
     */
    public LegTest()
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
    public void afficherLegTF()
    {
        FondDeCarte fondDeCa1 = new FondDeCarte();
        Waypoint waypoint1 = new Waypoint(300, 500, "1", fondDeCa1);
        Waypoint waypoint2 = new Waypoint(400, 400, "2", fondDeCa1);
        Leg leg1 = new Leg("TF", fondDeCa1);
    }
    
    @Test
    public void afficherLegCF()
    {
        FondDeCarte fondDeCa1 = new FondDeCarte();
        Waypoint waypoint1 = new Waypoint(300, 500, "1", fondDeCa1);
        Waypoint waypoint2 = new Waypoint(400, 400, "2", fondDeCa1);
        Leg leg1 = new Leg("CF", fondDeCa1);
    }
    
    @Test
    public void afficherLegDF()
    {
        FondDeCarte fondDeCa1 = new FondDeCarte();
        Waypoint waypoint1 = new Waypoint(300, 500, "1", fondDeCa1);
        Waypoint waypoint2 = new Waypoint(400, 400, "2", fondDeCa1);
        Leg leg1 = new Leg("DF", fondDeCa1);
    }
}

