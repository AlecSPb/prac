package com.example.user.dprac;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.dprac.Adapters.OrderHistoryAdapter;
import com.example.user.dprac.models.OrderHistory;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReturnFragment extends Fragment {
    OrderHistoryAdapter orderHistoryAdapter;
    List<OrderHistory> orderList;
    RecyclerView order_list;


    public ReturnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_return, container, false);
        order_list = (RecyclerView)view.findViewById(R.id.order_list);

        order_list.hasFixedSize();

        orderList = new ArrayList<>();
        orderHistoryAdapter = new OrderHistoryAdapter(getActivity(), orderList);

        order_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        order_list.setAdapter(orderHistoryAdapter);

        prepareOrderList();
        return view;
    }

    public void prepareOrderList(){
        for(int i=0;i<7;i++){
            OrderHistory order = new OrderHistory(R.mipmap.product_image1,"Order # 965874582","Nuovo Simoneli Appia II Semiautomatic 2 group Espresso",
                    "Tuesday 7th September 2018","09:34 PM");
            orderList.add(order);
        }

    }

}
