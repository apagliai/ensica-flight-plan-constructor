import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * ImageN6K est une classe permettant de developper des applications graphiques tres simplement
 * <P>
 * Seulement trois etapes suffisent pour dessiner :
 * <P>
 * 1. creer un objet ImageN6K
 * <P>
 * 2. Changer la couleur des points de l'image, avec la methode setColor.
 * <P>
 * 3. demander l'affichage de l'image avec la methode <B>repaint</B>
 * 
 * <P>
 * <P>
 * NB: Une application peut creer plusieurs ImageN6K.
 * Et en appelant la methode getGraphics(), on obtient un objet Graphics2D qui permet 
 * de faire du dessin vectoriel (API Java2D).
 * 
 * @author F.Frances
 * @version V5 Sep 2011
 */
public class ImageN6K extends BufferedImage
{
    private BufferedImage buffer;
    private Graphics2D graphics;
    private Frame frame;
    private CanvasN6K canvas;
    private MediaTracker mediaTracker;
    private MouseListenerN6K mouseListener;
    private KeyListenerN6K keyListener;
    private boolean closed;

    /** Constante caractere renvoyee par getChar si aucune touche n'a ete tapee */
  public static final char NO_CHAR=KeyEvent.CHAR_UNDEFINED;

  /** Construit une fenetre graphique de taille 800x600, sans titre.*/
  public ImageN6K() { this(800,600); }
  
  /** Construit une fenetre graphique sans titre.
   * @param width,height Les dimensions de l'image
   */
  public ImageN6K(int width, int height) { this("Untitled",width,height); }

  /** Construit une fenetre graphique.
   * @param Title Le titre s'affiche dans le bandeau de la fenetre.
   * @param width,height Les dimensions de l'image
   */
  public ImageN6K(String title,int width, int height) {
    super(width,height,BufferedImage.TYPE_INT_RGB);
    buffer = this;
// attention, l'ordre des appels est important
    frame = new Frame(title);
    canvas = new CanvasN6K();
    canvas.setSize(width,height);
    
    frame.add(canvas);
    frame.addWindowListener(new WindowListenerN6K());
    frame.pack();
    frame.setVisible(true);

    mediaTracker=new MediaTracker(canvas);
    mouseListener=new MouseListenerN6K();
    keyListener=new KeyListenerN6K();
    canvas.addMouseListener(mouseListener);
    canvas.addMouseMotionListener(mouseListener);
    canvas.addMouseWheelListener(mouseListener);
    canvas.addKeyListener(keyListener);

    pause(1000);
  }
  
  /**
   * Recupere la hauteur de la fenetre graphique.
   * @return la hauteur utile en nombre de pixels
   */
  public int getHeight() { return super.getHeight(); }
  
  /**
   * Recupere la largeur de la fenetre graphique.
   * @return la largeur utile en nombre de pixels
   */
  public int getWidth() { return super.getWidth(); }

  public void setTitle(String title) { frame.setTitle(title); }

  /** Demande la mise a jour du dessin */
  public void repaint() { canvas.repaint(); }
  
  /** Fait une pause (par exemple pour des animations)
   * @param millisecondes la duree de la pause en millisecondes.
   */
  public void pause(long millisecondes) {
    try {Thread.sleep(millisecondes); }
    catch (InterruptedException e) {}
  }      

  /**
   * Colorie le point de coordonnees (x,y) avec la couleur specifiee.
   * @param x la coordonnee x
   * @param y la coordonnee y
   * @param couleur l'entier specifiant la couleur, selon le format 0xRRGGBB
   */
  public void setRGB(int x, int y, int couleur) { super.setRGB(x,y,couleur); }

  /**
   * Colorie le point de coordonnees (x,y) avec la teinte specifiee.
   * (la saturation et la brillance sont fixees au maximum).
   * @param couleur la teinte de la couleur. 
   * Cette valeur multipliee par 2Pi donne l'angle de la teinte sur le cercle des couleurs.
   * Quelques valeurs : rouge=0, jaune=1/6, vert=2/6, cyan=3/6, bleu=4/6, magenta=5/6.
   */
  public void setColor(int x, int y, double couleur) { setHSB(x,y,(float)couleur,1,1); }
  
  /**
   * Colorie le point de coordonnees (x,y) avec la couleur specifiee.
   * @param x la coordonnee x
   * @param y la coordonnee y
   * @param teinte,saturation,brillance les composantes specifiant la couleur
   */
  public void setHSB(int x, int y, float teinte, float saturation, float brillance)
  {
    setRGB(x,y,Color.HSBtoRGB(teinte,saturation,brillance)); 
  }
  
  /** Ferme la fenetre. Attention une femetre fermee ne peut plus ouverte ensuite. */
  public void close() { frame.dispose(); }
  /** Dit si la fenetre a ete fermee. */
  public boolean isClosed() { return closed; }


  /** Emet un son bip */
  public final void beep() {
    frame.getToolkit().beep();
  }

  /** Charge une image depuis un fichier du repertoire courant, et la prepare pour l'affichage. */
  public final Image loadImage(String name) {
    Image img=frame.getToolkit().getImage(name);
    MediaTracker tracker=new MediaTracker(frame);
    tracker.addImage(img,0);
    try { mediaTracker.waitForAll();} 
    catch (InterruptedException e) {
      System.out.println("Impossible de lire l'image "+name);
      return null;
    }
    while (!frame.prepareImage(img,null))
        pause(200);
    return img;
  }

  /** Indique si la touche specifiee est enfoncee.   */
  public final boolean keyPressed(int keyCode) { return keyListener.keyPressed(keyCode); }

