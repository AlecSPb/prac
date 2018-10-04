package com.example.user.dprac;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class SharedElementFragment1 extends Fragment implements View.OnClickListener{
    ImageView login_btn,login_phone_btn;
    ImageView square_background,square_box,square_mobile,square_login_text,square_selected_tip_left,square_selected_tip_right;
    LinearLayout linearLayout;
    RelativeLayout powered_by_box;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash_fragment, container, false);
        /**
         * Defining views elements
         */
        square_background = (ImageView) view.findViewById(R.id.splash_background);
        square_box = (ImageView)view.findViewById(R.id.splash_box);
        square_mobile = (ImageView) view.findViewById(R.id.splash_mobile);
        linearLayout = (LinearLayout)view.findViewById(R.id.toolbar_box);
        square_selected_tip_left = (ImageView)view.findViewById(R.id.selected_tip_left);
        square_selected_tip_right = (ImageView)view.findViewById(R.id.selected_tip_right);


        login_btn = (ImageView)view.findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
        login_phone_btn = (ImageView)view.findViewById(R.id.login_phone_btn);
        login_phone_btn.setOnClickListener(this);

        powered_by_box = (RelativeLayout)view.findViewById(R.id.powered_by);
        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
                }
            public void onFinish() {
                if(SharedPrefManager.getInstance(getActivity()).isLoggedIn()){
                    startActivity(new Intent(getActivity(),MainActivity.class));
                }else{

                    startAnimation(powered_by_box,R.anim.fade_out);
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startAnimation(linearLayout,R.anim.slide_in_bottom);

                                }
                            });

                        }
                    };
                    thread.start();
                    }
                    }
        }.start();

        return view;
        }

    private void addNextFragment( ImageView square_background,ImageView square_box,ImageView square_mobile,LinearLayout linearLayout, boolean overlap,Fragment fragment,int position,ImageView square_selected_tip) {
        Slide slideTransition = new Slide(Gravity.BOTTOM);
        slideTransition.setDuration(500);
        ChangeBounds changeBoundsTransition = new ChangeBounds();
        changeBoundsTransition.setDuration(500);
        fragment.setEnterTransition(slideTransition);
        fragment.setSharedElementEnterTransition(changeBoundsTransition);
        Constants.fragment_position=position;
        getFragmentManager().beginTransaction()
                .replace(R.id.sample2_content, fragment)
                .addSharedElement(square_background, getString(R.string.square_background))
                .addSharedElement(square_box,getString(R.string.square_box))
                .addSharedElement(square_mobile,getString(R.string.square_mobile))
                .addSharedElement(linearLayout,getString(R.string.square_login_text))
                .addSharedElement(square_selected_tip,getString(R.string.square_selected_tip))
                .commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_btn:
                SharedElementFragment2 sharedElementFragment2 = new SharedElementFragment2();
                addNextFragment(square_background,square_box,square_mobile,linearLayout, false,sharedElementFragment2,2,square_selected_tip_left);
                break;

            case R.id.login_phone_btn:
                SharedElementFragment3 sharedElementFragment3 = new SharedElementFragment3();
                addNextFragment(square_background,square_box,square_mobile,linearLayout, false,sharedElementFragment3,3,square_selected_tip_right);
                break;
        }
    }


    public void startAnimation(final View view, int anim){
        Animation animation = AnimationUtils.loadAnimation(getContext(),anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(view.getVisibility() == View.VISIBLE){
                    view.setVisibility(View.INVISIBLE);
                }else if(view.getVisibility() == View.INVISIBLE ){
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
          view.startAnimation(animation);
    }
}
