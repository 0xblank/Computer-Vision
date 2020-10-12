import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class ChangeTailleOuCouleur { 

static public void main(String argv[]) {
	// chargement de l'image initiale 
	// NB: Il faut qu'elle ait une taille paire en largeur et hauteur
	// pour le traitement 0 (qui divise les dimensions par 2)
	String fic="Planetes.jpg";
	
	ImageIcon fond=new ImageIcon(fic);
	int largeur=fond.getIconWidth();
	int hauteur=fond.getIconHeight();
	System.out.println(largeur+" x "+hauteur);

	// affichage de cette image dans une fenêtre témoin
	JFrame initiale=new JFrame("Mon image initiale"); 
	initiale.setSize(largeur+9,hauteur+53);
	initiale.setVisible(true);
    JLabel image = new JLabel(fond);
    initiale.add(image);
    initiale.repaint();

	// transformation de cette image en tableau de pixels
	int[] tabPix=new int[largeur*hauteur];
	// un pixel est codé par 4 octets : alpha,rouge,vert,bleu
	// alpha dose la transparence: 0 = transparent ... 255 = opaque
    PixelGrabber pg = new PixelGrabber(fond.getImage(), 0, 0,
    		largeur, hauteur, tabPix, 0, largeur);
    try { pg.grabPixels(); } 
	catch (InterruptedException e) {
		System.err.println(
		"interruption pendant la récupération des pixels de l'image!");
	}
    //Traitement de ce tableau de pixels 
	Image resultat=null; //on y mettra le resultat du traitement
	int[] tabOut=null;
	int large=largeur;
	int haut=hauteur;
	int choix=0; //choix du traitement : 0 ou 1
	
    switch(choix) {
    case 0: //Diminue Taille 
    	// (ici : copie d'un pixel sur 2 dans un autre tableau)
    	int[] tableauReduit=new int[largeur*hauteur/4];
    	int indice=0;
        for (int l = 0; l < hauteur; l=l+2) {
    		for (int c = 0; c < largeur; c=c+2) {
    			tableauReduit[indice]=tabPix[l*largeur + c];
    			indice++;
    		}
    	}
        large=largeur/2;
        haut=hauteur/2;
        tabOut=tableauReduit;
    break;
    
    case  1://Change Couleur Fond
    	//(ici changement de la couleur de fond en caca d'oie)
    	Color couleurDuFond=new Color(tabPix[0]); // 1er pixel = couleur fond
    	Color modifie=new Color(200,150,50); // nouvelle couleur de fond
        for (int l = 0; l < hauteur; l++) {
    		for (int c = 0; c < largeur; c++) {
    			Color coul=new Color(tabPix[l * largeur + c]); //couleur pixel courant
    			if (coul.equals(couleurDuFond)) { // s'il est couleur fond
    				tabPix[l*largeur + c]=modifie.getRGB(); // on le change
    			}
    		}
    	}
        tabOut=tabPix;
    break;
    
    default: //Identite
    	tabOut=tabPix;
    break;
    }
	// conversion du tableau de pixels en objet de classe Image
   	resultat=initiale.createImage(new MemoryImageSource(large,
   			haut, tabOut, 0, large));

	// conversion du tableau de pixels en objet de classe ImageIcon
	ImageIcon resultatIcone=new ImageIcon(resultat);

	// affichage du resultat dans une autre fenetre
	JFrame finale=new JFrame();
	finale.setSize(resultatIcone.getIconWidth()+9,
			resultatIcone.getIconHeight()+53);
	finale.setVisible(true);
    JLabel imgOut = new JLabel(resultatIcone);
    finale.add(imgOut);
    finale.repaint();
	}
}