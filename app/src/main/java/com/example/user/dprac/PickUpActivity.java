package com.example.user.dprac;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.TokenWatcher;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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

public class PickUpActivity extends AppCompatActivity implements View.OnClickListener{
    TextView order_no;
    JSONObject jsonObject;
    Button pick_up;
    String order_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickupactivity);
        order_no = (TextView)findViewById(R.id.order_no);
        pick_up = (Button)findViewById(R.id.pick_button);
        pick_up.setOnClickListener(this);

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try {
                    order_no.setText(jsonObject.getString("order_id"));
                    order_id = jsonObject.getString("order_id");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    jsonObject = new JSONObject(getIntent().getStringExtra("data"));


                } catch (JSONException e) {
                    e.printStackTrace();

                }

                handler.sendEmptyMessage(0);
            }


        };
        Thread thread = new Thread(runnable);
        thread.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pick_button:


                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("order_id", order_id);
                    jsonObject.put("status", "Shipment Picked Up");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                // put your json here
                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                Request request = new Request.Builder()
                        .url("http://orders.ekuep.com/api/update-order-status")
                        .post(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(PickUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        if (response.isSuccessful()) {
                            PickUpActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showPickUpOrder(PickUpActivity.this);
                                }
                            });
                              //final String data = response.body().string();
//                             Toast.makeText(PickUpActivity.this,data,Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                break;

            case R.id.cancel_button:
                finish();
                break;


        }

    }



    public void showPickUpOrder(final Context context){
{

                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.pickup_dialog_box);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                TextView home = (TextView) dialog.findViewById(R.id.home_button);
                home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    Toast.makeText(getApplicationContext(),"Cancel" ,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        // dialog.dismiss();
                    }

                });

                TextView scan = (TextView) dialog.findViewById(R.id.scan_button);
                scan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  Toast.makeText(context,"Okay" ,Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });

                dialog.show();
            }
        };



}
