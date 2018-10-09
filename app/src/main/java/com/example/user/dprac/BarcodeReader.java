package com.example.user.dprac;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class BarcodeReader extends Fragment implements  BarcodeGraphicTracker.BarcodeGraphicTrackerListener {
    private static final String TAG = BarcodeReader.class.getSimpleName();

    private static final int RC_HANDLE_GMS = 9001;


    private boolean autoFocus = true;
    private boolean useFlash = false;
    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    private SharedPreferences permissionStatus;
    private static final int PERMISSION_CALLBACK_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    private boolean sentToSettings = false;


    JSONObject order_data;
    JSONArray product_data;
    String status;

    public BarcodeReader() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_barcode_reader, container, false);
        permissionStatus = getActivity().getSharedPreferences("permissionStatus", getActivity().MODE_PRIVATE);
        mPreview = view.findViewById(R.id.preview);

        ((MainActivity)getActivity()).bar_title.setText("Scan");
        return view;
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        permissionStatus = getActivity().getSharedPreferences("permissionStatus", getActivity().MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.grant_permission));
                builder.setMessage(getString(R.string.permission_camera));
                builder.setPositiveButton(R.string.grant, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        //  mListener.onCameraPermissionDenied();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.CAMERA, false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.grant_permission));
                builder.setMessage(getString(R.string.permission_camera));
                builder.setPositiveButton(R.string.grant, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        //  mListener.onCameraPermissionDenied();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CALLBACK_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.CAMERA, true);
            editor.commit();
        } else {

            proceedAfterPermission();

        }


    }

    private void proceedAfterPermission() {

         createCameraSource(autoFocus,useFlash);

        }
        @SuppressLint("InlinedApi")
    private void createCameraSource(final boolean autoFocus,final boolean useFlash) {
        Log.e(TAG, "createCameraSource:");
        Context context = getActivity();

        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(this);
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());

        if (!barcodeDetector.isOperational()) {

            Log.w(TAG, "Detector dependencies are not yet available.");

            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = getActivity().registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(getActivity(), R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

            CameraSource.Builder builder = new CameraSource.Builder(getActivity(),barcodeDetector)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1600, 1024)
                    .setRequestedFps(15.0f);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                builder = builder.setFocusMode(
                        autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);
            }

            mCameraSource = builder
                    .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                    .build();


    }

    @Override
    public void onResume() {
        super.onResume();
        startCameraSource();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                proceedAfterPermission();
            } else {
            }
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
               proceedAfterPermission();

            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.grant_permission));
                builder.setMessage(getString(R.string.permission_camera));
                builder.setPositiveButton(R.string.grant, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                proceedAfterPermission();
            }
        }
    }

    private void startCameraSource() throws SecurityException {
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                                getActivity());
                        if (code != ConnectionResult.SUCCESS) {
                            Dialog dlg =
                                    GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), code, RC_HANDLE_GMS);
                            dlg.show();
                        }

                        if (mCameraSource != null) {
                            try {
                                mPreview.start(mCameraSource);
                            } catch (IOException e) {
                                Log.e(TAG, "Unable to start camera source.", e);
                                mCameraSource.release();
                                mCameraSource = null;
                            }
                        }
                    }
                });

            }
        };
        thread.start();



    }

    @Override
    public void onScanned(final Barcode barcode) {
        String id = barcode.displayValue;

           orderService(id);
    }


      public void orderService(String id){

      OkHttpClient okHttpClient = new OkHttpClient();
      String url =Constants.get_order_detail_url+id;
      final Request request = new Request.Builder()
              .url(url)
              .header("Authorization","Bearer "+SharedPrefManager.getInstance(getContext()).getToken())
              .build();

      okHttpClient.newCall(request).enqueue(new Callback() {
          @Override
          public void onFailure(Call call, IOException e) {
              Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onResponse(Call call, Response response) throws IOException {

              if(response.isSuccessful()){
                  final String data = response.body().string();

                  getActivity().runOnUiThread(new Runnable() {
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
                              Intent intent = new Intent(getContext(),DeliveredOrderActivity.class);
                              intent.putExtra("order_data",order_data.toString());
                              intent.putExtra("product_data",product_data.toString());
                              startActivity(intent);
                          }else if(status.equals("Customer not available")){
                              Intent intent = new Intent(getContext(),CustomerNotAvailable.class);
                              intent.putExtra("order_data",order_data.toString());
                              intent.putExtra("product_data",product_data.toString());
                              startActivity(intent);
                          }else if(status.equals("Return By Customer")){
                              Intent intent = new Intent(getContext(),ReturnByCustomer.class);
                              intent.putExtra("order_data",order_data.toString());
                              intent.putExtra("product_data",product_data.toString());
                              startActivity(intent);
                          }
                          else if(status.equals("Shipment Picked Up")){
                              Intent intent = new Intent(getContext(),OrderDetailActivity1.class);
                              intent.putExtra("order_data",order_data.toString());
                              intent.putExtra("product_data",product_data.toString());
                              startActivity(intent);
                          }else{
                              Intent intent = new Intent(getContext(),PickUpActivity1.class);
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
