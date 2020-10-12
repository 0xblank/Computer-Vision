//Fichier principal
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class TPsample { 

static public void main(String argv[]) {
	// chargement de l'image initiale
	// Il faut qu'elle ait une taille paire en largeur en hauteur (pour algo1DiminueTaille)
	String rep;
	String fic;
	rep="C:/luthon/";
	rep="C:/Users/luthon/Pictures/BaseImgVideo/images/JPEG/";
	fic="Planetes.jpg";
	ImageIcon fond=new ImageIcon(rep+fic);
	int largeur=fond.getIconWidth();
	int hauteur=fond.getIconHeight();
	
	System.out.println(largeur+" x "+hauteur);

	// affichage de cette image dans une fenêtre témoin
	Fenetre initiale=new Fenetre(largeur+9,hauteur+53);
	initiale.obtenirZoneGraphique().drawImage(fond.getImage(),0,0,initiale);
	initiale.miseAJour();

	// transformation de cette image en tableau de pixels
	int[] tabPix=new int[largeur*hauteur];
	// un pixel est codé par 4 octets : alpha,rouge,vert,bleu
	// le premier en dose la tranparence 0 = transparent ... 255 = opaque

	// 1) Acquisition des pixels de l'image
	PixelGrabber pg = new PixelGrabber(fond.getImage(), 0, 0, largeur, hauteur, tabPix, 0, largeur);
    try { pg.grabPixels(); } 
	catch (InterruptedException e) {
		System.err.println("interruption pendant la récupération des pixels de l'image!");
		}

	int choixTraitt=0; //choix du traitement a appliquer
	int[] tabIn;
	int[] tabOut;
	// 3) Traitements
	switch(choixTraitt) {
		case 0:
			//tabOut=MesTraitements.algo1DiminueTaille(tabIn,largeur,hauteur);
			tabOut=algo1DiminueTaille(tabPix,largeur,hauteur);
			largeur=largeur/2;hauteur=hauteur/2;
			break;
		case 1:
			tabOut=new int[largeur*hauteur];
			//MesTraitements.algo2ChangeCouleurFond(tabIn, tabOut,largeur, hauteur);
			algo2ChangeCouleurFond(tabPix, tabOut,largeur, hauteur);
		break;
		default: //mon algo de traitement
			tabIn=new int[largeur*hauteur];
			tabOut=new int[largeur*hauteur];

			// 2) Conversion du tableau (on recupere le plan vert uniquement)
			for (int l = 0; l < hauteur; l++) {
				for (int c = 0; c < largeur; c++) {
					Color coul=new Color(tabPix[l * largeur + c]); 
					tabIn[l*largeur + c]=coul.getGreen();  
				}
			}	
			
			// 3) MES CALCULS ... ICI :
			//mon param inutile 
			int monParam=666;
			algo3Amoi(tabIn,tabOut, monParam, largeur, hauteur);
			
			// 4) Reconstitution d'une image affichable en NdG
				int pixel;
				for (int l = 0; l < hauteur; l++) {
					for (int c = 0; c < largeur; c++) {
						pixel=tabOut[l*largeur + c];
						Color coul=new Color(pixel,pixel,pixel); 
						tabPix[l*largeur + c]=coul.getRGB();
						tabOut[l*largeur + c]=tabPix[l*largeur + c];
					}
				}				
			break;
	}
	
	// conversion du tableau de pixels en objet de classe Image
	Image resultat=initiale.createImage(new MemoryImageSource(largeur, hauteur, tabOut, 0, largeur));
	
	// conversion du tableau de pixels en objet de classe ImageIcon (si on en a besoin)
	ImageIcon resultatIcone=new ImageIcon(resultat);

	// affichage du résultat dans une autre fenêtre
	Fenetre finale=new Fenetre(resultatIcone.getIconWidth()+9, resultatIcone.getIconHeight()+53);
	finale.prepareImage(resultat);
	finale.obtenirZoneGraphique().drawImage(resultat,0,0,finale);
	finale.miseAJour();
	}	
//}
/// a mettre dans un Fichier independant: MesTraitements.java
//public class MesTraitements {
//-------------TRAITEMENT 1 
	public static int[] algo1DiminueTaille(int[] tabPix, int largeur, int hauteur)
	{
		//Traitement de ce tableau de pixels (ici copie d'un pixel sur 2 dans un autre tableau)
		int[] tableauReduit=new int[largeur*hauteur/4];
		int indice=0;
	    for (int l = 0; l < hauteur; l=l+2) {
			for (int c = 0; c < largeur; c=c+2) {
				tableauReduit[indice]=tabPix[l*largeur + c];
				indice++;
			}
		}
		return tableauReduit;
	}
//---------traitement 2
	public static void algo2ChangeCouleurFond(int[] tabPix, int[] tabOut, int largeur, int hauteur)
	{
		//calculs...
		// Traitement de ce tableau de pixels (ici changement de la couleur de fond)
		Color couleurDuFond=new Color(tabPix[0]); // couleur 1er pixel = couleur fond
		Color modifie=new Color(200,150,50); // nouvelle couleur de fond
	    for (int l = 0; l < hauteur; l++) {
			for (int c = 0; c < largeur; c++) {
				Color coul=new Color(tabPix[l * largeur + c]); //couleur pixel courant
				if (coul.equals(couleurDuFond)) { // s'il est couleur fond
					tabPix[l*largeur + c]=modifie.getRGB(); // on le change
				}
				tabOut[l*largeur+c]=tabPix[l*largeur+c];
			}
		}
	}
//----- mon algo a moi...qui ne fait pas grand chose
	public static void algo3Amoi(int[] tabIn, int[] tabOut, int param, int largeur, int hauteur)
	{
		System.out.println("param="+param);
		for (int l = 0; l < hauteur; l++) {
			for (int c = 0; c < largeur; c++) {
				tabOut[l*largeur + c]=tabIn[l*largeur + c];  
			}
		}	
	
	}
}
