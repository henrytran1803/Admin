package com.example.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> productList;

    public ProductAdapter(Context context, ArrayList<Product> productList) {
        super(context, 0, productList);
        this.productList = productList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_view, parent, false);
        }

        TextView idTextView = convertView.findViewById(R.id.idproduct);
        TextView nameTextView = convertView.findViewById(R.id.nameproduct);
        TextView priceTextView = convertView.findViewById(R.id.priceproduct);

        idTextView.setText(product.getId());
        nameTextView.setText(product.getName());
        priceTextView.setText(product.getPrice()+"");

        return convertView;
    }
}
