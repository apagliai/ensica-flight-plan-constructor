import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.*;
/**
 * Classe Waypoint : création de tous les types de Waypoints, par clic, ou par entrée clavier.
 * Les méthodes permettent d'afficher les Waypoints et définissent les Legs, c'est-à-dire les types de segments pouvant relier deux Waypoints (Fix) entre eux.
 * 
 * @author Anthony Pagliai & Colin Mourard
 * @version 1.0 - 06.01.2014
 */
public class Waypoint
{
    private int latitude;
    private int longitude;
    private String name;
    private FondDeCarte fond;
    private String status; //Déterminer si un waypoint est de type "fly-by" ou "fly-over", c'est fly-by par défaut.
    
    /**
     * Creation d'un waypoint par entrée clavier.
     * 
     * @param x - Longitude correspondant au waypoint, en nombre de pixels d'abscisse de la photo en fond de carte (pas de la fenêtre d'affichage)
     * @param y - Latitude correspondant au waypoint, en nombre de pixels d'ordonnée de la photo en fond de carte (pas de la fenêtre d'affichage)
     * @param s - Le nom du waypoint.
     * @param f - Fond de Carte, on prendra soin de le charger au préalable.
     * 
     */
    public Waypoint(int x, int y, String s, FondDeCarte f)
    {
        fond = f;
        name = s;
        status = new String("FlyBy");
        longitude = x;
        latitude = y;
        
        //Affichage du waypoint
        fond.getGraphics2D().setColor(Color.red);
        fond.getGraphics2D().fillOval((int)(this.longitude*fond.getImageN6K().getWidth()*fond.getZoom()/fond.getImage().getWidth(null))-7,
                                      (int)(this.latitude*fond.getImageN6K().getHeight()*fond.getZoom()/fond.getImage().getHeight(null))-7,14,14);
        fond.getImageN6K().repaint();
        
        //Ajout de ce waypoint dans le tableau de Waypoint (attribut de la classe FondDeCarte).
        int i = 0;
        while (fond.getTabWaypoint()[i] != null)
        {
            i++;
        }
        fond.getTabWaypoint()[i] = this;
    }

    /**
     * Création d'un waypoint par clic souris. On rentre le nom et le fond de carte dans le constructeur, puis on clique où l'on veut sur le fond de carte.
     * Le waypoint s'affiche directement.
     * 
     * @param s - le nom du waypoint.
     * @param f - le fond de carte qu'on aura pris soin de charger au préalable.
     * 
     */
    public Waypoint(String s, FondDeCarte f)
    {
        name = s;
        fond = f;
        status = new String("FlyBy");
        
        if (fond.getImageN6K().waitClick(new Point()) == 1)
        {
            longitude = (int)((fond.getImageN6K().mouseX()*fond.getImage().getWidth(null)/(fond.getZoom()*fond.getImageN6K().getWidth()))+fond.getOrigine().getX());
            latitude = (int)((fond.getImageN6K().mouseY()*fond.getImage().getHeight(null)/(fond.getZoom()*fond.getImageN6K().getHeight()))+fond.getOrigine().getY());
        }

        //Affichage du waypoint
        fond.getGraphics2D().setColor(Color.red);
        fond.getGraphics2D().fillOval((int)((this.longitude-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null))-7,
                                      (int)((this.latitude-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null))-7,
                                      14,14);
        fond.getImageN6K().repaint();
        
        //Ajout de ce waypoint dans le tableau de Waypoint (attribut de la classe FondDeCarte).
        int i = 0;
        while (fond.getTabWaypoint()[i] != null)
        {
            i++;
        }
        fond.getTabWaypoint()[i] = this;
    }

    /**
     * Méthode observateur attribut longitude
     */
    public int getLongitude() {return this.longitude;}
    /**
     * Méthode obersateur attribut latitude
     */
    public int getLatitude() {return this.latitude;}
    /**
     * Méthode observateur attribut name
     */
    public String getName() {return this.name;}
    /**
     * Méthode observateur attribut FondDeCarte
     */
    public FondDeCarte getFondDeCarte() {return this.fond;}
    /**
     * Méthode observateur attribut status
     */
    public String getStatus() {return this.status;}
    
    /**
     * Méthode activateur attribut longitude
     */
    public void setLongitude(int l) { this.longitude = l;}
    /**
     * Méthode activateur attribut latitude
     */
    public void setLatitude(int l) {this.latitude = l;}
    /**
     * Méthode activateur attribut name
     */
    public void setName(String s) {this.name = s;}
    /**
     * Méthode activateur attribut FondDeCarte
     */
    public void setFondDeCarte(FondDeCarte f) {this.fond = f;}
    /**
     * Méthode activateur attribut status
     * 
     * @param stat - le statut du waypoint TRES IMPORTANT, A RESPECTER : ne peut être que "FlyBy" ou "FlyOver".
     */
    public void setStatus(String stat)
    {
        if (stat != "FlyOver" && stat != "FlyBy") 
        {
            Exception exception = new Exception("ATTENTION : " + stat + " est un mauvais type de statut. Veuillez relancer la méthode.");
            System.out.println(exception.getMessage());
        }
        else {this.status = stat;}
    }
    
    /**
     * Afficher un waypoint au bon endroit sur le fond de carte, indépendamment de quelle partie du fond de carte est visible dans la fenêtre graphique et du zoom.
     */
    public void afficherWaypoint()
    {
        fond.getGraphics2D().setColor(Color.red);
        fond.getGraphics2D().fillOval((int)((this.longitude-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null))-7,
                                      (int)((this.latitude-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null))-7,
                                      14,14);
        fond.getImageN6K().repaint();
    }
}
