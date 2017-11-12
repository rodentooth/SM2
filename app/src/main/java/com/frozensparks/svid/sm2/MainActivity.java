package com.frozensparks.svid.sm2;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static java.security.AccessController.getContext;


public class MainActivity extends Activity {
    private static final String TAG = "CamTestActivity";
    Preview preview;
    Button buttonClick;
    Camera camera;
    Activity act;
    Context ctx;
    MediaRecorder recorder;
    boolean recording = false;
    private CameraPreview mPreview;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    ImageView opengallery;
    String timeStamp;
    TextView rectext;
    LinearLayout focus;
    TextView timer;
    Handler h = new Handler();


    int i;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        act = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);


        rectext = (TextView) findViewById(R.id.rectext);
        rectext.setVisibility(View.VISIBLE);
        timer = (TextView) findViewById(R.id.timer);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);




            recorder = new MediaRecorder();

        // Create an instance of Camera
        camera = getCameraInstance();

        Camera.Parameters params = camera.getParameters();
//*EDIT*//params.setFocusMode("continuous-picture");
//It is better to use defined constraints as opposed to String, thanks to AbdelHady
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(params);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, camera);
        preview.addView(mPreview);

        opengallery = (ImageView) findViewById(R.id.opengallery);

        focus = (LinearLayout) findViewById(R.id.focus);
        focus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //camera1.stopPreview();
                Camera.Parameters p = camera.getParameters();
                p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

                camera.setParameters(p);
                //recorder.setPreviewDisplay(mPreview.getHolder().getSurface());
                camera.startPreview();
                camera.autoFocus(null);
                return true;
            }
        });

        // Find the last picture
        String[] projection = new String[]{
                MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DATE_TAKEN,
                MediaStore.Video.VideoColumns.MIME_TYPE
        };
        final Cursor cursor = getApplicationContext().getContentResolver()
                .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null,
                        null, MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC");

