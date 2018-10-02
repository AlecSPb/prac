package com.example.user.dprac;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SharedElementFragment3 extends Fragment implements View.OnClickListener {
  LinearLayout login_phone;
  String status;
  String data;
  Dialog dialog;
  CountryCodePicker cpp;
  String country_code;
  String phone;
  TextInputLayout phone_no;
    ImageView login_btn,login_phone_btn;
    ImageView square_background,square_box,square_mobile,square_login_text;
    LinearLayout linearLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_with_phone_fragment, container, false);

        login_phone = (LinearLayout) view.findViewById(R.id.signin_btn);
        login_phone.setOnClickListener(this);


        login_btn = (ImageView)view.findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
        login_phone_btn = (ImageView)view.findViewById(R.id.login_phone_btn);
        login_phone_btn.setImageResource(R.drawable.login_with_number_btn_selected);
        square_background = (ImageView) view.findViewById(R.id.splash_background);
        square_box = (ImageView)view.findViewById(R.id.splash_box);
        square_mobile = (ImageView) view.findViewById(R.id.splash_mobile);
        linearLayout = (LinearLayout)view.findViewById(R.id.toolbar_box);


        phone_no = (TextInputLayout)view.findViewById(R.id.phone_text);
        cpp = (CountryCodePicker)view.findViewById(R.id.ccp);
        country_code = cpp.getDefaultCountryCodeWithPlus();
        cpp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country_code = cpp.getSelectedCountryCodeWithPlus();
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signin_btn:
                login();
                break;

            case R.id.login_btn:
                SharedElementFragment2 sharedElementFragment2 = new SharedElementFragment2();
                addNextFragment(square_background,square_box,square_mobile,linearLayout, false,sharedElementFragment2);
                login_phone_btn.setImageResource(R.drawable.login_with_number_btn);
                break;
        }
    }

    private void login() {
        if(!validatePhone()){
            return;
        }else {
            dialog = Helper.showProgressBar(getContext());
            dialog.show();

            JSONObject jsonObject = new JSONObject();
            try {
                phone = phone_no.getEditText().getText().toString().trim();
                jsonObject.put("phone_number", country_code+phone);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            // put your json here
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(Constants.live_url+"phone/send/code/mobile")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(),"heelo",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if (response.isSuccessful()) {
                        data = response.body().string();

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                JSONObject reader = null;

                                try {
                                    reader = new JSONObject(data);
                                    JSONObject read = reader.getJSONObject("response");
                                    status = read.getString("code");
                                    if(status.equals("200")){
                                   Handler handler  = new Handler();

                                   handler.postDelayed(new Runnable() {
                                       @Override
                                       public void run() {
                                           dialog.dismiss();
                                           Toast.makeText(getContext(),"Code Sent",Toast.LENGTH_LONG).show();

                                       }
                                   },4000);

                                    }else{
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();

                                            }
                                        },4000);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                    }else{
                       Thread thread = new Thread(){
                           @Override
                           public void run() {
                               try {
                                   Thread.sleep(3000);
                               } catch (InterruptedException e) {
                               }
                               getActivity().runOnUiThread(new Runnable() {
                                   @Override
                                    public void run() {
                                      dialog.dismiss();
                                       Helper.invalidDialog(getContext());
                                    }
                                });

                           }
                       };
                       thread.start();
                       }//end of else section
                }
            });

        }
    }

    private boolean validatePhone(){
        phone = phone_no.getEditText().getText().toString().trim();
        if(phone.isEmpty()){
            phone_no.setError("Field can't be empty");
            return false;
        }else{
            phone_no.setError(null);
            return true;
        }
    }


    private void addNextFragment( ImageView square_background,ImageView square_box,ImageView square_mobile,LinearLayout linearLayout, boolean overlap,Fragment fragment) {

        ChangeBounds changeBoundsTransition = new ChangeBounds();
        changeBoundsTransition.setDuration(500);
        fragment.setSharedElementEnterTransition(changeBoundsTransition);

        getFragmentManager().beginTransaction()
                .replace(R.id.sample2_content, fragment)
                .addSharedElement(square_background, getString(R.string.square_background))
                .addSharedElement(square_box,getString(R.string.square_box))
                .addSharedElement(square_mobile,getString(R.string.square_mobile))
                .addSharedElement(linearLayout,getString(R.string.square_login_text))
                .commit();
    }

}
