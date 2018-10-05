package com.example.user.dprac;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Matrix;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.transition.ChangeBounds;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SharedElementFragment2 extends Fragment implements View.OnClickListener {
    RelativeLayout signIn;
    TextInputLayout emailTxt, passTxt;
    String email, password;
    String status;
    String data;
    Dialog dialog;

    ImageView login_phone_btn,login_btn;
    ImageView square_background, square_box, square_mobile, square_login_text,square_selected_tip;
    LinearLayout linearLayout;
    TextView forgot_password;

    boolean flag = false;

    TextView code_text;
    ImageView code_icon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

/**
 * Defining view Elements
 *
 */
        square_background = (ImageView) view.findViewById(R.id.splash_background);
        square_box = (ImageView) view.findViewById(R.id.splash_box);
        square_mobile = (ImageView) view.findViewById(R.id.splash_mobile);
        linearLayout = (LinearLayout) view.findViewById(R.id.toolbar_box);
        square_selected_tip = (ImageView)view.findViewById(R.id.selected_tip);

        login_phone_btn = (ImageView) view.findViewById(R.id.login_phone_btn);
        login_phone_btn.setOnClickListener(this);
        login_btn = (ImageView)view.findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
        login_btn.setImageResource(R.drawable.login_btn_selected);
        signIn = (RelativeLayout) view.findViewById(R.id.signin_btn);
        signIn.setOnClickListener(this);

        forgot_password = (TextView)view.findViewById(R.id.forgot_password);
        code_text = (TextView)view.findViewById(R.id.code_text);
        code_icon = (ImageView) view.findViewById(R.id.code_icon);


        emailTxt = (TextInputLayout) view.findViewById(R.id.email_txt);
        passTxt = (TextInputLayout) view.findViewById(R.id.password_txt);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            flag = bundle.getBoolean("flag",false);
        }

        if(flag==true){
            emailTxt.setVisibility(View.INVISIBLE);
            passTxt.setVisibility(View.INVISIBLE);
            signIn.setVisibility(View.INVISIBLE);
            code_text.setVisibility(View.INVISIBLE);
            code_icon.setVisibility(View.INVISIBLE);
            forgot_password.setVisibility(View.INVISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    emailTxt.setVisibility(View.VISIBLE);
                    passTxt.setVisibility(View.VISIBLE);
                    signIn.setVisibility(View.VISIBLE);
                    forgot_password.setVisibility(View.VISIBLE);
                    code_icon.setVisibility(View.VISIBLE);
                    code_text.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            emailTxt.startAnimation(animation);
            passTxt.startAnimation(animation);
            signIn.setAnimation(animation);
            forgot_password.setAnimation(animation);
            code_icon.setAnimation(animation);
            code_text.setAnimation(animation);
        }


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin_btn:
                login();
                break;


            case R.id.login_phone_btn:
                SharedElementFragment3 sharedElementFragment3 = new SharedElementFragment3();
                addNextFragment(square_background, square_box, square_mobile, linearLayout, false, sharedElementFragment3,square_selected_tip);
                login_btn.setImageResource(R.drawable.login_btn);
                break;

            case R.id.login_btn:
                break;

        }
    }

    private void login() {
        if (!validateEmail()) {
            return;
        } else if (!emailValidator(email)) {
            emailTxt.setError("Please enter valid email");
        } else if (!validatePassword()) {
            return;
        } else {
            dialog = Helper.showProgressBar(getContext());
            dialog.show();

            JSONObject jsonObject = new JSONObject();
            try {
                email = emailTxt.getEditText().getText().toString().trim();
                password = passTxt.getEditText().getText().toString().trim();
                jsonObject.put("email", email);
                jsonObject.put("password", password);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            // put your json here
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(Constants.live_url + "login/driver")
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
                                    status = reader.getString("status");
                                    JSONObject jsonToken = reader.getJSONObject("token");
                                    JSONObject user = reader.getJSONObject("user_object");
                                    final String id = user.getString("id");
                                    final String token = jsonToken.getString("token");
                                    final String email = user.getString("email");
                                    if (status.equals("200")) {
                                        Handler handler = new Handler();

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                SharedPrefManager.getInstance(getActivity()).StoreUser(id, email, token);
                                                dialog.dismiss();
                                                startActivity(new Intent(getContext(), MainActivity.class));

                                            }
                                        }, 4000);

                                    } else {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                                //  Toast.makeText(getContext(),"Invalid Credentials",Toast.LENGTH_LONG).show();
                                            }
                                        }, 4000);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                    } else {
                        Thread thread = new Thread() {
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

    private boolean validateEmail() {
        email = emailTxt.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            emailTxt.setError("Email Field can't be empty");
            return false;
        } else {
            emailTxt.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        password = passTxt.getEditText().getText().toString().trim();
        if (password.isEmpty()) {
            passTxt.setError("Password Field can't be empty");
            return false;
        } else {
            passTxt.setError(null);
            return true;
        }
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void addNextFragment( ImageView square_background,ImageView square_box,ImageView square_mobile,LinearLayout linearLayout, boolean overlap,Fragment fragment,ImageView square_selected_tip) {

        ChangeBounds changeBoundsTransition = new ChangeBounds();
        changeBoundsTransition.setDuration(420);
        Bundle bundle = new Bundle();
        bundle.putBoolean("flag",true);
        fragment.setArguments(bundle);
        fragment.setSharedElementEnterTransition(changeBoundsTransition);
        Constants.fragment_position = 3;
        getFragmentManager().beginTransaction()
                .replace(R.id.sample2_content, fragment)
                .addSharedElement(square_background, getString(R.string.square_background))
                .addSharedElement(square_box,getString(R.string.square_box))
                .addSharedElement(square_mobile,getString(R.string.square_mobile))
                .addSharedElement(linearLayout,getString(R.string.square_login_text))
                .addSharedElement(square_selected_tip,getString(R.string.square_selected_tip))
                .commit();
    }


}
