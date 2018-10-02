package com.example.user.dprac;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
  RelativeLayout login_phone;
  String status;
  String data;
  Dialog dialog;
  CountryCodePicker cpp;
  String country_code;
  String phone,verificationCode;
  TextInputLayout phone_no,verification_code;
  ImageView login_btn,login_phone_btn,code_icon;
  ImageView square_background,square_box,square_mobile,square_login_text;
  LinearLayout toolbar_btn,code_send_btn;
  boolean flag = false;
  TextView code_text;
  String code;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_with_phone_fragment1, container, false);

        login_phone = (RelativeLayout) view.findViewById(R.id.signin_btn);
        login_phone.setOnClickListener(this);


        login_btn = (ImageView)view.findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
        login_phone_btn = (ImageView)view.findViewById(R.id.login_phone_btn);
        login_phone_btn.setImageResource(R.drawable.login_with_number_btn_selected);
        square_background = (ImageView) view.findViewById(R.id.splash_background);
        square_box = (ImageView)view.findViewById(R.id.splash_box);
        square_mobile = (ImageView) view.findViewById(R.id.splash_mobile);
        toolbar_btn = (LinearLayout)view.findViewById(R.id.toolbar_box);

        code_text = (TextView)view.findViewById(R.id.code_text);
        code_icon = (ImageView) view.findViewById(R.id.code_icon);
        code_send_btn = (LinearLayout)view.findViewById(R.id.send_code_btn);
        code_send_btn.setOnClickListener(this);

        verification_code = (TextInputLayout)view.findViewById(R.id.verification_code);

        phone_no = (TextInputLayout)view.findViewById(R.id.phone_text);
        cpp = (CountryCodePicker)view.findViewById(R.id.ccp);
        country_code = cpp.getDefaultCountryCodeWithPlus();
        cpp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country_code = cpp.getSelectedCountryCodeWithPlus();
            }
        });


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            flag = bundle.getBoolean("flag",false);
        }

        if(flag==true){
            phone_no.setVisibility(View.INVISIBLE);
            cpp.setVisibility(View.INVISIBLE);
            login_phone.setVisibility(View.INVISIBLE);
            code_text.setVisibility(View.INVISIBLE);
            code_icon.setVisibility(View.INVISIBLE);
            code_send_btn.setVisibility(View.INVISIBLE);
            verification_code.setVisibility(View.INVISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    phone_no.setVisibility(View.VISIBLE);
                    cpp.setVisibility(View.VISIBLE);
                    login_phone.setVisibility(View.VISIBLE);
                    code_icon.setVisibility(View.VISIBLE);
                    code_text.setVisibility(View.VISIBLE);
                    code_send_btn.setVisibility(View.VISIBLE);
                    verification_code.setVisibility(View.VISIBLE);

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            phone_no.startAnimation(animation);
            cpp.startAnimation(animation);
            login_phone.setAnimation(animation);
            code_send_btn.setAnimation(animation);
            code_icon.setAnimation(animation);
            code_text.setAnimation(animation);
            verification_code.setAnimation(animation);
        }







        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signin_btn:
                login();
                break;

            case R.id.send_code_btn:
                sendCode();
                break;

            case R.id.login_btn:
                SharedElementFragment2 sharedElementFragment2 = new SharedElementFragment2();
                addNextFragment(square_background,square_box,square_mobile, toolbar_btn, false,sharedElementFragment2);
                login_phone_btn.setImageResource(R.drawable.login_with_number_btn);
                break;
        }
    }

    private void login() {
        if(!validateCode()){
            return;
        }else {
            dialog = Helper.showProgressBar(getContext());
            dialog.show();

            JSONObject jsonObject = new JSONObject();
            try {
                verificationCode = verification_code.getEditText().getText().toString().trim();
                jsonObject.put("phone_number", country_code+phone);
                jsonObject.put("verification_code",verificationCode);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            // put your json here
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(Constants.live_url+"phone/verification/mobile")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
//                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                    status = reader.getString("code");
                                    if(status.equals("200")){
                                        Handler handler  = new Handler();

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                                Toast.makeText(getContext(),"Code is correct",Toast.LENGTH_LONG).show();
                                                /**
                                                 *
                                                 * Store the user in shared Preferences and send the user to dashboard
                                                 */

                                            }
                                        },4000);

                                    }else{
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                                Helper.invalidDialog(getContext(),"Alert","Code is not correct");

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
                                        Helper.invalidDialog(getContext(),"Alert","Code is not correct");
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


    public void sendCode(){
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
                                                SmsReceiver.bindListener(new SmsReceiver.SmsListener() {
                                                    @Override
                                                    public void messageReceived(String messageText) {
                                                        Toast.makeText(getContext(),messageText,Toast.LENGTH_LONG).show();
                                                    }
                                                });

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
                                        Helper.invalidDialog(getContext(),"Alert","Phone number is invalid");
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
            phone_no.setError("This Field can't be empty");
            return false;
        }else{
            phone_no.setError(null);
            return true;
        }
    }
    private boolean validateCode(){
        verificationCode = verification_code.getEditText().getText().toString().trim();
        if(verificationCode.isEmpty()){
            verification_code.setError("Please Enter the code");
            return false;
        }else{
            verification_code.setError(null);
            return true;
        }
    }



    private void addNextFragment( ImageView square_background,ImageView square_box,ImageView square_mobile,LinearLayout linearLayout, boolean overlap,Fragment fragment) {

        ChangeBounds changeBoundsTransition = new ChangeBounds();
        changeBoundsTransition.setDuration(500);
        Bundle bundle = new Bundle();
        bundle.putBoolean("flag",true);
        fragment.setArguments(bundle);
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
