package com.example.user.dprac;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.dprac.Adapters.ProductAdapter;
import com.example.user.dprac.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PickUpActivity1 extends AppCompatActivity implements View.OnClickListener {
    TextView address,bar_title;
    ImageView bar_icon;
    String city,Address,postal_code,country;
    JSONObject jsonObject;
    Button pick_up,cancel;
    String order_id;
    Toolbar toolbar;

    RecyclerView productView;
    List<Product> productList;
    ProductAdapter productAdapter;
    JSONArray jsonArray;
    Button total;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickup_activity1);

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
        bar_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        address = (TextView)findViewById(R.id.address);
        pick_up = (Button)findViewById(R.id.pick_button);
        cancel = (Button)findViewById(R.id.back_button);
        cancel.setOnClickListener(this);
        pick_up.setOnClickListener(this);

        total = (Button)findViewById(R.id.total_amount);

        productView = (RecyclerView)findViewById(R.id.product_list);
        productView.hasFixedSize();
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this,productList);
        productView.setLayoutManager(new LinearLayoutManager(this));
        productView.setAdapter(productAdapter);



        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try {
                    bar_title.setText("Order # "+jsonObject.getString("order_id"));
                    city = jsonObject.getString("city");
                    postal_code = jsonObject.getString("postal_code");
                    country = jsonObject.getString("country");
                    Address = jsonObject.getString("address");
                    if(jsonObject.getString("address")!=null){
                        address.setText(Address+", \n"+city+" "+postal_code+","+country);
                    }
                    order_id = jsonObject.getString("order_id");


                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //Toast.makeText(PickUpActivity1.this,jsonObject.toString(),Toast.LENGTH_LONG).show();
                        Product product = new Product(jsonObject.getString("name"),"Serial # "+jsonObject.getString("reference"),"5555.00",jsonObject.getString("qty"));
                        productList.add(product);

                        }
                    total.setText("SAR "+jsonObject.getString("total_paid"));
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
                    jsonArray = new JSONArray(getIntent().getStringExtra("product_data"));

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

                Helper.showDialogBox(PickUpActivity1.this,"Pick Order?","Are you sure you want to pick this order?","Yes",order_id);
                break;

            case R.id.back_button:
                finish();
                break;


        }

    }
}
