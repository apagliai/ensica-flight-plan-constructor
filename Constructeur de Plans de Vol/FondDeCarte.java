import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.*;
/**
 * POUR PLUS D'INFORMATIONS, CONSULTER LE FICHIER README.
 * 
 * Permet de charger la carte aeronautique de son choix et de l'afficher.
 * Cette classe contient toutes les méthodes relatives au fond de carte : zoom, déplacements sur la carte.
 * 
 * @author Anthony Pagliai & Colin Mourard 
 * @version 1.0 - 29.12.2013
 */
public class FondDeCarte
{

    private ImageN6K dessin;
    private Image map;
    private Graphics2D g;
    private double zoom;
    private Waypoint [] tabWaypoint; //Le tableau qui contiendra tous les waypoints chargés pour un fond de carte, utile pour le réaffichage après zoom par exemple.
    private Leg [] tabLeg;           //Le tableau qui contiendra tous les legs chargés pour un fond de carte, également utile pour le réaffichage.
    private Point origine;           //Le coin haut gauche de ce qui est affiché : (0,0) de "dessin" correspond à un point de "map", ce point est "origine"

    /**
     * Constructeur "test" qui affiche une carte déjà définie.
     */
    public FondDeCarte()
    {
        zoom = 1;
        dessin = new ImageN6K("TEST",800,600);
        map = dessin.loadImage("test.jpg");
        g = (Graphics2D)dessin.getGraphics();
        tabWaypoint = new Waypoint[1000]; //Je suppose que je me limite à 1000 waypoints pour simplifier le remplissage du tableau,
        //ce qui est déjà considérable pour un même fond de carte.
        tabLeg = new Leg[1000];  //De même, s'il n'y a que 1000 waypoints, il ne peut pas y avoir plus de 1000 legs ...
        origine = new Point(0,0);

        //Affichage de la carte test.
        g.drawImage(map,0,0,dessin.getWidth(),dessin.getHeight(),0,0,dessin.getWidth(),dessin.getHeight(),null);

        //Affichage de la valeur du zoom sur la carte dans le coin en haut à gauche.
        Font fonte = new Font("Calibri", Font.BOLD, 30);
        g.setFont(fonte);
        g.setColor(Color.blue);
        g.drawString("Zoom x"+this.zoom, 50,50);
        dessin.repaint();
    }

    /**
     * Constructeur qui affiche n'importe quelle carte dans une fenêtre de taille 1300x760.
     * 
     * @param name - le nom du fichier image à charger, avec l'extension et les guillemets.
     * @param title - le nom de la fenêtre graphique avec les guillemets.
     * @param zoom - le zoom avec lequel on affiche l'image. Zoom x1 pour adapter la taille de l'image à la taille de l'écran.
     */
    public FondDeCarte(String name, String title, double zoom)
    {
        this.zoom = zoom;
        dessin = new ImageN6K(title,1300,760);
        map = dessin.loadImage(name);
        g = (Graphics2D)dessin.getGraphics();
        tabWaypoint = new Waypoint[1000];
        tabLeg = new Leg[1000];
        origine = new Point(0,0);

        //Affichage de la carte chargée.
        g.drawImage(map,0,0,dessin.getWidth(),dessin.getHeight(),0,0,(int)(map.getWidth(null)/zoom),(int)(map.getHeight(null)/zoom),null);

        //Affichage de la valeur du zoom sur la carte dans le coin en haut à gauche.
        Font fonte = new Font("Calibri", Font.BOLD, 30);
        g.setFont(fonte);
        g.setColor(Color.blue);
        g.drawString("Zoom x"+this.zoom, 50,50);
        dessin.repaint();
    }

    /**
     * Méthode observateur attribut ImageN6K
     */
    public ImageN6K getImageN6K() {return this.dessin;}
    /**
     * Méthode observateur attribut Image
     */
    public Image getImage() {return this.map;}
    /**
     * Méthode observateur attribut Graphics2D
     */
    public Graphics2D getGraphics2D() {return this.g;}
    /**
     * Méthode observateur attribut double zoom
     */
    public double getZoom() {return this.zoom;}
    /**
     * Méthode observateur attribut tableau de waypoints
     */
    public Waypoint [] getTabWaypoint() {return this.tabWaypoint;}
    /**
     * Méthode observateur attribut tableau de legs
     */
    public Leg [] getTabLeg() {return this.tabLeg;}
    /**
     * Méthode observateur attribut origine
     */
    public Point getOrigine() {return this.origine;}

