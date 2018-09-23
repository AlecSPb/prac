package com.example.user.dprac.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.dprac.R;
import com.example.user.dprac.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       TextView name,serial_no,quantity,price,number;
        public ViewHolder(View itemView) {
            super(itemView);
            number = (TextView)itemView.findViewById(R.id.number);
            name = (TextView)itemView.findViewById(R.id.product_title);
            serial_no = (TextView)itemView.findViewById(R.id.serial_number);
            quantity = (TextView)itemView.findViewById(R.id.quantity);
            price = (TextView)itemView.findViewById(R.id.price);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

           Product product = productList.get(position);
           holder.name.setText(product.getTitle());
           holder.price.setText(product.getPrice());
           holder.quantity.setText(product.getQuantity());
           holder.serial_no.setText(product.getSerial_no());
           int no = position+1;
           holder.number.setText(String.valueOf(no)+" .");
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


}
