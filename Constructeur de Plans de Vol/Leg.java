import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.*;
/**
 * Permet de construire tous les legs, dans un fond de carte donné.
 * 
 * La console est très importante avec cette classe, à chaque clic ou entrée clavier, elle affiche un nouveau message. Gardez donc toujours un oeil dessus, cela peut
 * être utile...
 * 
 * @author Anthony Pagliai & Colin Mourard
 * @version 1.0 - 22.01.2014
 */
public class Leg
{
    private String type;
    private FondDeCarte fond;
    private Waypoint depart;
    private Waypoint arrivee;
    private Waypoint tangence;
    private int cap;    //Utile uniquement pour le Course To Fix

    /**
     * Constructeur de Leg. Prévoir d'avoir déjà chargé un objet de type FondDeCarte
     * 
     * CourseToFix : le cap sera interprété comme un QDM, c'est-à-dire que l'avion rejoindra le waypoint voulu en suivant la cap que l'on indiquera.
     *               De plus, les caps sont définis selon la réglementation aéronautique : 0° indique le Nord, 90° l'Est, 180° le Sud et 270° l'Ouest.
     *
     * @param legType - TRES IMPORTANT, A RESPECTER : fixType ne peut qu'être "TF" (Track to Fix), "CF" (Course to Fix) ou "DF" (Direct to Fix)
     * @param f - le fond de carte. Prévoir de l'avoir déjà chargé.
     * 
     */
    public Leg(String legType, FondDeCarte f)
    {
        fond = f;

        //Vérification que le fixType est bien correctement défini.
        if (legType != "TF" && legType != "RF" && legType != "CF" && legType != "DF")
        {
            Exception exception = new Exception("ATTENTION : " + legType + " est un mauvais type de leg. Veuillez relancer le constructeur.");
            System.out.println(exception.getMessage());
        }
        else {this.type = legType;}

        //Cas du TrackToFix (TF)
        if (legType == "TF")
        {
            //Sélection du waypoint initial par clic.
            System.out.println("Veuillez cliquer sur le WayPoint initial");
            boolean detection = false;  //Permet de savoir si un waypoint a été détecté par le clic.
            while (detection != true)
            {
                int clic = fond.getImageN6K().waitClick(new Point());
                if (clic == 1)
                {
                    for (int i=0; i<fond.getTabWaypoint().length && depart == null; i++)
                    {
                        if (fond.getTabWaypoint()[i] != null)
                        {
                            if (fond.distanceCarre(fond.getTabWaypoint()[i]) < 400) //Clic détecté sur tout le cercle de rayon 20 pixels, de centre le centre du WayPoint
                            {
                                depart = fond.getTabWaypoint()[i];
                                detection = true;
                            }
                        }
                    }

                    if (depart == null) {System.out.println("Pas de WayPoint détecté. Veuillez réessayer de cliquer sur le Waypoint initial");}
                }
            }

            //Sélection du waypoint final par clic.
            System.out.println("Veuillez cliquer sur le WayPoint final");
            detection = false;
            while (detection != true)
            {
                int clic = fond.getImageN6K().waitClick(new Point());
                if (clic == 1)
                {
                    for (int i=0; i<fond.getTabWaypoint().length && arrivee == null; i++)
                    {
                        if (fond.getTabWaypoint()[i] != null)
                        {
                            if (fond.distanceCarre(fond.getTabWaypoint()[i]) < 400) //Clic détecté sur tout le cercle de rayon 20 pixels, de centre le centre du WayPoint
                            {
                                arrivee = fond.getTabWaypoint()[i];
                                detection = true;
                            }
                        }
                    }

                    if (arrivee == null) {System.out.println("Pas de WayPoint détecté. Veuillez réessayer de cliquer sur le Waypoint final");}
                }
            }

            //Sélection du waypoint pour faire la tangence, ou clic droit si on ne souhaite pas en sélectionner.
            System.out.println("Veuillez cliquer sur le Waypoint suivant de la trajectoire (afin de pouvoir calculer une trajectoire tangente)");
            System.out.println("   OU Clic DROIT pour ne pas en sélectionner du tout");
            detection = false;
            while (detection != true)
            {
                int clic = fond.getImageN6K().waitClick(new Point());
                if (clic == 1)
                {
                    for (int i=0; i<fond.getTabWaypoint().length && tangence == null; i++)
                    {
                        if (fond.getTabWaypoint()[i] != null)
                        {
                            if (fond.distanceCarre(fond.getTabWaypoint()[i]) < 400) //Clic détecté sur tout le cercle de rayon 20 pixels, de centre le centre du WayPoint
                            {
                                tangence = fond.getTabWaypoint()[i];
                                detection = true;
                            }
                        }
                    }

                    if (tangence == null) 
                    {
                        System.out.println("Pas de WayPoint détecté. Veuillez réessayer de cliquer sur le Waypoint tangence");
                        System.out.println("   OU Clic DROIT pour ne pas en sélectionner");
                    }
                }
                else if (clic == 3) {tangence = null; detection = true;}
            }

            //Affichage du leg sur le fond de carte.
            fond.getGraphics2D().setColor(Color.red);
            fond.getGraphics2D().drawLine((int)((depart.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)),
                (int)((depart.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)),
                (int)((arrivee.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)),
                (int)((arrivee.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)));
            fond.getImageN6K().repaint();       
        }

        //Cas du CourseToFix (CF)
        if (legType == "CF")
        {
            //Sélection du waypoint final par clic.
            System.out.println("Veuillez cliquer sur le WayPoint final");
            boolean detection = false;   //Permet de savoir si un waypoint a été détecté par le clic.
            while (detection != true)
            {
                int clic = fond.getImageN6K().waitClick(new Point());
                if (clic == 1)
                {
                    for (int i=0; i<fond.getTabWaypoint().length && arrivee == null; i++)
                    {
                        if (fond.getTabWaypoint()[i] != null)
                        {
                            if (fond.distanceCarre(fond.getTabWaypoint()[i]) < 400) //Clic détecté sur tout le cercle de rayon 20 pixels, de centre le centre du WayPoint
                            {
                                arrivee = fond.getTabWaypoint()[i];
                                detection = true;
                            }
                        }
                    }

                    if (arrivee == null) {System.out.println("Pas de WayPoint détecté. Veuillez réessayer de cliquer sur le Waypoint final");}
                }
            }

            //Capture du cap (QDR) par entrée clavier, nécéssite de rentrer les 3 chiffres : par exemple, cap 45° s'écrit 045.
            System.out.println("Veuillez entrer le cap souhaité à l'aide du clavier.");
            boolean juste = false;
            while (juste == false)
            {
                cap = 0;
                char centaine = fond.getImageN6K().waitChar();
                char dizaine = fond.getImageN6K().waitChar();
                char unite = fond.getImageN6K().waitChar();
                cap = 100*(Integer.parseInt("" + centaine)) + 10*(Integer.parseInt("" + dizaine)) + Integer.parseInt("" + unite);
                System.out.println(cap+"° est-il le bon cap? Clic gauche : OUI // Clic droit : NON");
                if (fond.getImageN6K().waitClick(new Point()) == 1) {juste = true;}
                else {System.out.println("Veuillez réécrire le cap souhaité, après avoir recliqué sur le fond de carte.");}
            }

            //Sélection du waypoint pour faire la tangence, ou clic droit si on ne souhaite pas en sélectionner.
            System.out.println("Veuillez cliquer sur le Waypoint suivant de la trajectoire (afin de pouvoir calculer une trajectoire tangente)");
            System.out.println("   OU Clic DROIT pour ne pas en sélectionner du tout");
            detection = false;
            while (detection != true)
            {
                int clic = fond.getImageN6K().waitClick(new Point());
                if (clic == 1)
                {
                    for (int i=0; i<fond.getTabWaypoint().length && tangence == null; i++)
                    {
                        if (fond.getTabWaypoint()[i] != null)
                        {
                            if (fond.distanceCarre(fond.getTabWaypoint()[i]) < 400) //Clic détecté sur tout le cercle de rayon 20 pixels, de centre le centre du WayPoint
                            {
                                tangence = fond.getTabWaypoint()[i];
                                detection = true;
                            }
                        }
                    }

                    if (tangence == null) 
                    {
                        System.out.println("Pas de WayPoint détecté. Veuillez réessayer de cliquer sur le Waypoint tangence");
                        System.out.println("   OU Clic DROIT pour ne pas en sélectionner");
                    }
                }
                else if (clic == 3) {tangence = null; detection = true;}
            }

            //Conversion du cap en angle selon la définition des angles sous Java (zéro à l'horizontal vers la droite puis orientation trigo) et affichage du leg.
            int angle = 0;
            if (cap > 270)
            {
                angle = 450-cap;
                //Tracé du leg CF
                int x_int = (int)((arrivee.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)-200*Math.cos(Math.toRadians(angle)));
                int y_int = (int)((arrivee.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)+200*Math.sin(Math.toRadians(angle)));
                fond.getGraphics2D().setColor(Color.red);
                fond.getGraphics2D().drawLine(x_int,y_int,
                    (int)((arrivee.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)),
                    (int)((arrivee.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)));
                fond.getImageN6K().repaint();
            }
            else if (cap < 90)
            {
                angle = 90-cap;
                //Tracé du leg CF
                int x_int = (int)((arrivee.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)-200*Math.cos(Math.toRadians(angle)));
                int y_int = (int)((arrivee.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)+200*Math.sin(Math.toRadians(angle)));
                fond.getGraphics2D().setColor(Color.red);
                fond.getGraphics2D().drawLine(x_int,y_int,
                    (int)((arrivee.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)),
                    (int)((arrivee.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)));
                fond.getImageN6K().repaint();
            }
            else 
            {  
                angle = 270-cap;
                //Tracé du leg CF
                int x_int = (int)((arrivee.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)+200*Math.cos(Math.toRadians(angle)));
                int y_int = (int)((arrivee.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)-200*Math.sin(Math.toRadians(angle)));
                fond.getGraphics2D().setColor(Color.red);
                fond.getGraphics2D().drawLine(x_int,y_int,
                    (int)((arrivee.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)),
                    (int)((arrivee.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)));
                fond.getImageN6K().repaint();
            }
        }

        //Cas du DirectToFix (DF)
        if (legType == "DF")    
        {         
            //Sélection du point à partir duquel tracer le Leg et affichage du Leg.
            System.out.println("Appuyer sur le fond de carte à l'endroit souhaité du début du Leg DF");
            int clic = fond.getImageN6K().waitClick(new Point());
            if (clic == 1)
            {
                depart = new Waypoint(0,0,"DepartDF",fond);  //Utile pour les 2 lignes suivantes, sans cette ligne, je manipule un objet vide (null) ce qui ne va pas. 
                depart.setLongitude((int)(fond.getOrigine().getX()+(fond.getImageN6K().mouseX()*fond.getImage().getWidth(null)/(fond.getZoom()*fond.getImageN6K().getWidth()))));
                depart.setLatitude((int)(fond.getOrigine().getY()+(fond.getImageN6K().mouseY()*fond.getImage().getHeight(null)/(fond.getZoom()*fond.getImageN6K().getHeight()))));
            }

            //Sélection du waypoint final par clic.
            System.out.println("Veuillez cliquer sur le WayPoint final");
            boolean detection = false;   //Permet de savoir si un waypoint a été détecté par le clic.
            while (detection != true)
            {
                clic = fond.getImageN6K().waitClick(new Point());
                if (clic == 1)
                {
                    for (int i=0; i<fond.getTabWaypoint().length && arrivee == null; i++)
                    {
                        if (fond.getTabWaypoint()[i] != null)
                        {
                            if (fond.distanceCarre(fond.getTabWaypoint()[i]) < 400) //Clic détecté sur tout le cercle de rayon 20 pixels, de centre le centre du WayPoint
                            {
                                arrivee = fond.getTabWaypoint()[i];
                                detection = true;
                            }
                        }
                    }

                    if (arrivee == null) {System.out.println("Pas de WayPoint détecté. Veuillez réessayer de cliquer sur le Waypoint final");}
                }
            }

            //Sélection du waypoint pour faire la tangence, ou clic droit si on ne souhaite pas en sélectionner.
            System.out.println("Veuillez cliquer sur le Waypoint suivant de la trajectoire (afin de pouvoir calculer une trajectoire tangente)");
            System.out.println("   OU Clic DROIT pour ne pas en sélectionner du tout");
            detection = false;
            while (detection != true)
            {
                clic = fond.getImageN6K().waitClick(new Point());
                if (clic == 1)
                {
                    for (int i=0; i<fond.getTabWaypoint().length && tangence == null; i++)
                    {
                        if (fond.getTabWaypoint()[i] != null)
                        {
                            if (fond.distanceCarre(fond.getTabWaypoint()[i]) < 400) //Clic détecté sur tout le cercle de rayon 20 pixels, de centre le centre du WayPoint
                            {
                                tangence = fond.getTabWaypoint()[i];
                                detection = true;
                            }
                        }
                    }

                    if (tangence == null) 
                    {
                        System.out.println("Pas de WayPoint détecté. Veuillez réessayer de cliquer sur le Waypoint tangence");
                        System.out.println("   OU Clic DROIT pour ne pas en sélectionner");
                    }
                }
                else if (clic == 3) {tangence = null; detection = true;}
            }

            //Affichage du leg sur le fond de carte.
            fond.getGraphics2D().setColor(Color.red);
            fond.getGraphics2D().drawLine((int)((depart.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)),
                (int)((depart.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)),
                (int)((arrivee.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)),
                (int)((arrivee.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)));
            fond.getImageN6K().repaint();       
        }

        //Ajout de ce leg dans le tableau de Leg (attribut de la classe FondDeCarte).
        int i = 0;
        while (fond.getTabLeg()[i] != null)
        {
            i++;
        }
        fond.getTabLeg()[i] = this;
    }

