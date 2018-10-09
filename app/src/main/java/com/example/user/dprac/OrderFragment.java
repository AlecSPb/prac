package com.example.user.dprac;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.user.dprac.Adapters.OrderHistoryAdapter;
import com.example.user.dprac.models.OrderHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {

RecyclerView today_order_list,yesterday_order_list;
OrderHistoryAdapter orderHistoryAdapter;
OrderHistoryAdapter orderHistoryAdapter1;
List<OrderHistory> orderList;
List<OrderHistory> yesterday_list;


    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_order, container, false);
        today_order_list = (RecyclerView)view.findViewById(R.id.today_order_list);
        today_order_list.hasFixedSize();
        orderList = new ArrayList<>();
        orderHistoryAdapter = new OrderHistoryAdapter(getActivity(), orderList);
        today_order_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        today_order_list.setAdapter(orderHistoryAdapter);
        new MyTask().execute();

        return view;
    }


//    public void prepareTodayList(){
//        for(int i=0;i<3;i++){
//            OrderHistory order = new OrderHistory(R.mipmap.product_image1,"Order # 965874582","Nuovo Simoneli Appia II Semiautomatic 2 group Espresso",
//                    "Tuesday 7th September 2018","09:34 PM");
//            orderList.add(order);
//        }
//
//    }

    class MyTask extends AsyncTask<Void,JSONObject,Void>{
        JSONObject reader = null;
        JSONArray order_data;

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected Void doInBackground(Void... voids) {
            getAssignOrders();
            String data = SharedPrefManager.getInstance(getContext()).getOrderData();

            try {
                reader = new JSONObject(data);
                order_data =reader.getJSONArray("orders");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i=0;i<order_data.length();i++){
                try {
                    JSONObject jsonObject = order_data.getJSONObject(i);
                    Log.d("###",String.valueOf(jsonObject));
                    publishProgress(jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONObject... values) {
            OrderHistory order = null;
            try {
                order = new OrderHistory(R.mipmap.product_image1,"Order # "+values[0].getString("order_id"),
                        "Nuovo Simoneli Appia II Semiautomatic 2 group Espresso",
                        "Tuesday 7th September 2018","09:34 PM");
                Log.d("####",values[0].getString("order_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

                orderList.add(order);
               orderHistoryAdapter.notifyDataSetChanged();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ((HistoryFragment)getParentFragment()).dialog.dismiss();
        }
    }

    public void getAssignOrders(){
        String id = SharedPrefManager.getInstance(getContext()).getUserId();
        OkHttpClient okHttpClient = new OkHttpClient();
        String url =Constants.get_assigned_orders+12;
        final Request request = new Request.Builder()
                .url(url)
                .header("Authorization","Bearer "+SharedPrefManager.getInstance(getContext()).getToken())
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful()){
                    final String data = response.body().string();


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPrefManager.getInstance(getContext()).StoreOrderData(data);
                                }

                    });
                }
            }
        });


    }




}
