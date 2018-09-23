package com.example.user.dprac.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.dprac.R;
import com.example.user.dprac.models.OrderHistory;


import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private Context context;
    private List<OrderHistory> orderList;

    public OrderHistoryAdapter(Context context, List<OrderHistory> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,order_no,date,time;
        ImageView product_image;
        public ViewHolder(View itemView) {
            super(itemView);
            time = (TextView)itemView.findViewById(R.id.time);
            order_no = (TextView)itemView.findViewById(R.id.order_no);
            name = (TextView)itemView.findViewById(R.id.product_desc);
            date = (TextView)itemView.findViewById(R.id.date);
            product_image = (ImageView)itemView.findViewById(R.id.product_image);
        }
    }
    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_history,parent,false);
        return new OrderHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {

        OrderHistory order = orderList.get(position);
         holder.name.setText(order.getName());
        holder.date.setText(order.getDate());
        holder.time.setText(order.getTime());
        holder.order_no.setText(order.getOrder_no());
        holder.product_image.setImageResource(R.mipmap.product_image1);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
