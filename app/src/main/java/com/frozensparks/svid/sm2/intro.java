package com.frozensparks.svid.sm2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class intro extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);
        final TextView splash = (TextView) findViewById(R.id.splash);
        splash.setVisibility(View.GONE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED
                ) {

            AlphaAnimation fadeOutAnimation = new AlphaAnimation(0, 1); // start alpha, end alpha
            fadeOutAnimation.setDuration(3000); // time for animation in milliseconds
            fadeOutAnimation.setFillAfter(true); // make the transformation persist
            fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    Intent myIntent = new Intent(intro.this, MainActivity.class);
                    intro.this.startActivity(myIntent);
                    finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationStart(Animation animation) {
                    splash.setVisibility(View.VISIBLE);
                }
            });

            splash.setAnimation(fadeOutAnimation);
            splash.startAnimation(fadeOutAnimation);

        }


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(intro.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(intro.this,
                    Manifest.permission.CAMERA)) {

                showExplenationDialog(1);


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1886);


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else if (ContextCompat.checkSelfPermission(intro.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(intro.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                showExplenationDialog(2);


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(intro.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 112);


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else if (ContextCompat.checkSelfPermission(intro.this,
                android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(intro.this,
                    android.Manifest.permission.RECORD_AUDIO)) {

showExplenationDialog(3);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


    }

    public void showExplenationDialog(final int i){

        final AlertDialog.Builder builder = new AlertDialog.Builder(intro.this);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int g) {

                if(i==1){

                    ActivityCompat.requestPermissions(intro.this, new String[]{Manifest.permission.CAMERA}, 1886);


                }
                else if(i==2){

                    ActivityCompat.requestPermissions(intro.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 112);

                }
                else if(i==3){
                    ActivityCompat.requestPermissions(intro.this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);

                }

            }
        });

        if(i==1){

            builder.setTitle("We need access to the camera to record your videos!");


        }
        else if(i==2){

            builder.setTitle("We need access to the storage to save your videos!");

        }
        else if(i==3){
            builder.setTitle("We need access to the mic to record the noise!");

        }


        builder.show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1886: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                                    == PackageManager.PERMISSION_GRANTED
                            ) {
                        Intent myIntent = new Intent(intro.this, MainActivity.class);
                        intro.this.startActivity(myIntent);
                    }else if (ContextCompat.checkSelfPermission(intro.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(intro.this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            showExplenationDialog(2);


                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(intro.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 112);


                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }else if (ContextCompat.checkSelfPermission(intro.this,
                            android.Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(intro.this,
                                android.Manifest.permission.RECORD_AUDIO)) {

                            showExplenationDialog(3);

                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);


                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }

                } else {

                    if (ContextCompat.checkSelfPermission(intro.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(intro.this,
                                Manifest.permission.CAMERA)) {

                            showExplenationDialog(1);


                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1886);


                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }else if (ContextCompat.checkSelfPermission(intro.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(intro.this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            showExplenationDialog(2);


                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(intro.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 112);


                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }else if (ContextCompat.checkSelfPermission(intro.this,
                            android.Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(intro.this,
                                android.Manifest.permission.RECORD_AUDIO)) {

                            showExplenationDialog(3);

                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);


                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }
                }
                return;
            }
            case 112: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                                    == PackageManager.PERMISSION_GRANTED
                            ) {
                        Intent myIntent = new Intent(intro.this, MainActivity.class);
                        intro.this.startActivity(myIntent);
                    }else if (ContextCompat.checkSelfPermission(intro.this,
                            android.Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(intro.this,
                                android.Manifest.permission.RECORD_AUDIO)) {

                            showExplenationDialog(3);

                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);


                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }

                } else {

                    if (ContextCompat.checkSelfPermission(intro.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(intro.this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            showExplenationDialog(2);


                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(intro.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 112);


                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }else if (ContextCompat.checkSelfPermission(intro.this,
                            android.Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(intro.this,
                                android.Manifest.permission.RECORD_AUDIO)) {

                            showExplenationDialog(3);

                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);


                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }

                }
                return;
            }
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                                    == PackageManager.PERMISSION_GRANTED
                            ) {
                        Intent myIntent = new Intent(intro.this, MainActivity.class);
                        intro.this.startActivity(myIntent);
                    }

                } else {

                    if (ContextCompat.checkSelfPermission(intro.this,
                            android.Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(intro.this,
                                android.Manifest.permission.RECORD_AUDIO)) {

                            showExplenationDialog(3);

                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);


                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
    }





}
