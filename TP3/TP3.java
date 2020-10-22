package TP3;//Auteur: F.Luthon UPPA 2020//
/**
import static com.googlecode.javacv.cpp.opencv_calib3d.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
import static com.googlecode.javacv.cpp.opencv_legacy.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
/**/
import com.googlecode.javacv.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class TP3 {
 public static void main(String[] args) throws Exception {
  int wi=320; //largeur par defaut
  int he=240; //hauteur par defaut
  CvCapture capture; //source video
  IplImage frame, grayimg;
  int choix=3; //sourceCamera=0;sinonFichier//
  switch(choix) {
   case 0:
    capture=cvCreateCameraCapture(CV_CAP_ANY);//ou (-1);//
    cvSetCaptureProperty(capture,CV_CAP_PROP_FRAME_WIDTH,wi);
    cvSetCaptureProperty(capture,CV_CAP_PROP_FRAME_HEIGHT,he);
   break;
   default:
    String rep="";
    //String fic="video_intro.mpg";
    String fic= "src/TP3/402779392.mp4";
    String file=rep+fic;
    capture=cvCreateFileCapture(file);
    frame=cvQueryFrame(capture);
    CvSize dim=cvGetSize(frame);
    wi=dim.width(); he=dim.height();
  }
  System.out.println(wi+" x "+he);
  FrameRecorder recorder=new OpenCVFrameRecorder("EnregisVid.AVI",wi,he);
  recorder.setVideoCodec(CV_FOURCC('M','J','P','G'));
  recorder.setFrameRate(15);
  recorder.setPixelFormat(0); //ATT:0 pour NdG; 1 pour couleur//
  recorder.start();
  cvNamedWindow("VideoIn",CV_WINDOW_AUTOSIZE);
  cvNamedWindow("VideoOut",CV_WINDOW_AUTOSIZE);
  for(;;) {
   frame=cvQueryFrame(capture);
   if(frame == null) {
    System.out.println("ERR=No video file");
    break;
   }
   cvShowImage("VideoIn",frame);
   grayimg=cvCreateImage(cvGetSize(frame),IPL_DEPTH_8U,1);
   CvMat matrice= cvCreateMat(he,wi,CV_8UC1);
   /*TRAITEMENT: Negatif */
   int N = 5; 
   double A = 0.8;
   double[][] H = new double[N][N];
   for(int k=0; k<4; k++) {
       for(int p=0; p<4; p++) {
           H[k][p] = 1;
       }
   }

   int d = (N-1)/2;
   for (int l=0; l<he; l++) {
    for (int c=0; c<wi; c++) {
    	//Voisinage de chaque pixel
    	int xmin = Math.max(0,c-d);
    	int xmax = Math.min((wi-1),c+d);
    	int ymin = Math.max(0,l-d);
    	int ymax = Math.min((he-1),l+d);
    	//System.out.println("x "+xmin+" "+xmax);
    	//System.out.println("y "+ymin+" "+ymax);

    	CvScalar pixelimage = cvGet2D(frame.asCvMat(), l, c);
    	double a = 0;
    	double b = 0;
    	//coef du pixel dans la matrice de convolution centr�e
    	for (int i=xmin; i<xmax; i++) {
    		for (int j=ymin; j<ymax; j++) {
            	int vx = i-c+d;
            	int vy = j-l+d;
            	
            	//System.out.println(i+" "+j);
            	CvScalar pixel = cvGet2D(frame.asCvMat(), j, i);
            	a = a + pixel.green()*H[vx][vy];
            	b = b + H[vx][vy];
        	}
    	};
    	double mflou = a/b;
    	double mdetail = pixelimage.green()-mflou;
    	
    	double im_final = 0; 
    	switch(choix) {
    	   case 1:
    		   im_final = A*pixelimage.green()+(1-A)*mdetail;
    	   break;
    	   case 2:
    		   im_final = A*pixelimage.green()+(1-A)*Math.log(mdetail);
    	   break;
    	   case 3:
    		   im_final = A*pixelimage.green()+(1-A)*Math.exp(mdetail);
    	   break;
    	  }
		  matrice.put((l*wi + c), im_final);
    	/*
     CvScalar pixel = cvGet2D(frame.asCvMat(), l, c);
     double pix=255-pixel.green();
     matrice.put((l*wi + c), pix);
     */
    }
   }
   grayimg = matrice.asIplImage();
   matrice.release();
   cvShowImage("VideoOut",grayimg);
   recorder.record(grayimg); //frame);//ATT:pixelFormat//
   char c=(char) cvWaitKey(1000/30);
   if(c=='q') break;
  }
  recorder.stop();
  cvReleaseCapture(capture);
  cvDestroyWindow("VideoIn");
  cvDestroyWindow("VideoOut");
  cvReleaseImage(frame);
 }
}
//FIN//