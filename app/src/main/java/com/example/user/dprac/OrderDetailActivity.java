package com.example.user.dprac;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView return_package,not_available,deliver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_activity);

        return_package = (ImageView)findViewById(R.id.return_package);
        not_available = (ImageView)findViewById(R.id.not_available);
        deliver       =(ImageView)findViewById(R.id.deliver);

        return_package.setOnClickListener(this);
        not_available.setOnClickListener(this);
        deliver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.deliver:
                Helper.showDialogBox(OrderDetailActivity.this);
                break;

            case R.id.not_available:
                Helper.showDialogBox(OrderDetailActivity.this);
                break;

            case R.id.return_package:
                Helper.showDialogBox(OrderDetailActivity.this);
                break;

        }
    }
}
