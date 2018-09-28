package com.example.user.dprac;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.dprac.camera.CameraSource;
import com.example.user.dprac.camera.CameraSourcePreview;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class BarCodeReaderActivity extends AppCompatActivity implements BarcodeTracker.BarcodeGraphicTrackerCallback  {
    JSONObject order_data;
    JSONArray product_data;
    String status;
    public static final String BarcodeObject = "Barcode";


    // Intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;

    // Permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;

    boolean autoFocus = true;
    boolean useFlash = false;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_barcode);

        /**
         * Setting up Toolbar
         */
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_scan_toolbar,null);
        actionBar.setCustomView(view);

        TextView barTitle = (TextView)findViewById(R.id.bar_title);
        barTitle.setText("Scan Product");



        mPreview = (CameraSourcePreview) findViewById(R.id.preview);

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash);
        } else {
            requestCameraPermission();
        }


    }

    @Override
    public void onDetectedQrCode(Barcode barcode) {
        if (barcode != null) {

       //    dialog =  Helper.showProgressBar(this);
         //  dialog.show();

            /**
             * Calling Service of getting order
             */
            String id = barcode.displayValue;
            orderService(id);

        }
    }


    private void requestCameraPermission() {
      //  Log.w(TAG, "Camera permission is not granted. Requesting permission");
        BarCodeReaderActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final String[] permissions = new String[]{Manifest.permission.CAMERA};

                if (!ActivityCompat.shouldShowRequestPermissionRationale(BarCodeReaderActivity.this,
                        Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(BarCodeReaderActivity.this, permissions, RC_HANDLE_CAMERA_PERM);
                }
            }
        });

    }



    private void createCameraSource(final boolean autoFocus,final boolean useFlash) {


        BarCodeReaderActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context context = getApplicationContext();

                BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context)
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();
                BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(BarCodeReaderActivity.this);
                barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());

                if (!barcodeDetector.isOperational()) {

                    IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                    boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

                    if (hasLowStorage) {
                        Toast.makeText(BarCodeReaderActivity.this, "Low Storage",
                                Toast.LENGTH_LONG).show();

                    }
                }

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);

                CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                        .setFacing(CameraSource.CAMERA_FACING_BACK)
                        .setRequestedPreviewSize(metrics.widthPixels, metrics.heightPixels)
                        .setRequestedFps(24.0f);

                // make sure that auto focus is an available option
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    builder = builder.setFocusMode(
                            autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);
                }

                mCameraSource = builder
                        .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                        .build();
            }
        });

    }

    // Restarts the camera
    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    // Stops the camera
    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
    }

    public void onRequestPermissionsResult(final int requestCode,final
                                           @NonNull String[] permissions,final
                                           @NonNull int[] grantResults) {

        BarCodeReaderActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (requestCode != RC_HANDLE_CAMERA_PERM) {

                    BarCodeReaderActivity.super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    return;
                }

                if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    boolean autoFocus = true;
                    boolean useFlash = false;
                    createCameraSource(autoFocus, useFlash);
                    return;
                }



                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(BarCodeReaderActivity.this);
                builder.setTitle("Multitracker sample")
                        .setMessage("No Camera Permission")
                        .setPositiveButton("OK", listener)
                        .show();
            }
        });

    }


    private void startCameraSource() throws SecurityException {

        BarCodeReaderActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                        getApplicationContext());
                if (code != ConnectionResult.SUCCESS) {
                    Dialog dlg =
                            GoogleApiAvailability.getInstance().getErrorDialog(BarCodeReaderActivity.this, code, RC_HANDLE_GMS);
                    dlg.show();
                }

                if (mCameraSource != null) {
                    try {
                        mPreview.start(mCameraSource);
                    } catch (IOException e) {
                        //  Log.e(TAG, "Unable to start camera source.", e);
                        mCameraSource.release();
                        mCameraSource = null;
                    }
                }
            }
        });

    }
  public void orderService(String id){

      OkHttpClient okHttpClient = new OkHttpClient();
      String url = Constants.live_url+"get-order-product-details/"+id;
      final Request request = new Request.Builder().url(url).build();

      okHttpClient.newCall(request).enqueue(new Callback() {
          @Override
          public void onFailure(Call call, IOException e) {
              Toast.makeText(BarCodeReaderActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onResponse(Call call, Response response) throws IOException {

              if(response.isSuccessful()){
                  final String data = response.body().string();

                  BarCodeReaderActivity.this.runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          try {
                              JSONObject reader = new JSONObject(data);
                              order_data = reader.getJSONObject("order_data");
                              product_data =reader.getJSONArray("product_data");


                          } catch (JSONException e) {
                              e.printStackTrace();
                          }

                          try {
                              status = order_data.getString("naqel_status_text");
                          } catch (JSONException e) {
                              e.printStackTrace();
                          }
                          if(status.equals("Shipment Delivered in Good Condition")){
                              Intent intent = new Intent(BarCodeReaderActivity.this,DeliveredOrderActivity.class);
                              intent.putExtra("order_data",order_data.toString());
                              intent.putExtra("product_data",product_data.toString());
                              startActivity(intent);
                          }else if(status.equals("Customer not available")){
                              Intent intent = new Intent(BarCodeReaderActivity.this,CustomerNotAvailable.class);
                              intent.putExtra("order_data",order_data.toString());
                              intent.putExtra("product_data",product_data.toString());
                              startActivity(intent);
                          }else if(status.equals("Return By Customer")){
                              Intent intent = new Intent(BarCodeReaderActivity.this,ReturnByCustomer.class);
                              intent.putExtra("order_data",order_data.toString());
                              intent.putExtra("product_data",product_data.toString());
                              startActivity(intent);
                          }
                          else if(status.equals("Shipment Picked Up")){
                              Intent intent = new Intent(BarCodeReaderActivity.this,OrderDetailActivity1.class);
                              intent.putExtra("order_data",order_data.toString());
                              intent.putExtra("product_data",product_data.toString());
                              startActivity(intent);
                          }else{
                              Intent intent = new Intent(BarCodeReaderActivity.this,PickUpActivity.class);
                              intent.putExtra("order_data",order_data.toString());
                              intent.putExtra("product_data",product_data.toString());
                              startActivity(intent);
                          }



                      }
                  });
              }
          }
      });

  }


}
