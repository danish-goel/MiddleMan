package com.hackathon.inout.middleman;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MessagingActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "MiddleMan::EDActivity";
    private BroadcastReceiver receiver;
    private Mat mGray;

    static int max_neighbors=-1;
    static int min_neighbors=-1;

    public static Filter filter;

    private int absoluteFaceSize;

    private CameraBridgeViewBase mOpenCvCameraView;

    private CascadeClassifier cascadeClassifier;
    // This is the openCV cascade Classifier for smile detection
    private CascadeClassifier cascadeClassifierSmile;

    private Button mButtonStart;
    private Button mButtonStop;
    private ImageView mood;

    private Boolean running = true;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        public void onManagerConnected(int status){
            switch(status){
                case LoaderCallbackInterface.SUCCESS:
                    initializeOpenCVDependencies();
                    mOpenCvCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }

    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        filter=new Filter();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);

        setContentView(R.layout.activity_messaging);

        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.camera_view);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(1);

        mood = (ImageView) findViewById(R.id.mood);
        mButtonStart = (Button) findViewById(R.id.button_start);
        mButtonStop = (Button) findViewById(R.id.button_stop);

        IntentFilter filter = new IntentFilter();
        filter.addAction("ADDLOG_C");

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOpenCvCameraView.enableView();
                running = true;
                setButtons();
            }
        });

        mButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOpenCvCameraView.disableView();
                running = false;
                setButtons();
            }
        });

        setButtons();

    }

    private void initializeOpenCVDependencies() {
        try {

            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);

            // This section is to load the face cascade classifier

            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            File file = new File(mCascadeFile.getAbsolutePath());
            if(file.exists())
                System.out.println(mCascadeFile.getAbsolutePath());;
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            System.out.println(cascadeClassifier.load(mCascadeFile.getAbsolutePath()));

            InputStream isSmile = getResources().openRawResource(R.raw.haarcascade_smile);

            // This section is to load the Smile cascade classifier

            File cascadeDirSmile = getDir("cascadeSmile", Context.MODE_PRIVATE);
            File mCascadeFileSmile = new File(cascadeDirSmile, "haarcascade_smile.xml");
            FileOutputStream osSmile = new FileOutputStream(mCascadeFileSmile);

            byte[] bufferSmile = new byte[4096];
            int bytesReadSmile;
            while ((bytesReadSmile = isSmile.read(bufferSmile)) != -1) {
                osSmile.write(bufferSmile, 0, bytesReadSmile);

            }
            isSmile.close();
            osSmile.close();
            cascadeClassifierSmile = new CascadeClassifier(mCascadeFileSmile.getAbsolutePath());
            System.out.println(cascadeClassifierSmile.load(mCascadeFileSmile.getAbsolutePath()));

        } catch (Exception e) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {

        mGray = new Mat(height, width, CvType.CV_8UC1);

        absoluteFaceSize = (int) (height * 0.5);

    }

    public void onCameraViewStopped() {
        mGray.release();
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {

        MatOfRect faces = new MatOfRect();
        Mat inputFrame = frame.gray();

        System.out.println(absoluteFaceSize);

        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(inputFrame, faces, 1.1, 2, 3 | Objdetect.CASCADE_SCALE_IMAGE,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }

        Rect[] facesArray = faces.toArray();
        System.out.println(facesArray.length);
        for (int i = 0; i <facesArray.length; i++)
        {

            Mat grayScaleImageROI= null;
            MatOfRect nestedObjects = new MatOfRect();
            Imgproc.rectangle(inputFrame, facesArray[0].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);

            final int half_height = (int)Math.round((float)facesArray[0].height * 0.5);
            facesArray[0].y = facesArray[0].y + half_height;
            facesArray[0].height = half_height;
            Imgproc.rectangle(inputFrame, facesArray[0].tl(), facesArray[0].br(), new Scalar(0, 255, 0, 255), 3);
            grayScaleImageROI = inputFrame.submat(facesArray[0]);



//            if (cascadeClassifierSmile != null) {
//                cascadeClassifierSmile.detectMultiScale(grayScaleImageROI, nestedObjects, 1.1, 1, 3, new Size(absoluteFaceSize, absoluteFaceSize), new Size());
//            }

            // Crop Mouth
            final int onethird_width = (int)Math.round((float)grayScaleImageROI.width() * 0.33);
            facesArray[0].x = facesArray[0].x + onethird_width;
            facesArray[0].width = onethird_width;
            Imgproc.rectangle(inputFrame, facesArray[0].tl(), facesArray[0].br(), new Scalar(0, 255, 0, 255), 3);
            grayScaleImageROI = inputFrame.submat(facesArray[0]);



            ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            Mat hierarchy = new Mat();

            Mat smallImg2 = new Mat(grayScaleImageROI.size(), CvType.CV_8UC1);
            Mat smallImg1 = new Mat(grayScaleImageROI.size(), CvType.CV_8UC1);

            Imgproc.Canny(grayScaleImageROI, smallImg2, 80, 100);
            Imgproc.findContours(smallImg2, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

            hierarchy.release();
            Imgproc.drawContours(smallImg2, contours, -1, new Scalar(255, 255, 255));

            MatOfInt4 lines = new MatOfInt4();
            Imgproc.HoughLinesP(smallImg2, lines, 1, Math.PI / 180, 20, 15, 10);

            for (int j = 0; j < lines.cols(); j++) {
                double[] l          = lines.get(0, j);
                Point startPoint    = new Point(l[0], l[1]);
                Point endPoint      = new Point(l[2], l[3]);

                Imgproc.line(smallImg2, startPoint, endPoint, new Scalar(255, 0, 255), 1);
            }

            grayScaleImageROI = smallImg2;

            Bitmap bmp = null;
            try {
                bmp = Bitmap.createBitmap(grayScaleImageROI.cols(), grayScaleImageROI.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(grayScaleImageROI, bmp);
            } catch (CvException e) {
                Log.d(TAG, e.getMessage());
            }


            FileOutputStream out = null;

            String filename = String.valueOf(SystemClock.currentThreadTimeMillis());


            File sd = new File(Environment.getExternalStorageDirectory() + "/frames");
            boolean success = true;
            if (!sd.exists()) {
                success = sd.mkdir();
            }
            if (success) {
                File dest = new File(sd, filename);

                try {
                    out = new FileOutputStream(dest);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, e.getMessage());
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                            Log.d(TAG, "OK!!");
                        }
                    } catch (IOException e) {
                        Log.d(TAG, e.getMessage() + "Error");
                        e.printStackTrace();
                    }
                }
            }

            Imgproc.rectangle(inputFrame, facesArray[0].tl(), facesArray[0].br(), new Scalar(0, 255, 0, 255), 3);
            System.out.println("Applied Rectangle");

            Rect[] nestedArray = nestedObjects.toArray();

            int smile_neighbors = (int) nestedArray.length;

            if(min_neighbors ==-1)
                min_neighbors = smile_neighbors;

            max_neighbors = Math.max(max_neighbors, smile_neighbors);

            MatOfDouble mu = new MatOfDouble();
            MatOfDouble sigma = new MatOfDouble();
            Core.meanStdDev(grayScaleImageROI, mu, sigma);
            double d = mu.get(0,0)[0];

            System.out.println("LOL-"+String.valueOf(d));
            if(d>25){

                int decision = filter.add(1);
                if(decision==0){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mOpenCvCameraView.setBackground(getDrawable(R.drawable.blue2));
                        }
                    });

                    Decision.decision=0;
                    return inputFrame;
                }
                else{
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mOpenCvCameraView.setBackground(getDrawable(R.drawable.green2));
                        }
                    });
                    Decision.decision=1;
                    return inputFrame;
                }

            }
            else{

                int decision = filter.add(0);
                if(decision==0){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mOpenCvCameraView.setBackground(getDrawable(R.drawable.blue2));

                        }
                    });

                    Decision.decision=0;
                    return inputFrame;
                }
                else{
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mOpenCvCameraView.setBackground(getDrawable(R.drawable.green2));
                        }
                    });

                    Decision.decision=1;
                    return inputFrame;
                }
            }
        }

        runOnUiThread(new Runnable() {
            public void run() {
                mOpenCvCameraView.setBackground(getDrawable(R.drawable.white2));
            }
        });


//        Intent intent = new Intent("ADDLOG_C");
//        intent.putExtra("log", "Not Detected!");
//        sendBroadcast(intent);
        return inputFrame;
    }

    public void setButtons() {
        if (running == true) {
            mButtonStart.setEnabled(false);
            mButtonStop.setEnabled(true);
        } else {
            mButtonStart.setEnabled(true);
            mButtonStop.setEnabled(false);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
