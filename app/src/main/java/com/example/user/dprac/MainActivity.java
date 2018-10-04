package com.example.user.dprac;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout scan_button;
    View scan_bar;
    Animation animation;
    int CAMERA_REQUEST =9001;
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        Constants.homePressed=false;

        /**
         * Adding Toolbar
         */

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toolbar,null);
        actionBar.setCustomView(view);

        scan_button = (LinearLayout)findViewById(R.id.scan_button);
        scan_bar = (View)findViewById(R.id.scan_bar);
        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.progress);
        startAnimation(animation,scan_bar);

        scan_button.setOnClickListener(this);

        }


    public void startAnimation(final Animation animation, final View object){
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                object.setVisibility(View.VISIBLE);
                object.setAnimation(animation);


            }

            @Override
            public void onAnimationEnd(Animation animation) {
                object.startAnimation(animation);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        object.startAnimation(animation);

    }

    @Override
    protected void onStart() {
        super.onStart();
        startAnimation(animation,scan_bar);
    }


    @Override
    public void onClick(View v) {

        Intent barCodeActivity = new Intent(MainActivity.this,BarCodeReaderActivity.class);
        startActivity(barCodeActivity);
        //startActivityForResult(barCodeActivity,CAMERA_REQUEST);
        //startActivity(new Intent(MainActivity.this,OrderDetailActivity.class));
        //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarCodeReaderActivity.BarcodeObject);
                    Toast.makeText(MainActivity.this,barcode.displayValue,Toast.LENGTH_SHORT).show();

                }
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.logout) {
            SharedPrefManager.getInstance(getApplicationContext()).Logout();
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.putExtra("flag",true);
            finishAffinity();
            startActivity(intent);

            }
        if(item.getItemId() == R.id.history){
            startActivity(new Intent(MainActivity.this,HistoryActivity.class));
        }

        return true;
    }


}