    /**
     * Méthode activateur attribut dessin
     */
    public void setImageN6K(ImageN6K other) {dessin = other;}
    /**
     * Méthode activateur attribut map
     */
    public void setImage(Image other) {map = other;}
    /**
     * Méthode activateur attribut Graphics2D
     */
    public void setGraphics2D(Graphics2D other) {g = other;}
    /**
     * Méthode activateur attribut zoom
     */
    public void setZoom(int other) {zoom = other;}
    /**
     * Méthode activateur attribut tableau de Waypoint
     */
    public void setTabWaypoint(Waypoint[] other) {tabWaypoint = other;}
    /**
     * Méthode activateur attribut tableau de Leg
     */
    public void setTabLeg(Leg[] other) {tabLeg = other;}
    /**
     * Méthode activateur attribut origine
     * 
     * @param point - la nouvelle origine
     */
    public void setOrigine(Point point) {origine = new Point((int)point.getX(),(int)point.getY());}

    /**
     * Vérifie qu'un waypoint appartient à la fenêtre d'affichage. //C'est plutôt une méthode auxiliaire...
     * 
     * @param waypoint - le point pour lequel on veut faire cette vérification
     * @return un booléen, true s'il appartient à la fenêtre d'affichage et false dans le cas contraire
     */
    public boolean appartient(Waypoint waypoint)
    {
        if (waypoint.getLongitude()>(int)origine.getX() && waypoint.getLongitude()<(int)(origine.getX()+map.getWidth(null)/zoom)
        && waypoint.getLatitude()>(int)origine.getY() && waypoint.getLatitude()<(int)(origine.getY()+map.getHeight(null)/zoom)) {return true;}
        else {return false;}
    }

    /**
     * Calcule la distance au carré entre le dernier clic souris et un waypoint passé en paramètre.
     * Cette méthode sert d'auxiliaire pour la classe Leg.
     */
    public int distanceCarre(Waypoint waypoint)
    {
        return (int)(Math.pow((dessin.mouseX()-((waypoint.getLongitude()-origine.getX())*zoom*dessin.getWidth()/map.getWidth(null))),2)
            +Math.pow((dessin.mouseY()-((waypoint.getLatitude()-origine.getY())*zoom*dessin.getHeight()/map.getHeight(null))),2));
    }

    /**
     * Zoomer sur le fond de carte actuel par rapport au point central de la fenêtre d'affichage en cours.
     * 
     * @param zoomfinal - le zoom final du fond de carte.
     */
    public void zoomer(double zoomfinal)
    {
        //Calcul du centre de zoom, c'est-à-dire du point central de ce qui est affiché, pour zoomer autour de ce centre de zoom.
        Point centre = new Point((int)((2*origine.getX()+map.getWidth(null)/zoom)/2),
                (int)((2*origine.getY()+map.getHeight(null)/zoom)/2));

        //On remplace l'attribut zoom par la nouvelle valeur du zoom.
        this.zoom = zoomfinal;

        //Affichage du fond de carte zoomé de la valeur zoomfinal.

        //Au préalable, je repeinds le fond en noir, juste au cas où ... Pour éviter un risque de superposition de fonds de cartes.
        g.setBackground(Color.black);
        g.clearRect(0,0,dessin.getWidth(),dessin.getHeight());

        g.drawImage(map,0,0,dessin.getWidth(),dessin.getHeight(),
            (int)(centre.getX()-map.getWidth(null)/2/zoom),
            (int)(centre.getY()-map.getHeight(null)/2/zoom),
            (int)(centre.getX()+map.getWidth(null)/2/zoom),
            (int)(centre.getY()+map.getHeight(null)/2/zoom),
            null);

        //Ecriture dans l'attribut "origine" de la nouvelle origine qui est le coin gauche de ce qui est zoomé.
        this.origine = new Point((int)((centre.getX()-map.getWidth(null)/2/zoom)),
            (int)((centre.getY()-map.getHeight(null)/2/zoom)));

        //Affichage de la nouvelle valeur du zoom
        g.setColor(Color.blue);
        g.drawString("Zoom x"+this.zoom, 50,50);
        dessin.repaint();

        //Affichage de tous les waypoints qui étaient déjà affichés précédemment.
        for (int i=0; i<tabWaypoint.length; i++)
        {
            if (tabWaypoint[i] != null && tabWaypoint[i].getName() != "DepartDF" && appartient(tabWaypoint[i]) == true) 
            {
                tabWaypoint[i].afficherWaypoint();
            }
        }

        //Affichage de tous les legs qui étaient déjà affichés précédemment.
        for (int i=0; i<tabLeg.length; i++)
        {
            if (tabLeg[i] != null)
            {
                if (tabLeg[i].getType() == "TF" || tabLeg[i].getType() == "DF") 
                {
                    tabLeg[i].afficherLegTF();
                }
                else if (tabLeg[i].getType() == "CF")
                {
                    tabLeg[i].afficherLegCF();
                }
            }
        }
    }

