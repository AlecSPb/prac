package com.example.user.dprac;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static SharedPrefManager Instance;
    private static Context ctx;

    private static final String Key_Pref = "Key_Pref";
    private static final String Key_Id = "Key_id";
    private static final String Key_Email = "Key_Email";
    private static final String Key_Token = "Key_Token";
    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(Instance==null){
            Instance = new SharedPrefManager(context);
        }
        return Instance;
    }

    public boolean StoreUser(String id, String email,String token) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Key_Pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Key_Id,id);
        editor.putString(Key_Email, email);
        editor.putString(Key_Token,token);
        editor.apply();
        return true;


    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Key_Pref, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(Key_Token, null) != null) {
            return true;
        }
        return false;


    }

    public boolean Logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Key_Pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }


}