    /**
     * Méthode observateur attribut fixType
     */
    public String getType() {return this.type;}
    /**
     * Méthode observateur attribut FondDeCarte
     */
    public FondDeCarte getFond() {return this.fond;}
    /**
     * Méthode observateur attribut depart
     */
    public Waypoint getDepart() {return this.depart;}
    /**
     * Méthode observateur attribut arrivee
     */
    public Waypoint getArrivee() {return this.arrivee;}
    /**
     * Méthode observateur attribut tangence
     */
    public Waypoint getTangence() {return this.tangence;}
    /**
     * Méthode observateur attribut cap
     */
    public int getCap() {return this.cap;}

    /**
     * Méthode activateur attribut fixType
     */
    public void setType(String other) {this.type = other;}
    /**
     * Méthode activateur attribut FondDeCarte
     */
    public void setFondDeCarte(FondDeCarte other) {this.fond = other;}
    /**
     * Méthode activateur attribut depart
     */
    public void setDepart(Waypoint other) {this.depart = other;}
    /**
     * Méthode activateur attribut arrivee
     */
    public void setArrivee(Waypoint other) {this.arrivee = other;}
    /**
     * Méthode activateur attribut tangence
     */
    public void setTangence(Waypoint other) {this.tangence = other;}
    /**
     * Méthode activateur attribut cap
     */
    public void setCap(int other) {this.cap = other;}

