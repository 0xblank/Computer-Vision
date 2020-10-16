package TP2;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_highgui.CvCapture;
import java.awt.Frame;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.*;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.cpp.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_calib3d.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

public class blero2 {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        IplImage img=cvLoadImage("src/TP2/BleroJPG/blero_3.jpg");
        IplImage img2=cvLoadImage("src/TP2/BleroJPG/blero_10.jpg");

        cvShowImage("Original",img);
        //cvShowImage("HSV",hsvimg);
        cvShowImage("GRAY",img2);

        cvWaitKey();
        cvSaveImage("Original.jpg",img);
        //cvSaveImage("HSV.jpg",hsvimg);
        cvSaveImage("GRAY.jpg",img2);

        cvReleaseImage(img);
        //cvReleaseImage(hsvimg);
        cvReleaseImage(img2);
    }

}