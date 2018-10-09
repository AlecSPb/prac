package com.example.user.dprac;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener{

    LinearLayout scan_button;
    View scan_bar;
    Animation animation;
    int CAMERA_REQUEST =9001;
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Constants.inner_fragment_position =0;

        View view  =  inflater.inflate(R.layout.fragment_main, container, false);
        Constants.homePressed=false;

        scan_button = (LinearLayout)view.findViewById(R.id.scan_button);
        scan_bar = (View)view.findViewById(R.id.scan_bar);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.progress);
        startAnimation(animation,scan_bar);

        ((MainActivity)getActivity()).bar_title.setText("Dashboard");
        scan_button.setOnClickListener(this);
        return view;
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
    public void onStart() {
        super.onStart();
        startAnimation(animation,scan_bar);
    }

    @Override
    public void onClick(View v) {

        BarcodeReader barCodeReaderFragment = new BarcodeReader();
        getFragmentManager().beginTransaction()
                .replace(R.id.container, barCodeReaderFragment)
                .addToBackStack(null)
                .commit();
        Constants.inner_fragment_position=5;

    }




}