    /**
     * Affichage du Leg TF au bon endroit sur la map, indépendamment de la valeur du zoom. Méthode auxiliaire pour la méthode zoomEtDeplacement (FondDeCarte).
     * Remarque : méthode qui peut être utilisée pour les leg DF également.
     */
    public void afficherLegTF()
    {
        fond.getGraphics2D().setColor(Color.red);
        fond.getGraphics2D().drawLine((int)((depart.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)),
            (int)((depart.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)),
            (int)((arrivee.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)),
            (int)((arrivee.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)));
        fond.getImageN6K().repaint();     
    }

    /**
     * Affichage du Leg CF au bon endroit sur la map, indépendamment de la valeur du zoom. Méthode auxiliaire pour la méthode zoomEtDeplacement (FondDeCarte) 
     */
    public void afficherLegCF()
    {
        int angle = 0;
        if (cap > 270)
        {
            angle = 450-cap;
            //Tracé du leg CF
            int x_int = (int)((arrivee.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)-200*Math.cos(Math.toRadians(angle)));
            int y_int = (int)((arrivee.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)+200*Math.sin(Math.toRadians(angle)));
            fond.getGraphics2D().setColor(Color.red);
            fond.getGraphics2D().drawLine(x_int,y_int,
                (int)((arrivee.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)),
                (int)((arrivee.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)));
            fond.getImageN6K().repaint();
        }
        else if (cap < 90)
        {
            angle = 90-cap;
            //Tracé du leg CF
            int x_int = (int)((arrivee.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)-200*Math.cos(Math.toRadians(angle)));
            int y_int = (int)((arrivee.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)+200*Math.sin(Math.toRadians(angle)));
            fond.getGraphics2D().setColor(Color.red);
            fond.getGraphics2D().drawLine(x_int,y_int,
                (int)((arrivee.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)),
                (int)((arrivee.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)));
            fond.getImageN6K().repaint();
        }
        else 
        {  
            angle = 270-cap;
            //Tracé du leg CF
            int x_int = (int)((arrivee.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)+200*Math.cos(Math.toRadians(angle)));
            int y_int = (int)((arrivee.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)-200*Math.sin(Math.toRadians(angle)));
            fond.getGraphics2D().setColor(Color.red);
            fond.getGraphics2D().drawLine(x_int,y_int,
                (int)((arrivee.getLongitude()-fond.getOrigine().getX())*fond.getZoom()*fond.getImageN6K().getWidth()/fond.getImage().getWidth(null)),
                (int)((arrivee.getLatitude()-fond.getOrigine().getY())*fond.getZoom()*fond.getImageN6K().getHeight()/fond.getImage().getHeight(null)));
            fond.getImageN6K().repaint();
        }
    }
}

