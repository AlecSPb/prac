package com.example.user.dprac;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
 boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        Bundle bundle  = getIntent().getExtras();
        if(bundle!=null){
            flag = bundle.getBoolean("flag");
        }
        setupLayout();
    }

    private void setupLayout() {

        if(!flag){
            SharedElementFragment1 sharedElementFragment1 =new SharedElementFragment1();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.sample2_content, sharedElementFragment1)
                    .commit();
        }else{
            SharedElementFragment2 sharedElementFragment2 = new SharedElementFragment2();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.sample2_content,sharedElementFragment2)
                    .commit();
        }

    }
}
