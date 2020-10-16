//TRAITEMENT DE SEQUENCE D'IMAGES 
/**
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_calib3d.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
/**/
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class VisioSeqJCV {
  
static public void main(String argv[]) {
  String file="blero_";
  int iMin=3;
  int iMax=25;
  
  //chargement image initiale (pour avoir les dimensions)
  IplImage img0=cvLoadImage(file+iMin+".jpg");
  int largeur=img0.width();
  int hauteur=img0.height();
  
  //creation des fenetres de visu
  cvNamedWindow("vSeq",CV_WINDOW_AUTOSIZE);
  cvNamedWindow("vOut",CV_WINDOW_AUTOSIZE);
  
  //visualisation de la sequence
  for (int i=iMin;i<iMax ; i++) {
	  img0=cvLoadImage(file+i+".jpg");
      cvShowImage("vSeq",img0);
      cvWaitKey(100);
  }
//declaration des tableaux de pixels pour calculs
// int[] tabPix=new int[largeur*hauteur];
  IplImage img1=cvCreateImage(cvGetSize(img0),IPL_DEPTH_8U,1);
  CvMat Matrice= cvCreateMat(hauteur,largeur,CV_8UC1);
  
  // Boucle de Traitement de chaque image
  for (int i=iMin;i<iMax ; i++) {
	  img0=cvLoadImage(file+i+".jpg");
		for (int l=0; l<hauteur; l++) {
			for(int c=0; c<largeur; c++) {
				Matrice.put((l*largeur+c),255-cvGet2D(img0.asCvMat(),l,c).green());
//				tabPix[l*largeur+c]=(int)cvGetReal2D(Matrice, l, c);
//				tabPix[l*largeur + c]=255-tabPix[l*largeur+c];
//				Matrice.put((l*largeur + c), tabPix[l*largeur + c]);
			}
		}
		img1 = Matrice.asIplImage();
		cvShowImage("vOut",img1);
		cvWaitKey(100);
   	}
  	cvWaitKey();
  	
  	Matrice.release();
   	cvDestroyWindow("vSeq");
  	cvDestroyWindow("vOut");
 	cvReleaseImage(img0);
  	cvReleaseImage(img1);
 }
}
//FIN DU PROGRAMME