// Put it in the image view
        if (cursor.moveToFirst()) {
            String imageLocation = cursor.getString(1);
            File imageFile = new File(imageLocation);
            if (imageFile.exists()) {   // TODO: is there a better way to do this?

                Bitmap bMap = ThumbnailUtils.createVideoThumbnail(imageFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);


                Bitmap bm = BitmapFactory.decodeFile(imageLocation);
                opengallery.setImageBitmap(bMap);
            }
        }
        rectext.setVisibility(View.GONE);
        opengallery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MOVIES) + "/SM2/"));

                Intent galleryIntent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                Uri selectedUri = Uri.parse(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_MOVIES) + "/SM2/");
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setDataAndType(selectedUri, null);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "content://media/internal/images/media/"));
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            }
        });

            /*preview = new Preview(this, (SurfaceView) findViewById(R.id.surfaceView));
            preview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            ((FrameLayout) findViewById(R.id.layout)).addView(preview);
            preview.setKeepScreenOn(true);

            preview.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    if (recording) {
                        // stop recording and release camera
                        recorder.stop();  // stop the recording
                        releaseMediaRecorder(); // release the MediaRecorder object
                        camera.lock();         // take camera access back from MediaRecorder

                        // inform the user that recording has stopped
                        //setCaptureButtonText("Capture");
                        recording = false;
                    } else {
                        // initialize video camera
                        if (prepareRecorder()) {
                            // Camera is available and unlocked, MediaRecorder is prepared,
                            // now you can start recording
                            recorder.start();

                            // inform the user that recording has started
                            //setCaptureButtonText("Stop");
                            recording = true;
                        } else {
                            // prepare didn't work, release the camera
                            releaseMediaRecorder();
                            // inform user
                        }
                /*camera.takePicture(shutterCallback, rawCallback, jpegCallback);
                Toast.makeText(ctx, ("take photo help"), Toast.LENGTH_LONG).show();

                    }
                }
            });

*/


        // Add a listener to the Capture button
        final ImageView captureButton = (ImageView) findViewById(R.id.button_capture);
        captureButton.setImageResource(R.mipmap.rec);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recording) {
                            // stop recording and release camera
                            recorder.stop();  // stop the recording
                            releaseMediaRecorder(); // release the MediaRecorder object
                            camera.lock();         // take camera access back from MediaRecorder

                            // inform the user that recording has stopped
                            captureButton.setImageResource(R.mipmap.rec);

                            recording = false;

                            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DCIM) + "/Camera/", "VID_" + timeStamp + ".mp4");
                            refreshGallery(mediaStorageDir);


                            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(mediaStorageDir.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                            opengallery.setImageBitmap(bMap);

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);


                            builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                            builder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    File fdelete = new File(Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_DCIM) + "/Camera/", "VID_" + timeStamp + ".mp4");
                                    if (fdelete.exists()) {
                                        if (fdelete.delete()) {
                                            System.out.println("file Deleted :" + fdelete.getAbsolutePath());
                                        } else {
                                            System.out.println("file not Deleted :" + fdelete.getAbsolutePath());
                                        }
                                    }
                                    MediaScannerConnection.scanFile(MainActivity.this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                                        /*
                                         *   (non-Javadoc)
                                         * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                                         */
                                        public void onScanCompleted(String path, Uri uri) {
                                            Log.i("ExternalStorage", "Scanned " + path + ":");
                                            Log.i("ExternalStorage", "-> uri=" + uri);
                                        }
                                    });
                                    refreshGallery(fdelete);

                                }

                            });

                            builder.setTitle("SAVE THIS FOOTAGE?");

                            builder.show();


                            rectext.clearAnimation();
                            rectext.setVisibility(View.INVISIBLE);
                            timer.setText("000");
                            i = 0;


                        } else {
                            // initialize video camera
                            if (prepareRecorder()) {
                                // Camera is available and unlocked, MediaRecorder is prepared,
                                // now you can start recording
                                recorder.start();

                                // inform the user that recording has started
                                captureButton.setImageResource(R.mipmap.stop);
                                recording = true;
                                Activity act = MainActivity.this;
                                act.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rectext.setVisibility(View.VISIBLE);

                                        Animation myFadeInAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.twin);
                                        rectext.startAnimation(myFadeInAnimation);
                                    }
                                });


                                h.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

                                        if (recording) {

                                            i = i + 1;
                                            timer.setText(Integer.toString(i));


                                            h.postDelayed(this, 1000);
                                        }
                                    }

                                }, 100);


                            } else {
                                // prepare didn't work, release the camera
                                releaseMediaRecorder();
                                // inform user
                            }
                        }
                    }
                }
        );
        //		buttonClick = (Button) findViewById(R.id.btnCapture);
        //
        //		buttonClick.setOnClickListener(new OnClickListener() {
        //			public void onClick(View v) {
        ////				preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        //				camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        //			}
        //		});
        //
        //		buttonClick.setOnLongClickListener(new OnLongClickListener(){
        //			@Override
        //			public boolean onLongClick(View arg0) {
        //				camera.autoFocus(new AutoFocusCallback(){
        //					@Override
        //					public void onAutoFocus(boolean arg0, Camera arg1) {
        //						//camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        //					}
        //				});
        //				return true;
        //			}
        //		});
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {


        Intent starterIntent = getIntent();
        finish();
        startActivity(starterIntent);


    }

    private boolean  prepareRecorder() {


        camera = getCameraInstance();
        recorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        camera.unlock();
        recorder.setCamera(camera);

        // Step 2: Set sources
        recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file


        String fileName = String.format("%d", System.currentTimeMillis());
        //File outFile = new File(imagesFolder, fileName);


        //recorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
        //recorder.setOutputFile(imagesFolder.getAbsolutePath() +fileName);


        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        refreshGallery(mediaStorageDir);

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("SM2", "failed to create directory");
                            }
        }
        // Create a media file name
         timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        recorder.setOutputFile( Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM)+"/Camera/" + File.separator +
                "VID_" + timeStamp + ".mp4");



        // Step 5: Set the preview output
        recorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;

        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;


        }
        return true;


    }
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    private void releaseMediaRecorder(){
        if (recorder != null) {
            recorder.reset();   // clear recorder configuration
            recorder.release(); // release the recorder object
            recorder = null;
            camera.lock();           // lock camera for later use
        }
    }




    @Override
    protected void onResume() {
        super.onResume();


       /* int numCams = Camera.getNumberOfCameras();
        if(numCams > 0){

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED){
                Toast.makeText(ctx, "denied", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1886);

            }


            else if( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){

                   ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 112);

               }


            else if( ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);

            }
            else


            try{
                camera = Camera.open(0);
                camera.startPreview();
                preview.setCamera(camera);
            } catch (RuntimeException ex){
                Toast.makeText(ctx, "camera not found" +ex, Toast.LENGTH_LONG).show();
            }
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
        finish();
    }
    private void releaseCamera(){
        if (camera != null){
            camera.release();        // release the camera for other applications
            camera = null;
        }
    }

    private void resetCam() {
        camera.startPreview();
        preview.setCamera(camera);
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            //			 Log.d(TAG, "onShutter'd");
        }
    };

    PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //			 Log.d(TAG, "onPictureTaken - raw");
        }
    };

    PictureCallback jpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            new SaveImageTask().execute(data);
            resetCam();
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };



    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (!Environment.getExternalStorageState().equals("")) {

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "MyCameraApp");
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "IMG_" + timeStamp + ".jpg");
            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }
            try {
                File myFile = mediaFile;
                myFile.createNewFile();
                FileOutputStream fOut = new FileOutputStream(myFile);
                OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);
                //myOutWriter.append(getOutputMediaFileUri(2));
                myOutWriter.close();
                fOut.close();
                //txtData.setText("");
            }
            catch (Exception e)
            {

                Log.d("save error",e.getMessage());
            }

            return mediaFile;
        }
        return null;
    }

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            // Write to SD Card
            try {

                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "SM2");
                imagesFolder.mkdirs();


                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/camtest");
                dir.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(imagesFolder, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());

                refreshGallery(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;


        }

    }
}