import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * Bibliothèque de Waypoints. Permet de sauvegarder tous les Waypoints créés.
 * 
 * @author Anthony Pagliai & Colin Mourard
 * @version 1.0 - 15.01.2014
 */
public class WaypointLibrary
{
    public Waypoint [] waypointDataBase;
    //public DataOutputStream fichierDePoints = new DataOutputStream(new FileOutputStream("MyFixDataBase.dat"));

    /**
     * Constructeur du tableau.
     */
    public WaypointLibrary()
    {
        //fichierDePoints.writeInt(waypointDataBase.length);
        for (int i=0; i<waypointDataBase.length; i++)
        {
            //waypointDataBase.ecrireDans(fichierDePoints);
        }
    }
    
    public void ecrireDans(DataOutputStream fichier) throws IOException
    {
        for (int i=0; i<100; i++)
        {
            fichier.writeChar(waypointDataBase[i].getName().charAt(i));
        }
    }
}
