package TP2;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_32F; //ajout manuel
import static com.googlecode.javacv.cpp.opencv_core.CV_TERMCRIT_ITER; //ajout manuel
import static com.googlecode.javacv.cpp.opencv_core.CV_TERMCRIT_EPS; //ajout manuel
import static com.googlecode.javacv.cpp.opencv_core.cvTermCriteria; //ajout manuel
import static com.googlecode.javacv.cpp.opencv_legacy.cvCalcOpticalFlowHS; //ajout manuel
import static com.googlecode.javacv.cpp.opencv_core.cvConvert; //ajout manuel
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateMat;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_CAP_ANY;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_CAP_PROP_FRAME_WIDTH;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_FOURCC;
import static com.googlecode.javacv.cpp.opencv_highgui.CV_WINDOW_AUTOSIZE;
import static com.googlecode.javacv.cpp.opencv_highgui.cvCreateCameraCapture;
import static com.googlecode.javacv.cpp.opencv_highgui.cvCreateFileCapture;
import static com.googlecode.javacv.cpp.opencv_highgui.cvDestroyWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvNamedWindow;
import static com.googlecode.javacv.cpp.opencv_highgui.cvQueryFrame;
import static com.googlecode.javacv.cpp.opencv_highgui.cvReleaseCapture;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSetCaptureProperty;
import static com.googlecode.javacv.cpp.opencv_highgui.cvShowImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvWaitKey;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCanny;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;

import com.googlecode.javacv.FrameRecorder;
import com.googlecode.javacv.OpenCVFrameRecorder;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;
//
import com.googlecode.javacv.cpp.opencv_core.CvTermCriteria;//manuel
import com.googlecode.javacv.cpp.opencv_highgui;//manuel
import com.googlecode.javacv.*;
import com.googlecode.javacv.FrameRecorder.Exception;
import com.googlecode.javacv.cpp.*;
import org.w3c.dom.css.RGBColor;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_calib3d.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
import static com.googlecode.javacv.cpp.opencv_legacy.*;


public class blero {

    public static void main(String[] args) {
        int wi=450;//largeur
        int he=320;//hauteur
        CvCapture capture;
        CvCapture capture1;
        CvCapture capturePlage;
        IplImage frame, resimg, frame1, framePlage, srcimg;
        //int choix=0;
        double seuil=6;

/*
        switch(choix) {
            case 0:
                capture=cvCreateCameraCapture(CV_CAP_ANY);
                cvSetCaptureProperty(capture,CV_CAP_PROP_FRAME_WIDTH,wi);
                cvSetCaptureProperty(capture,CV_CAP_PROP_FRAME_HEIGHT,he);
                break;
            default :
                String rep="src/TP2";
                String file=rep+"VideoCourte.avi";
                capture=cvCreateFileCapture(file);
                break;
        }*/


        cvNamedWindow("Src Video",CV_WINDOW_AUTOSIZE);
        cvNamedWindow("Res Video",CV_WINDOW_AUTOSIZE);


        for(int j=4;j<=24;j++)
        {
            String rep="src/TP2/";
            String file=rep+"BleroJPG/blero_"+j+".jpg";

            int k = j-1;

            String filem1 =rep+"/BleroJPG/blero_"+k+".jpg";
            String filePlage =rep+"plage.jpg";

            capture=cvCreateFileCapture(file);
            capture1=cvCreateFileCapture(filem1);
            capturePlage=cvCreateFileCapture(filePlage);

            frame=cvQueryFrame(capture);
            frame1=cvQueryFrame(capture1);
            framePlage=cvQueryFrame(capturePlage);
            if(frame == null) {
                System.out.println("ERR=No video file");
                break;
            }
            if(frame1 == null) {
                System.out.println("ERR=No video file");
                break;
            }
            if(framePlage == null) {
                System.out.println("ERR=No video file");
                break;
            }

            CvMat  MatriceDest= cvCreateMat(frame.height(),frame.width(),opencv_core.CV_8UC1);
            CvMat MatriceSrc = frame.asCvMat();

            CvMat MatriceSrc1 = frame1.asCvMat();

            CvMat MatriceSrcPlage = framePlage.asCvMat();

            for (int l=0; l<frame.height(); l++) {
                for(int c=0; c<frame.width(); c++){

                    CvScalar pixel = cvGet2D(MatriceSrc, l, c);
                    CvScalar pixel1 = cvGet2D(MatriceSrc1, l, c);
                    CvScalar pixelPlage = cvGet2D(MatriceSrcPlage, l, c);

                    double pix=pixel.green();
                    double pix1=pixel1.green();
                    double pixPlage=pixelPlage.green();

                    if(pix>=pix1+seuil || pix<=pix1-seuil) {

                        MatriceDest.put((l*frame.width()+c),pix);
                    }

                    else {
                        MatriceDest.put((l*frame.width()+c),255);
                    }

                }

            }
            srcimg = MatriceSrc.asIplImage();
            resimg = MatriceDest.asIplImage();


            cvErode(resimg, resimg, null, 1);
            cvDilate(resimg, resimg, null, 1);
            cvDilate(resimg, resimg, null, 1);
            cvErode(resimg, resimg, null, 1);

            MatriceDest = resimg.asCvMat();

            for (int l=0; l<frame.height(); l++) {
                for(int c=0; c<frame.width(); c++){

                    CvScalar pixel = cvGet2D(MatriceDest, l, c);
                    CvScalar pixelSrc = cvGet2D(MatriceSrc, l, c);
                    CvScalar pixelPlage = cvGet2D(MatriceSrcPlage, l, c);

                    double pix=pixel.blue();
                    double pixSrc=pixelSrc.green();
                    double pixPlage=pixelPlage.green();
                    System.out.println(pixel);
                    System.out.println(pixel.red());
                    System.out.println(pixel.blue());
                    System.out.println(pixel.green());
                    if(pix == 255) {

                        MatriceDest.put((l*frame.width()+c),pixPlage);
                    }
                    else{
                        MatriceDest.put((l*frame.width()+c),pixSrc);
                    }
                }

            }
            MatriceSrc.release();
            MatriceDest.release();
            cvShowImage("Src Video",srcimg);
            cvShowImage("Res Video",resimg);
            char c=(char) cvWaitKey(25);
            if(c=='q') break;

        }
 //       cvReleaseCapture(capture);
        cvDestroyWindow("Src Video");
        cvDestroyWindow("Res Video");
        System.exit(0);

    }

}