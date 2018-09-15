package com.example.user.dprac;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView return_package,not_available,deliver;
     TextView order_no,product_title,serial_no,contact,sku,deliver_address,time_date;
     JSONObject jsonObject;
     String order_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_activity);

        return_package = (ImageView)findViewById(R.id.return_package);
        not_available = (ImageView)findViewById(R.id.not_available);
        deliver       =(ImageView)findViewById(R.id.deliver);



        order_no = (TextView)findViewById(R.id.order_no);
        product_title = (TextView)findViewById(R.id.product_title);
        serial_no = (TextView)findViewById(R.id.serial_no);
        contact = (TextView)findViewById(R.id.contact);
        sku = (TextView)findViewById(R.id.sku);
        deliver_address = (TextView)findViewById(R.id.deliver_address);
        time_date = (TextView)findViewById(R.id.dateTime);

        return_package.setOnClickListener(this);
        not_available.setOnClickListener(this);
        deliver.setOnClickListener(this);

        //Barcode barcode = getIntent().getParcelableExtra(BarCodeReaderActivity.BarcodeObject);
       //Toast.makeText(OrderDetailActivity.this,barcode.displayValue,Toast.LENGTH_SHORT).show();


        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try {
                    order_no.setText(jsonObject.getString("order_id"));
                    order_id = jsonObject.getString("order_id");
//                    product_title.setText(jsonObject.getString("first_name"));
//                    serial_no.setText(jsonObject.getString("last_name"));
//                    contact.setText(jsonObject.getString(""));
//                    sku.setText(jsonObject.getString(""));
//                    deliver_address.setText(jsonObject.getString(""));
//                    time_date.setText(jsonObject.getString(""));
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
        switch (v.getId()){

            case R.id.not_available:
                Helper.showDialogBox(OrderDetailActivity.this,"Customer not available?","Are you sure customer is not available?","Please confirm that the customer is not available.","Yes,Customer not available",order_id);
                break;

            case R.id.deliver:
                Helper.showDialogBox(OrderDetailActivity.this,"Deliver Package?","Are you sure the package is delivered?","Please confirm that the package is delivered","Mark as delivered",order_id);
                break;

            case R.id.return_package:
                Helper.showDialogBox(OrderDetailActivity.this,"Package Return?","Are you sure the package is returned?","Please confirm that the package is returned","Mark as Return",order_id);
                break;

        }
    }


}
