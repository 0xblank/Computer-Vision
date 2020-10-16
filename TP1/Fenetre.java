import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.*;
import javax.swing.*;

/* Definition d'une interface graphique 
   avec une zone de dessin et un bouton pour quitter */

public class Fenetre extends JFrame { // notre interface graphique

    private ZoneGraphique zoneDeDessin; // l'endroit ou on dessine
    private Button bouton; // le bouton 'bouton'

    private class GestionFenetre extends WindowAdapter {
        public synchronized void windowClosing(WindowEvent e) { // fermeture de la fenêtre
            System.exit(0); // arrêt du programme
        }
    }

    private class ActionBouton implements ActionListener { // evts du bouton
        public synchronized void actionPerformed(ActionEvent e) { // active
            action();
        }
    }

    protected void action() {
        System.exit(0); // on quitte le programme
    }

    public Fenetre(int x, int y) {
        super("Fenêtre de dessin"); // creation de la fenetre
        setBackground(new Color(255,255,255)); // couleur du fond
        setForeground(new Color(0,0,250)); // couleur de trace
        addWindowListener(new GestionFenetre()); // traitements des événements de la fenêtre
        // On cree le composant zone de dessin
        zoneDeDessin=new ZoneGraphique();
        zoneDeDessin.setBackground(getBackground()); // couleur du fond
        zoneDeDessin.setForeground(getForeground()); // couleur de trace
        // On cree le composant bouton
        bouton=new Button("Action");

        // on prepare les objets de placement des composants
        GridBagLayout placeur=new GridBagLayout(); // placeur
        GridBagConstraints contraintes=new GridBagConstraints(); // regles de placement
        getContentPane().setLayout(placeur); // associer le placeur a l'interface graphique
        contraintes.fill=GridBagConstraints.BOTH; // remplissage des cases
        contraintes.anchor=GridBagConstraints.CENTER; // occupation des cases
        contraintes.ipadx=2; contraintes.ipady=2; // marges externes
        contraintes.insets=new Insets(0,0,0,0); // marges internes

        // On va ajouter le composant de dessin
        contraintes.gridx=0; contraintes.gridy=0; // en haut
        contraintes.gridwidth=1; contraintes.gridheight=1; // sur 1 case
        contraintes.weightx=100; contraintes.weighty=100; // dimensionnement total
        placeur.setConstraints(zoneDeDessin, contraintes); // placement
        getContentPane().add(zoneDeDessin); // ajout du composant

        // On va ajouter le bouton bouton
        contraintes.gridx=0; contraintes.gridy=1; // en bas
        contraintes.gridwidth=1; contraintes.gridheight=1; // sur 1 case
        contraintes.weightx=100; contraintes.weighty=0; // dimensionnement total
        placeur.setConstraints(bouton, contraintes); // placement
        getContentPane().add(bouton); // ajout du composant
        bouton.addActionListener(new ActionBouton()); // traitement des evts
        setSize(x,y);
        setVisible(true);
    }

    public synchronized Graphics obtenirZoneGraphique() { return zoneDeDessin.obtenirGraphics(); }

    public synchronized void miseAJour() { zoneDeDessin.repaint(); }

    public Color obtenirCouleurFond() { return getBackground(); }

    public Color obtenirCouleurTrace() { return getForeground(); }

    public synchronized Image lireImage(String nom) {
        Image photo=getToolkit().getImage(nom);
        return photo;
    }

    public synchronized Image lireImage(URL nom) {
        Image photo=getToolkit().getImage(nom);
        return photo;
    }

    public synchronized Image lireEtPreparerImage(String nom) {
        Image photo=lireImage(nom);
        prepareImage(photo);
        return photo;
    }

    public synchronized Image lireEtPreparerImage(URL nom) {
        Image photo=lireImage(nom);
        prepareImage(photo);
        return photo;
    }

    public synchronized void prepareImage(Image photo) {
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(photo, 0);
        super.prepareImage(photo,this);
        try { tracker.waitForID(0); }
        catch (InterruptedException e) {}
    }

    public AudioClip lireAudioClip(String nom) {
        URL fichierSon=null; AudioClip son;
        try {
            fichierSon=new URL("file:"+nom);
            son= Applet.newAudioClip(fichierSon);
        }
        catch (MalformedURLException mue) {son=null; }
        return son;
    }

    public void delai(int d) {
        try {	Thread.sleep(d); }
        catch (InterruptedException e) {}
    }

    public int largeurZoneGraphique() {
        return zoneDeDessin.size().width;
    }

    public int hauteurZoneGraphique() {
        return zoneDeDessin.size().height;
    }

    public class ZoneGraphique extends JPanel {
// Extension de la classe JPanel de swing :
// Contient un tampon en mémoire dans lequel on peut dessiner.
// ce tampon est affiché à l'écran chaque fois que l'on appele la méthode 
// repaint() ou chaque fois que java fait un paint() (la fenêtre apparaît,
// bouge ...).
// Ce tampon est de la classe Graphics de AWT et on y accède par la méthode
// obtenirGraphics() on peut lui appliquer toutes les méthodes de dessin définies
// dans la classe Graphics (voir AWT).

        private Image dessin=null; // tampon de dessin
        public int taillex=-1; // dimensions de la zone de dessin
        public int tailley=-1; // ces valeurs sont accessibles de l'exterieur

        private void creerTampon() { // creation du tampon
            taillex=getSize().width; // taille du tampon
            tailley=getSize().height;
            dessin=createImage(taillex, tailley); // creation du tampon
        }

        public Graphics obtenirGraphics() {
            // retourne le tampon dans lequel on peut dessiner et qui sera affiché
            // à l'écran par paint() et repaint()
            // voir si la taille de la zone de dessin n'a pas change
            // c'est le cas lorsque l'utilisateur redimensionne la fenetre
            if ((taillex != getSize().width) || (tailley != getSize().height)) {
                creerTampon(); // si oui, il faut recreer la tampon
            }
            return dessin.getGraphics(); // retourne le tampon de dessin
        }

        public void update(Graphics g) {
            // Surdéfinition de update() pour qu'elle n'efface pas
            paint(g);
        }

        public void paint(Graphics g) {
            // Surdéfinition de paint() pour qu'elle affiche le tampon à l'écran
            if (dessin != null) g.drawImage(dessin,0,0,this); // affichage du tampon
        }

        public void setBackground(Color c) {
            super.setBackground(c);
            creerTampon();
        }
    }

}