    /**
     * Permet de gérer les différentes opérations de zoom et de déplacement sur le fond de carte. Toutes les opérations s'effectuent au clavier. 
     * 
     * ZOOM (ou dézoomer) : par cran de + ou - 0,1. Pour zoomer davantage, appuyer sur la touche 'p' du clavier (Plus) et pour dézoomer,
     * appuyer sur la touche 'm' (Moins). Si la touche est maintenue enfoncée, le zoom fonctionne aussi longtemps que l'utilisateur laisse le doigt sur la touche.
     * 
     * DEPLACEMENT : à zoom constant. Pour se déplacer, on utilise les touches du clavier  : 'd' pour droite, 'q' pour gauche, 's' pour bas et 'z' pour haut. 
     * De même, l'appui long fonctionne avec le déplacement.
     * 
     * ARRÊT : On appuie sur la touche 't' (Terminer)
     */
    public void deplacementEtZoomClavier()
    {
        char touche = 'o'; //Caractère ayant peu d'importance, il me permet juste de rentrer dans ma boucle while.
        while (touche != 't')
        {
            touche = dessin.waitChar();

            /**
             * Zoom ou dézoom progressif par tranche de + ou - 0,1.
             */
            if (touche == 'p')
            {
                //Calcul du centre de zoom, c'est-à-dire du point central de ce qui est affiché, pour zoomer autour de ce centre de zoom.
                Point centre = new Point((int)((2*origine.getX()+map.getWidth(null)/zoom)/2),
                        (int)((2*origine.getY()+map.getHeight(null)/zoom)/2));

                //On remplace l'attribut zoom par la nouvelle valeur du zoom.
                this.zoom = zoom+0.1;

                //Affichage du fond de carte zoomé de la valeur zoomfinal.

                //Au préalable, je repeinds le fond en noir, juste au cas où ... Pour éviter un risque de superposition de fonds de cartes.
                g.setBackground(Color.black);
                g.clearRect(0,0,dessin.getWidth(),dessin.getHeight());

                g.drawImage(map,0,0,dessin.getWidth(),dessin.getHeight(),
                    (int)(centre.getX()-map.getWidth(null)/2/zoom),
                    (int)(centre.getY()-map.getHeight(null)/2/zoom),
                    (int)(centre.getX()+map.getWidth(null)/2/zoom),
                    (int)(centre.getY()+map.getHeight(null)/2/zoom),
                    null);

                //Ecriture dans l'attribut "origine" de la nouvelle origine qui est le coin gauche de ce qui est zoomé.
                this.origine = new Point((int)((centre.getX()-map.getWidth(null)/2/zoom)),
                    (int)((centre.getY()-map.getHeight(null)/2/zoom)));

                //Affichage de la nouvelle valeur du zoom
                g.setColor(Color.blue);
                g.drawString("Zoom x"+(double)(int)(this.zoom*10)/10, 50,50);
                dessin.repaint();

                //Affichage de tous les waypoints qui étaient déjà affichés précédemment.
                for (int i=0; i<tabWaypoint.length; i++)
                {
                    if (tabWaypoint[i] != null && tabWaypoint[i].getName() != "DepartDF" && appartient(tabWaypoint[i]) == true) 
                    {
                        tabWaypoint[i].afficherWaypoint();
                    }
                }

                //Affichage de tous les legs qui étaient déjà affichés précédemment.
                for (int i=0; i<tabLeg.length; i++)
                {
                    if (tabLeg[i] != null)
                    {
                        if (tabLeg[i].getType() == "TF" || tabLeg[i].getType() == "DF") 
                        {
                            tabLeg[i].afficherLegTF();
                        }
                        else if (tabLeg[i].getType() == "CF")
                        {
                            tabLeg[i].afficherLegCF();
                        }
                    }
                }
            }
            else if (touche == 'm')
            {
                //Calcul du centre de zoom, c'est-à-dire du point central de ce qui est affiché, pour zoomer autour de ce centre de zoom.
                Point centre = new Point((int)((2*origine.getX()+map.getWidth(null)/zoom)/2),
                        (int)((2*origine.getY()+map.getHeight(null)/zoom)/2));

                //On remplace l'attribut zoom par la nouvelle valeur du zoom.
                this.zoom = zoom-0.1;

                //Affichage du fond de carte zoomé de la valeur zoomfinal.

                //Au préalable, je repeinds le fond en noir, juste au cas où ... Pour éviter un risque de superposition de fonds de cartes.
                g.setBackground(Color.black);
                g.clearRect(0,0,dessin.getWidth(),dessin.getHeight());

                g.drawImage(map,0,0,dessin.getWidth(),dessin.getHeight(),
                    (int)(centre.getX()-map.getWidth(null)/2/zoom),
                    (int)(centre.getY()-map.getHeight(null)/2/zoom),
                    (int)(centre.getX()+map.getWidth(null)/2/zoom),
                    (int)(centre.getY()+map.getHeight(null)/2/zoom),
                    null);

                //Ecriture dans l'attribut "origine" de la nouvelle origine qui est le coin gauche de ce qui est zoomé.
                this.origine = new Point((int)((centre.getX()-map.getWidth(null)/2/zoom)),
                    (int)((centre.getY()-map.getHeight(null)/2/zoom)));

                //Affichage de la nouvelle valeur du zoom
                g.setColor(Color.blue);
                g.drawString("Zoom x"+(double)(int)(this.zoom*10)/10, 50,50);
                dessin.repaint();

                //Affichage de tous les waypoints qui étaient déjà affichés précédemment.
                for (int i=0; i<tabWaypoint.length; i++)
                {
                    if (tabWaypoint[i] != null && tabWaypoint[i].getName() != "DepartDF" && appartient(tabWaypoint[i]) == true) 
                    {
                        tabWaypoint[i].afficherWaypoint();
                    }
                }

                //Affichage de tous les legs qui étaient déjà affichés précédemment.
                for (int i=0; i<tabLeg.length; i++)
                {
                    if (tabLeg[i] != null)
                    {
                        if (tabLeg[i].getType() == "TF" || tabLeg[i].getType() == "DF") 
                        {
                            tabLeg[i].afficherLegTF();
                        }
                        else if (tabLeg[i].getType() == "CF")
                        {
                            tabLeg[i].afficherLegCF();
                        }
                    }
                }
            }

            /**
             * Déplacer la vue sur la carte, à zoom constant. Pour se déplacer, on utilise les touches du clavier (après avoir cliqué sur la fenêtre graphique pour en faire
             * la fenêtre active) : 'd' pour droite, 'q' pour gauche, 's' pour bas et 'z' pour haut.
             * La méthode boucle infiniment. Appuyer sur la touche 't' pour terminer.
             */

            //Déplacement de la carte vers la droite, avec la touche 'd' du clavier
            else if (touche == 'd')
            {
                //Au préalable, je repeinds le fond en noir, juste au cas où ... Pour éviter un risque de superposition de fonds de cartes.
                g.setBackground(Color.black);
                g.clearRect(0,0,dessin.getWidth(),dessin.getHeight());

                //Affichage du fond de carte décalé de 100 pixels sur la droite.
                g.drawImage(map,0,0,dessin.getWidth(),dessin.getHeight(),
                    (int)(origine.getX()+(100*map.getWidth(null)/zoom/dessin.getWidth())),(int)origine.getY(),
                    (int)(origine.getX()+(map.getWidth(null)/zoom)+(100*map.getWidth(null)/zoom/dessin.getWidth())),
                    (int)(origine.getY()+(map.getHeight(null)/zoom)),null);

                //Ecriture de la nouvelle origine translatée de 100 pixels vers la droite,à convertir à l'échelle de la résolution du fond de carte.
                this.origine = new Point((int)(origine.getX()+(100*map.getWidth(null)/zoom/dessin.getWidth())),(int)origine.getY());

                //Réaffichage de la valeur du zoom
                g.setColor(Color.blue);
                g.drawString("Zoom x"+(double)(int)(this.zoom*10)/10, 50,50);
                dessin.repaint();

                //Affichage de tous les waypoints qui étaient déjà affichés précédemment.
                for (int i=0; i<tabWaypoint.length; i++)
                {
                    if (tabWaypoint[i] != null && tabWaypoint[i].getName() != "DepartDF" && appartient(tabWaypoint[i]) == true) 
                    {
                        tabWaypoint[i].afficherWaypoint();
                    }
                }

                //Affichage de tous les legs qui étaient déjà affichés précédemment.
                for (int i=0; i<tabLeg.length; i++)
                {
                    if (tabLeg[i] != null)
                    {
                        if (tabLeg[i].getType() == "TF" || tabLeg[i].getType() == "DF") 
                        {
                            tabLeg[i].afficherLegTF();
                        }
                        else if (tabLeg[i].getType() == "CF")
                        {
                            tabLeg[i].afficherLegCF();
                        }
                    }
                }
            }

            //Déplacement de la carte vers la gauche, avec la touche 'q' du clavier
            else if (touche == 'q')
            {
                //Au préalable, je repeinds le fond en noir, juste au cas où ... Pour éviter un risque de superposition de fonds de cartes.
                g.setBackground(Color.black);
                g.clearRect(0,0,dessin.getWidth(),dessin.getHeight());

                //Affichage du fond de carte décalé de 100 pixels sur la droite.
                g.drawImage(map,0,0,dessin.getWidth(),dessin.getHeight(),
                    (int)(origine.getX()-(100*map.getWidth(null)/zoom/dessin.getWidth())),(int)origine.getY(),
                    (int)(origine.getX()+(map.getWidth(null)/zoom)-(100*map.getWidth(null)/zoom/dessin.getWidth())),
                    (int)(origine.getY()+(map.getHeight(null)/zoom)),null);

                //Ecriture de la nouvelle origine translatée de 100 pixels vers la droite.
                this.origine = new Point((int)(origine.getX()-(100*map.getWidth(null)/zoom/dessin.getWidth())),(int)origine.getY());

                //Réaffichage de la valeur du zoom
                g.setColor(Color.blue);
                g.drawString("Zoom x"+(double)(int)(this.zoom*10)/10, 50,50);
                dessin.repaint();

                //Affichage de tous les waypoints qui étaient déjà affichés précédemment.
                for (int i=0; i<tabWaypoint.length; i++)
                {
                    if (tabWaypoint[i] != null && tabWaypoint[i].getName() != "DepartDF" && appartient(tabWaypoint[i]) == true) 
                    {
                        tabWaypoint[i].afficherWaypoint();
                    }
                }

                //Affichage de tous les legs qui étaient déjà affichés précédemment.
                for (int i=0; i<tabLeg.length; i++)
                {
                    if (tabLeg[i] != null)
                    {
                        if (tabLeg[i].getType() == "TF" || tabLeg[i].getType() == "DF") 
                        {
                            tabLeg[i].afficherLegTF();
                        }
                        else if (tabLeg[i].getType() == "CF")
                        {
                            tabLeg[i].afficherLegCF();
                        }
                    }
                }
            }

            //Déplacement de la carte vers le bas, avec la touche 's' du clavier
            else if (touche == 's')
            {
                //Au préalable, je repeinds le fond en noir, juste au cas où ... Pour éviter un risque de superposition de fonds de cartes.
                g.setBackground(Color.black);
                g.clearRect(0,0,dessin.getWidth(),dessin.getHeight());

                //Affichage du fond de carte décalé de 100 pixels vers le bas.
                g.drawImage(map,0,0,dessin.getWidth(),dessin.getHeight(),
                    (int)origine.getX(),(int)(origine.getY()+(100*map.getHeight(null)/zoom/dessin.getHeight())),
                    (int)(origine.getX()+(map.getWidth(null)/zoom)),
                    (int)(origine.getY()+(map.getHeight(null)/zoom)+(100*map.getHeight(null)/zoom/dessin.getHeight())),null);

                //Ecriture de la nouvelle origine translatée de 100 pixels vers le bas.
                this.origine = new Point((int)origine.getX(),(int)(origine.getY()+(100*map.getHeight(null)/zoom/dessin.getHeight())));

                //Réaffichage de la valeur du zoom
                g.setColor(Color.blue);
                g.drawString("Zoom x"+(double)(int)(this.zoom*10)/10, 50,50);
                dessin.repaint();

                //Affichage de tous les waypoints qui étaient déjà affichés précédemment.
                for (int i=0; i<tabWaypoint.length; i++)
                {
                    if (tabWaypoint[i] != null && tabWaypoint[i].getName() != "DepartDF" && appartient(tabWaypoint[i]) == true) 
                    {
                        tabWaypoint[i].afficherWaypoint();
                    }
                }

                //Affichage de tous les legs qui étaient déjà affichés précédemment.
                for (int i=0; i<tabLeg.length; i++)
                {
                    if (tabLeg[i] != null)
                    {
                        if (tabLeg[i].getType() == "TF" || tabLeg[i].getType() == "DF") 
                        {
                            tabLeg[i].afficherLegTF();
                        }
                        else if (tabLeg[i].getType() == "CF")
                        {
                            tabLeg[i].afficherLegCF();
                        }
                    }
                }
            }

            //Déplacement de la carte vers le haut, avec la touche 'z' du clavier
            else if (touche == 'z')
            {
                //Au préalable, je repeinds le fond en noir, juste au cas où ... Pour éviter un risque de superposition de fonds de cartes.
                g.setBackground(Color.black);
                g.clearRect(0,0,dessin.getWidth(),dessin.getHeight());

                //Affichage du fond de carte décalé de 100 pixels vers le haut.
                g.drawImage(map,0,0,dessin.getWidth(),dessin.getHeight(),
                    (int)origine.getX(),(int)(origine.getY()-(100*map.getHeight(null)/zoom/dessin.getHeight())),
                    (int)(origine.getX()+(map.getWidth(null)/zoom)),
                    (int)(origine.getY()+(map.getHeight(null)/zoom)-(100*map.getHeight(null)/zoom/dessin.getHeight())),null);

                //Ecriture de la nouvelle origine translatée de 100 pixels vers le haut.
                this.origine = new Point((int)origine.getX(),(int)(origine.getY()-(100*map.getHeight(null)/zoom/dessin.getHeight())));

                //Réaffichage de la valeur du zoom
                g.setColor(Color.blue);
                g.drawString("Zoom x"+(double)(int)(this.zoom*10)/10, 50,50);
                dessin.repaint();

                //Affichage de tous les waypoints qui étaient déjà affichés précédemment.
                for (int i=0; i<tabWaypoint.length; i++)
                {
                    if (tabWaypoint[i] != null && tabWaypoint[i].getName() != "DepartDF" && appartient(tabWaypoint[i]) == true) 
                    {
                        tabWaypoint[i].afficherWaypoint();
                    }
                }

                //Affichage de tous les legs qui étaient déjà affichés précédemment.
                for (int i=0; i<tabLeg.length; i++)
                {
                    if (tabLeg[i] != null)
                    {
                        if (tabLeg[i].getType() == "TF" || tabLeg[i].getType() == "DF") 
                        {
                            tabLeg[i].afficherLegTF();
                        }
                        else if (tabLeg[i].getType() == "CF")
                        {
                            tabLeg[i].afficherLegCF();
                        }
                    }
                }
            }
        }
    }

