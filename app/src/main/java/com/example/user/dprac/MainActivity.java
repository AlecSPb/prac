package com.example.user.dprac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout scan_button;
    View scan_bar;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        scan_button = (LinearLayout)findViewById(R.id.scan_button);
        scan_bar = (View)findViewById(R.id.scan_bar);
        animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.progress);
        startAnimation(animation,scan_bar);

        scan_button.setOnClickListener(this);







    }


    public void startAnimation(Animation animation, final View object){

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
       startActivity(new Intent(MainActivity.this,OrderDetailActivity.class));
        //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }
}
