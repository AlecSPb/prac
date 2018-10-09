package com.example.user.dprac;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

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
        }

    private void setupLayout() {

        if(!flag){
            SharedElementFragment1 sharedElementFragment1 =new SharedElementFragment1();
            Constants.login_fragment_position =1;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.sample2_content, sharedElementFragment1)
                    .commit();
        }else{
            SharedElementFragment2 sharedElementFragment2 = new SharedElementFragment2();
            Constants.login_fragment_position =2;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.sample2_content,sharedElementFragment2)
                    .commit();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Toast.makeText(LoginActivity.this,"Start",Toast.LENGTH_LONG).show();
        if(!Constants.homePressed){
            setupLayout();
        }else{
            setUpFragments();
        }
        }


  public void setUpFragments(){
      switch (Constants.login_fragment_position){
          case 1:
              SharedElementFragment1 sharedElementFragment1 =new SharedElementFragment1();
              Constants.login_fragment_position =1;
              getSupportFragmentManager().beginTransaction()
                      .replace(R.id.sample2_content, sharedElementFragment1)
                      .commit();
              Constants.homePressed = false;
              break;

          case 2:
              SharedElementFragment2 sharedElementFragment2 =new SharedElementFragment2();
              Constants.login_fragment_position =2;
              getSupportFragmentManager().beginTransaction()
                      .replace(R.id.sample2_content, sharedElementFragment2)
                      .commit();
              Constants.homePressed = false;
              break;


          case 3:
              SharedElementFragment3 sharedElementFragment3 =new SharedElementFragment3();
              Constants.login_fragment_position =3;
              getSupportFragmentManager().beginTransaction()
                      .replace(R.id.sample2_content, sharedElementFragment3)
                      .commit();
              Constants.homePressed = false;
              break;
      }
  }

    @Override
    protected void onUserLeaveHint()
    {
        Constants.homePressed = true;
        super.onUserLeaveHint();
    }


}