  /** Attend qu'un caractere soit tape au clavier.
   * @return Le caractere qui a ete presse.
   */
  public final char waitChar() { return keyListener.waitChar(); }

  /** Renvoie le dernier caractere tape au clavier.
   * Si aucun caractere n'a ete tape, renvoie le caractere NO_CHAR.
   */
  public final char getChar() { return keyListener.getChar(); }

  /** Renvoie la position X de la souris */
  public final int mouseX() { return mouseListener.mouseX(); }
  /** Renvoie la position Y de la souris */
  public final int mouseY() { return mouseListener.mouseY(); }
  /** Renvoie la rotation de la MouseWheel.
   * Zero pour la position initiale, chaque pas incremente ou decremente de un la rotation.
   */
  public final int mouseWheelRotation() { return mouseListener.wheelRotation(); }
  /** Remet a zero la position de la MouseWheel */
  public final void mouseWheelReset() { mouseListener.wheelReset(); }
  /** Indique si un bouton de la souris est enfonce */
  public final boolean mouseDown() { return mouseListener.mouseDown(); }
  /** Attend que l'utilisateur fasse un clic souris.
   * Modifie les attributs x et y du Point passe en parametre (s'il est non null).
   * @param position le Point ou l'utilisateur a clique.
   * @return le numero du bouton qui a ete clique (1, 2 ou 3), ou 0 si la fenetre a ete fermee.
   */
  public final int waitClick(Point position) { return mouseListener.waitClick(position); }

  
  /* La partie cachee avec la classe interne CanvasN6K, et les differents listeners */
  
  private class CanvasN6K extends Canvas
  {
      public void update(Graphics g)
      {
          if (buffer!=null)
            g.drawImage(buffer,0,0,this);
      }
        
      public void paint(Graphics g) { update(g); }
  }
  
  private class KeyListenerN6K implements KeyListener {

       private static final int KEYBOARD_SIZE=256; // seules certaines touches sont utilisees
       private boolean[] keyboard;

       private volatile char entry=KeyEvent.CHAR_UNDEFINED;

       public KeyListenerN6K() {
         keyboard=new boolean[KEYBOARD_SIZE];
         for (int i=0; i<KEYBOARD_SIZE; i++)
            keyboard[i]=false;
       }

       public final void keyPressed(KeyEvent e) {
         keyboard[e.getKeyCode()]=true;
       }
       public final void keyReleased(KeyEvent e) {
         keyboard[e.getKeyCode()]=false;
       }
       public final void keyTyped(KeyEvent e) {
         entry=e.getKeyChar();
         stopWait();
       }

       public final boolean keyPressed(int code) {
         return keyboard[code];
       }

       public final char getChar() {
        char result=entry;
        entry=KeyEvent.CHAR_UNDEFINED;
        return result;
       }    
    
       public final synchronized char waitChar() {
         try {
           keyListener.wait();
         } catch (InterruptedException e) {} 
         return getChar();
       }
       
       public final synchronized void stopWait() { keyListener.notifyAll(); }
  }

  private class MouseListenerN6K implements MouseListener, MouseMotionListener, MouseWheelListener {

       private int x,y,wheelRotation=0;
       private boolean down;
       private volatile int clickX, clickY, clickButton;
       private volatile boolean click;
    
       public final void mousePressed(MouseEvent e) { 
        x=e.getX(); y=e.getY(); down=true;
       }
       public final void mouseReleased(MouseEvent e) {
        x=e.getX(); y=e.getY(); down=false;
       }
       public final void mouseClicked(MouseEvent e) {
        clickX=e.getX(); clickY=e.getY(); click=true;
        switch (e.getModifiers()) {
            case MouseEvent.BUTTON1_MASK: clickButton=1; break;
            case MouseEvent.BUTTON2_MASK: clickButton=2; break;
            case MouseEvent.BUTTON3_MASK: clickButton=3; break;
            default: clickButton=0;
        }
        stopWait();
       }
       public final void mouseEntered(MouseEvent e) { x=e.getX(); y=e.getY(); }
       public final void mouseExited(MouseEvent e) { x=e.getX(); y=e.getY(); down=false; }
    
       public final void mouseDragged(MouseEvent e) { x=e.getX(); y=e.getY(); down=true; }
       public final void mouseMoved(MouseEvent e) { x=e.getX(); y=e.getY(); down=false; }
       
       public final void mouseWheelMoved(MouseWheelEvent e) { wheelRotation += e.getWheelRotation(); }
    
       public final int mouseX() { return x; }
       public final int mouseY() { return y; }
       public final int wheelRotation() { return wheelRotation; }
       public final void wheelReset() { wheelRotation = 0; }
       public final boolean mouseDown() { return down; }
       public final synchronized int waitClick(Point p) {
           try {
             mouseListener.wait();
           } catch (InterruptedException e) {}
           if (closed) return 0;
           if (p!=null) { p.x=x; p.y=y; }
           return clickButton;
       }
       public final synchronized void stopWait() { mouseListener.notifyAll(); }
  }

  private class WindowListenerN6K implements WindowListener {
      public void windowActivated(WindowEvent e) {}
      public void windowClosed(WindowEvent e) {}
      public void windowClosing(WindowEvent e) {
          closed=true;
          mouseListener.stopWait(); // debloque une attente eventuelle de clic souris
          keyListener.stopWait();  // et une attente eventuelle de touche clavier
          frame.dispose(); 
      }
      public void windowDeactivated(WindowEvent e) {}
      public void windowDeiconified(WindowEvent e) {}
      public void windowIconified(WindowEvent e) {}
      public void windowOpened(WindowEvent e) {}
  }
}