package com.example.sift_app_16;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final int PICK_IMAGE_1 = 15;
    final int PICK_IMAGE_2 = 16;


    private ImageView imageView1;
    private ImageView imageView2;
    private Button photo1;
    private Button photo2;
    private Button detect;

    Bitmap cameraImage1;
    Bitmap cameraImage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OpenCVLoader.initDebug();

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        photo1 = findViewById(R.id.photo_1);
        photo2 = findViewById(R.id.photo_2);
        detect = findViewById(R.id.detect);
    }

    public void photo1(View v){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,PICK_IMAGE_1);
    }

    public void photo2(View v){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,PICK_IMAGE_2);
    }

    public void detect(View v){
        Mat refImage1_mat = new Mat();
        Mat refImage2_mat = new Mat();
        Bitmap bmp32_1 = cameraImage1.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap bmp32_2 = cameraImage2.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32_1, refImage1_mat);
        Utils.bitmapToMat(bmp32_2, refImage2_mat);
        Mat pic=ImageProcessor.run(refImage1_mat,refImage2_mat);
        Bitmap display = convertMatToBitMap(pic);
        imageView2.setImageBitmap(display);
    }

    private static Bitmap convertMatToBitMap(Mat input){
        Bitmap bmp = null;
        Mat rgb = new Mat();
        Imgproc.cvtColor(input, rgb, Imgproc.COLOR_BGR2RGB);

        try {
            bmp = Bitmap.createBitmap(rgb.cols(), rgb.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(rgb, bmp);
        }
        catch (CvException e){
            Log.d("Exception",e.getMessage());
        }
        return bmp;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_1) {

                cameraImage1 = (Bitmap) data.getExtras().get("data");
                imageView1.setImageBitmap(cameraImage1);
            }
            if (requestCode == PICK_IMAGE_2) {
                cameraImage2 = (Bitmap) data.getExtras().get("data");
                imageView2.setImageBitmap(cameraImage2);
                }
            }

        }
    }
