package com.example.user.dprac;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Helper extends AppCompatActivity{
    JSONArray order_data;

    public static void showDialogBox(final Context context, String title, String description1, final String text_button, final String order_id) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.pickup_dialog_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
        TextView dialog_description = (TextView) dialog.findViewById(R.id.dialog_description);

        dialog_title.setText(title);
        dialog_description.setText(description1);

        TextView dialogBtn_cancel = (TextView) dialog.findViewById(R.id.left_button);
        dialogBtn_cancel.setText("Cancel");
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final TextView confirm = (TextView) dialog.findViewById(R.id.right_button);
        confirm.setText(text_button);
        final Helper helper = new Helper();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (text_button.equals("Mark as delivered")) {
                    helper.StatusUpdateService(context, order_id,"Shipment Delivered in Good Condition");
                    dialog.dismiss();
                   // Toast.makeText(context,"Package Delivered",Toast.LENGTH_SHORT).show();

                }else if (text_button.equals("Confirm")) {
                    helper.StatusUpdateService(context, order_id, "Customer not available");
                    dialog.dismiss();
                 //  Toast.makeText(context,"Customer not Available",Toast.LENGTH_SHORT).show();

                } else if (text_button.equals("Mark as Return")) {
                    helper.StatusUpdateService(context, order_id,"Return By Customer");
                    dialog.dismiss();
                   // Toast.makeText(context, "Order Returned", Toast.LENGTH_SHORT).show();
                }else if(text_button.equals("Return to Origin")){
                    helper.StatusUpdateService(context, order_id,"Return to Origin");
                    dialog.dismiss();
                   // Toast.makeText(context, "Order Returned to Origin", Toast.LENGTH_SHORT).show();
                }

                else if(text_button.equals("Yes")){
                    helper.StatusUpdateService(context,order_id,"Shipment Picked Up");
                    dialog.dismiss();
                }


            }
        });

        dialog.show();
    }


    public void StatusUpdateService(final Context context, String order_id, final String status) {
        final Dialog progressBar = Helper.showProgressBar(context);
        progressBar.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", order_id);
            jsonObject.put("status", status);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        // put your json here
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Constants.update_order_status_url)
                .header("Authorization","Bearer "+SharedPrefManager.getInstance(context).getToken())
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.dismiss();

                                    if(status.equals("Shipment Picked Up")){
                                        showPickUpOrder(context);
                                        //Toast.makeText(context,"spu",Toast.LENGTH_LONG).show();
                                    }else if(status.equals("Shipment Delivered in Good Condition")){
                                       // Toast.makeText(context,"sd",Toast.LENGTH_LONG).show();
                                        invalidDialog(context,"Success","Shipment Delivered in good condition.");
                                    }else if(status.equals("Return By Customer")){
                                       // Toast.makeText(context,"rc",Toast.LENGTH_LONG).show();
                                        invalidDialog(context,"Success","Order Returned by Customer.");
                                    }else if(status.equals("Return to Origin")){
                                       // Toast.makeText(context,"ro",Toast.LENGTH_LONG).show();
                                        invalidDialog(context,"Success","Order Returned to Origin.");
                                    }else if(status.equals("Customer not available")){
                                        invalidDialog(context,"Success","Customer not available marked.");
                                       // Toast.makeText(context,"Hello",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                        }
                    };
                    thread.start();

                }else{
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.dismiss();

                                }
                            });

                        }
                    };
                    thread.start();

                }
            }
        });

    }



        public static Dialog showProgressBar(Context context) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_progress_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            AVLoadingIndicatorView loader = (AVLoadingIndicatorView)dialog.findViewById(R.id.progress_bar);
            loader.show();
            return dialog;
        }
    public static Dialog showLoadingBar(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        AVLoadingIndicatorView loader = (AVLoadingIndicatorView)dialog.findViewById(R.id.progress_bar);
        loader.show();
        return dialog;
    }

    public static void invalidDialog(Context context){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_box_invalid_credentials);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView cancel = (TextView) dialog.findViewById(R.id.close_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void invalidDialog(Context context,String title,String description){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_box_invalid_credentials);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView dialog_title = (TextView)dialog.findViewById(R.id.dialog_title);
        TextView dialog_desc = (TextView)dialog.findViewById(R.id.dialog_description);
        TextView cancel = (TextView) dialog.findViewById(R.id.close_button);
        dialog_title.setText(title);
        dialog_desc.setText(description);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }








    public void showPickUpOrder(final Context context){
        {

            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.pickup_dialog_box);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


            TextView home = (TextView) dialog.findViewById(R.id.left_button);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Cancel" ,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    // dialog.dismiss();
                }

            });

            TextView scan = (TextView) dialog.findViewById(R.id.right_button);
            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  Toast.makeText(context,"Okay" ,Toast.LENGTH_SHORT).show();
                    ((Activity)context).finish();

                }
            });

            dialog.show();
        }
    }


    public static void startAnimation(Context context,final View view, int anim){
        Animation animation = AnimationUtils.loadAnimation(context,anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(view.getVisibility() == View.VISIBLE){
                    view.setVisibility(View.INVISIBLE);
                }else if(view.getVisibility() == View.INVISIBLE ){
                    view.setVisibility(View.VISIBLE);
                }else if(view.getVisibility()==View.GONE){
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }




}