    /**
     * Supprimer un leg du tableau de Legs. Cette méthode permet aussi de combler le trou ainsi créé.
     * 
     * @param leg - le leg que l'on souhaite supprimer du tableau
     */
    public void removeLeg(Leg leg)
    {
        //Calcul de la position du leg à supprimer.
        int positionRemove = 0;
        for (int i=0; i<tabLeg.length; i++)
        {
            if (leg == tabLeg[i]) {positionRemove = i;}
        }

        //Suppression du leg, et fermeture du trou ainsi créé dans le tableau de Legs.
        tabLeg[positionRemove] = null;
        for (int i=positionRemove; i<tabLeg.length-1; i++)
        {
            tabLeg[i] = tabLeg[i+1];
        }
    }

    /**
     * Supprimer un waypoint existant par clic souris. La méthode recalcule aussi la nouvelle trajectoire (si possible...)
     */
    public void removeWaypoint()
    {
        //Sélection du waypoint à supprimer par clic.
        System.out.println("Veuillez cliquer sur le WayPoint à supprimer");
        Waypoint remove = null;
        int positionRemove = 0;   //Identifier à quelle endroit se trouve le waypoint à supprimer dans le tableau.
        boolean detection = false;  //Permet de savoir si un waypoint a été détecté par le clic.
        while (detection != true)
        {
            int clic = dessin.waitClick(new Point());
            if (clic == 1)
            {
                for (int i=0; i<tabWaypoint.length && remove == null; i++)
                {
                    if (tabWaypoint[i] != null)
                    {
                        if (this.distanceCarre(tabWaypoint[i]) < 400) //Clic détecté sur tout le cercle de rayon 20 pixels, de centre le centre du WayPoint
                        {
                            remove = tabWaypoint[i];
                            positionRemove = i;
                            detection = true;
                        }
                    }
                }
                if (remove == null) {System.out.println("Pas de WayPoint détecté. Veuillez réessayer de cliquer sur le Waypoint à supprimer");}
            }
        }

        //Suppression du waypoint dans le tableau de Waypoint, et réorganisation dans le tableau de waypoints pour ne pas laisser une case vide au milieu.
        tabWaypoint[positionRemove] = null;
        for (int i=positionRemove; i<tabWaypoint.length-1; i++)
        {
            tabWaypoint[i] = tabWaypoint[i+1];
        }

        //Calcul de la trajectoire actualisée.
        for (int i=0; i<tabLeg.length; i++)
        {
            if (tabLeg[i] != null)
            {
                if (tabLeg[i].getArrivee() == remove)
                {
                    if (tabLeg[i].getType() == "DF" || tabLeg[i].getType() == "CF") {this.removeLeg(tabLeg[i]);}
                    else if (tabLeg[i].getType() == "TF") 
                    {
                        if (tabLeg[i].getTangence() != null) 
                        {
                            tabLeg[i].setArrivee(tabLeg[i].getTangence());
                            tabLeg[i].setTangence(null);
                        }
                        else 
                        {
                            //Sélection du nouveau waypoint final par clic.
                            System.out.println("Veuillez cliquer sur le nouveau WayPoint final");
                            detection = false;  
                            while (detection != true)
                            {
                                int clic = dessin.waitClick(new Point());
                                if (clic == 1)
                                {
                                    tabLeg[i].setArrivee(null);
                                    for (int k=0; k<tabWaypoint.length && tabLeg[i].getArrivee() == null; k++)
                                    {
                                        if (tabWaypoint[k] != null)
                                        {
                                            if (this.distanceCarre(tabWaypoint[k]) < 400) //Clic détecté sur tout le cercle de rayon 20 pixels, de centre le centre du WayPoint
                                            {
                                                tabLeg[i].setArrivee(tabWaypoint[k]);
                                                detection = true;
                                            }
                                        }
                                    }

                                    if (tabLeg[i].getArrivee() == null) {System.out.println("Pas de WayPoint détecté. Veuillez réessayer de cliquer sur le Waypoint final");}
                                }
                            }
                        }
                    }
                }
                
                else if (tabLeg[i].getDepart() == remove) {this.removeLeg(tabLeg[i]);}
            }
        }

        //Affichage du fond de carte. Pour que le waypoint se supprime, on doit tout redessiner...
        g.drawImage(map,0,0,dessin.getWidth(),dessin.getHeight(),
                    (int)origine.getX(),(int)origine.getY(),
                    (int)(origine.getX()+(map.getWidth(null)/zoom)),
                    (int)(origine.getY()+(map.getHeight(null)/zoom)),null);
                    
        //Réaffichage de la valeur du zoom
        g.setColor(Color.blue);
        g.drawString("Zoom x"+(double)(int)(this.zoom*10)/10, 50,50);
        dessin.repaint();
        
        //Affichage de tous les waypoints qui étaient déjà affichés précédemment.
        for (int i=0; i<tabWaypoint.length; i++)
        {
            if (tabWaypoint[i] != null && tabWaypoint[i].getName() != "DepartDF" && appartient(tabWaypoint[i]) == true) 
            {
                tabWaypoint[i].afficherWaypoint();
            }
        }

        //Affichage de tous les legs qui étaient déjà affichés précédemment.
        for (int i=0; i<tabLeg.length; i++)
        {
            if (tabLeg[i] != null)
            {
                if (tabLeg[i].getType() == "TF" || tabLeg[i].getType() == "DF") 
                {
                    tabLeg[i].afficherLegTF();
                }
                else if (tabLeg[i].getType() == "CF")
                {
                    tabLeg[i].afficherLegCF();
                }
            }
        }
    }
}
