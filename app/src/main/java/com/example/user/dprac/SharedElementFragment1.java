package com.example.user.dprac;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class SharedElementFragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash_fragment, container, false);
        final ImageView squareBlue = (ImageView) view.findViewById(R.id.square_blue);

        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(SharedPrefManager.getInstance(getActivity()).isLoggedIn()){
                    startActivity(new Intent(getActivity(),MainActivity.class));
                }
                addNextFragment(squareBlue, false);
            }
        }.start();

        return view;
    }

    private void addNextFragment( ImageView squareBlue, boolean overlap) {
        SharedElementFragment2 sharedElementFragment2 = new SharedElementFragment2();
        Slide slideTransition = new Slide(Gravity.BOTTOM);
        slideTransition.setDuration(500);

        ChangeBounds changeBoundsTransition = new ChangeBounds();
        changeBoundsTransition.setDuration(500);


        sharedElementFragment2.setEnterTransition(slideTransition);

        sharedElementFragment2.setSharedElementEnterTransition(changeBoundsTransition);

        getFragmentManager().beginTransaction()
                .replace(R.id.sample2_content, sharedElementFragment2)
                .addSharedElement(squareBlue, getString(R.string.square_blue_name))
                .commit();
    }




}
