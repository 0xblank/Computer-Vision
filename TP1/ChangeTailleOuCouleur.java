import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

import static java.lang.Math.exp;
import static oracle.jrockit.jfr.events.Bits.intValue;
/*
public class ChangeTailleOuCouleur {

static public void main(String argv[]) {
	// chargement de l'image initiale
	// NB: Il faut qu'elle ait une taille paire en largeur et hauteur
	// pour le traitement 0 (qui divise les dimensions par 2)
	String fic="src/damier_00.jpg";

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
	int[] tabx = new int[largeur*hauteur];
	double alpha = 0.5;
	int choix=2; //choix du traitement : 0 ou 1

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

	case 2: //Flou
		int[] tabx = new int[largeur*hauteur];
		double[] yPlus = new double[largeur*hauteur];
		int[] y = new int[largeur*hauteur];
		double alpha = 0.2;
		for (int l = 0; l < hauteur; l++) {
			for (int c = 0; c < largeur; c++) {
				Color coul=new Color(tabPix[l * largeur + c]); //couleur pixel courant
				tabx[l*largeur+c]=coul.getGreen();
			}
		}

		//Traitement horizontal
		for (int l=0; l<hauteur; l++) {
			for (int c=1; c<largeur; c++){
				yPlus[l*largeur+c]=tabx[l*largeur+c]+ Math.exp(-alpha)*(yPlus[l*largeur+c-1]-tabx[l*largeur+c]);
			}
			for (int c=largeur-2; c>=0; c--){
				y[l*largeur+c]=(int) (yPlus[l*largeur+c]+ Math.exp(-alpha)*(y[l*largeur+c+1]-yPlus[l*largeur+c]));
			}
		}

		// Traitement vertical
		for (int c=0; c<largeur; c++) {
			for (int l=1; l<hauteur; l++){
				yPlus[l*largeur+c]=y[l*largeur+c]+ Math.exp(-alpha)*(yPlus[(l-1)*largeur+c]-y[l*largeur+c]);
			}
			for (int l=hauteur-2; l>=0; l--){
				y[l*largeur+c]=(int) (yPlus[l*largeur+c]+ Math.exp(-alpha)*(y[(l+1)*largeur+c]-yPlus[l*largeur+c]));
			}
		}
		for (int l = 0; l < hauteur; l++) {
			for (int c = 0; c < largeur; c++) {
				int green = y[l*largeur+c];
				Color coul=new Color(green,green,green);
				tabPix[l*largeur+c]=coul.getRGB();
			}
		}
		tabOut=tabPix;
		break;

		case 3:
			double[] tabYplus = new double[largeur*hauteur];
			double[] tabYmoins = new double[largeur*hauteur];
			double[] tabY = new double[largeur*hauteur];
			double[] tabZ = new double[largeur*hauteur];
			int seuil = 10;

			for (int l = 0; l < hauteur; l++) {
				for (int c = 0; c < largeur; c++) {

					Color coul = new Color(tabPix[l * largeur + c]); //couleur pixel courant
					tabx[l * largeur + c] = coul.getGreen();
				}

			// Calcul de Yplus: équations traitement horizontal

			for (l=0; l<hauteur; l++) {

				for (c=1; c<largeur; c++){//5.7
					tabYplus[l*largeur+c]= -tabx[l*largeur+c]+ Math.exp(-alpha)*(tabYplus[l*largeur+c-1] + tabx[l*largeur+c]);
				}
				for (c=largeur-2; c>=0; c--){//5.8
					tabYmoins[l*largeur+c]=(int) (tabx[l*largeur+c]+ Math.exp(-alpha)*(tabYmoins[l*largeur+c+1] - tabx[l*largeur+c]));
				}
				for (c=0; c< largeur; c++){//5.9
					tabY[l*largeur+c] = (int) (tabYplus[l*largeur+c] + tabYmoins[l*largeur+c]);
				}
			}

			//Remise des tableaux à zéro

			for (l=0; l<hauteur; l++){
				for (c = 0; c<largeur; c++){
					tabYplus[l*largeur+c]=0;
					tabYmoins[l*largeur+c]=0;
				}
			}

			// Traitement vertical
			for (c=0; c<largeur; c++) {

				for (l=1; l<hauteur; l++){//5.7
					tabYplus[l*largeur+c]= -tabx[l*largeur+c]+ Math.exp(-alpha)*(tabYplus[(l-1)*largeur+c] + tabx[l*largeur+c]);
				}
				for (l=hauteur-2; l>=0; l--){//5.8
					tabYmoins[l*largeur+c]=(int) (tabx[l*largeur+c]+ Math.exp(-alpha)*(tabYmoins[(l+1)*largeur+c] - tabx[l*largeur+c]));
				}
				for (l = 0; l < hauteur; l++){
					tabZ[l*largeur+c] = (int) (tabYplus[l*largeur+c]+ tabYmoins[l*largeur+c]);
				}
			}


			for (l=0; l<hauteur; l++) {
				for (c=0; c<largeur; c++){
					int pixel = 255;
					double module = Math.sqrt (tabY[l*largeur+c]*tabY[l*largeur+c] + tabZ[l*largeur+c]*tabZ[l*largeur+c]);
					if (module > seuil)
						pixel = 0;
					coul = new Color (pixel, pixel, pixel);
					tabPix[l*largeur+c]=coul.getRGB(); //récupère 3 comp (VVV)


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
}*/