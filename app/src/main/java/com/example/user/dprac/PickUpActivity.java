package com.example.user.dprac;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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
    TextView order_no,name,address,title,sku,bar_title;
    ImageView bar_icon;
    String city,Address,postal_code,country;
    JSONObject jsonObject;
    Button pick_up,cancel;
    String order_id;
    ImageView order_image;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickupactivity);

        /**
         * Setting up toolbar
         *
         */
        toolbar  =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar  = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toolbar,null);
        actionBar.setCustomView(view);

        bar_icon = (ImageView)findViewById(R.id.bar_icon);
        bar_title = (TextView)findViewById(R.id.bar_title);

        bar_icon.setImageResource(R.drawable.arrow_icon);


        order_no = (TextView)findViewById(R.id.order_no);
        name = (TextView)findViewById(R.id.name);
        address = (TextView)findViewById(R.id.address);
        title = (TextView)findViewById(R.id.product_title);
        sku = (TextView)findViewById(R.id.sku);
        order_image = (ImageView)findViewById(R.id.order_image);
         pick_up = (Button)findViewById(R.id.pick_button);
         cancel = (Button)findViewById(R.id.back_button);
         cancel.setOnClickListener(this);
        pick_up.setOnClickListener(this);

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try {
                    bar_title.setText("Order # "+jsonObject.getString("order_id"));
                    order_no.setText(jsonObject.getString("order_id"));

                    if(jsonObject.getString("name")!=null){
                        name.setText(jsonObject.getString("name"));
                    }

                    if(jsonObject.getString("company")!=null){
                        title.setText(jsonObject.getString("company"));
                    }

                  if(jsonObject.getString("payment_method")!=null){
                      sku.setText("Payment Status: "+jsonObject.getString("payment_method"));
                  }

                    city = jsonObject.getString("city");
                    postal_code = jsonObject.getString("postal_code");
                    country = jsonObject.getString("country");
                    Address = jsonObject.getString("address");
                  if(jsonObject.getString("address")!=null){
                      address.setText(Address+", \n"+city+" "+postal_code+","+country);
                  }

                  order_id = jsonObject.getString("order_id");
                    if(jsonObject.getString("qr_image").equals(null)){
                        Toast.makeText(PickUpActivity.this,jsonObject.getString("qr_image"),Toast.LENGTH_LONG).show();
                        Glide.with(PickUpActivity.this).load(jsonObject.getString("qr_image")).into(order_image);
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    jsonObject = new JSONObject(getIntent().getStringExtra("order_data"));




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
                        .url(Constants.live_url+"update-order-status")
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


                        }
                    }
                });

                break;

            case R.id.back_button:
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


                TextView home = (TextView) dialog.findViewById(R.id.left_button);
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

                TextView scan = (TextView) dialog.findViewById(R.id.right_button);
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
