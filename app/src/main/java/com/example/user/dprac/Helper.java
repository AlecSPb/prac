package com.example.user.dprac;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

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

public class Helper {

    public static void showDialogBox(final Context context, String title, String description1, String description2, final String text_button, final String order_id) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
        TextView dialog_description = (TextView) dialog.findViewById(R.id.dialog_description1);
        TextView dialong_description2 = (TextView) dialog.findViewById(R.id.dialog_description2);

        dialog_title.setText(title);
        dialog_description.setText(description1);
        dialong_description2.setText(description2);

        TextView dialogBtn_cancel = (TextView) dialog.findViewById(R.id.back_button);
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Cancel" ,Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        final TextView confirm = (TextView) dialog.findViewById(R.id.confirm_button);
        confirm.setText(text_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text_button.equals("Yes,Customer not available")) {
                    StatusUpdateService(context, order_id, "Customer not available");
                    dialog.dismiss();
                    Toast.makeText(context,"Customer not Available",Toast.LENGTH_SHORT).show();
                } else if (text_button.equals("Mark as delivered")) {
                    StatusUpdateService(context, order_id,"Shipment Delivered in Good Condition");
                    dialog.dismiss();
                    Toast.makeText(context,"Package Delivered",Toast.LENGTH_SHORT).show();

                } else if (text_button.equals("Mark as Return")) {
                    StatusUpdateService(context, order_id,"Return By Customer");
                    dialog.dismiss();
                    Toast.makeText(context, "Order Returned", Toast.LENGTH_SHORT).show();
                }else if(text_button.equals("Return to Origin")){
                    StatusUpdateService(context, order_id,"Return to Origin");
                    dialog.dismiss();
                    Toast.makeText(context, "Order Returned to Origin", Toast.LENGTH_SHORT).show();
                }


            }
        });

        dialog.show();
    }


    public static void StatusUpdateService(final Context context, String order_id, String status) {

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
                .url(Constants.live_url+"update-order-status")
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


                    //final String data = response.body().string();
//                             Toast.makeText(PickUpActivity.this,data,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    public static Dialog showProgressBar(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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

}
