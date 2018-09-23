package com.example.user.dprac;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SharedElementFragment2 extends Fragment implements View.OnClickListener {
  Button signIn;
  TextInputLayout emailTxt,passTxt;
  String email,password;
  String status;
  String data;
  boolean flag=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        ImageView squareBlue = (ImageView) view.findViewById(R.id.square_blue);
        signIn = (Button)view.findViewById(R.id.signin_btn);
        signIn.setOnClickListener(this);

        emailTxt = (TextInputLayout)view.findViewById(R.id.email_txt);
        passTxt  = (TextInputLayout)view.findViewById(R.id.password_txt);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signin_btn:
                login();
                break;
        }
    }

    private void login() {
        if(!validateEmail()){
            return;
        }else if(!emailValidator(email)){
            emailTxt.setError("Please enter valid email");
        }else if(!validatePassword()){
            return;
        }else {
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
                    .url("http://dev-orders.ekuep.com/api/login/driver")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                    String id = user.getString("id");
                                    String token = jsonToken.getString("token");
                                    String email = user.getString("email");
                                    if(status.equals("200")){

                                   SharedPrefManager.getInstance(getActivity()).StoreUser(id,email,token);
                                   startActivity(new Intent(getContext(),MainActivity.class));

                                    }else{
                                        Toast.makeText(getContext(),"Invalid Credentials",Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                    }
                }
            });

        }
    }

    private boolean validateEmail(){
        email = emailTxt.getEditText().getText().toString().trim();
        if(email.isEmpty()){
            emailTxt.setError("Email Field can't be empty");
            return false;
        }else{
            emailTxt.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        password = passTxt.getEditText().getText().toString().trim();
        if(password.isEmpty()){
            passTxt.setError("Password Field can't be empty");
            return false;
        }else{
            passTxt.setError(null);
            return true;
        }
    }

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void isLogin(){



    }


}
