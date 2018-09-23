package com.example.user.dprac;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.example.user.dprac.Adapters.OrderHistoryAdapter;
import com.example.user.dprac.models.OrderHistory;

import java.util.ArrayList;
import java.util.List;


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
        yesterday_order_list = (RecyclerView)view.findViewById(R.id.yesterday_order_list);

        today_order_list.hasFixedSize();
        yesterday_order_list.hasFixedSize();

        orderList = new ArrayList<>();
        yesterday_list = new ArrayList<>();
        orderHistoryAdapter = new OrderHistoryAdapter(getActivity(), orderList);
        orderHistoryAdapter1 = new OrderHistoryAdapter(getActivity(),yesterday_list);

        today_order_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        yesterday_order_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        today_order_list.setAdapter(orderHistoryAdapter);
        yesterday_order_list.setAdapter(orderHistoryAdapter);

        prepareTodayList();
        prepareYesterdayList();
        return view;
    }


    public void prepareTodayList(){
        for(int i=0;i<3;i++){
            OrderHistory order = new OrderHistory(R.mipmap.product_image,"Order # 965874582","Nuovo Simoneli Appia II Semiautomatic 2 group Espresso",
                    "Tuesday 7th September 2018","09:34 PM");
            orderList.add(order);
        }

    }

    public void prepareYesterdayList(){
        for(int i=0;i<3;i++){
            OrderHistory order = new OrderHistory(R.mipmap.product_image,"Order # 965874582","Nuovo Simoneli Appia II Semiautomatic 2 group Espresso",
                    "Tuesday 7th September 2018","09:34 PM");
            yesterday_list.add(order);
        }

    }
}
