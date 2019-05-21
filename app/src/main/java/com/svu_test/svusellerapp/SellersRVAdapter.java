package com.svu_test.svusellerapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SellersRVAdapter extends RecyclerView.Adapter<SellersRVAdapter.ViewHolder> {

    private Context context;
    private ArrayList<SellerModel> sellerModels;
    private SellerActivity activity;

    public SellersRVAdapter(SellerActivity activity, Context context, ArrayList<SellerModel> sellerModels) {
        this.context = context;
        this.activity = activity;
        this.sellerModels = sellerModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_seller, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final SellerModel sellerModel = sellerModels.get(i);

        viewHolder.image.setImageBitmap(sellerModel.getImage());
        viewHolder.name.setText(sellerModel.getName());
        viewHolder.number.setText(sellerModel.getNumber());

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DBHelper(context).deleteSeller(sellerModel.getId());
                SellersRVAdapter.this.activity.recreate();
            }
        });

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.sellerModels.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, number;
        private ImageView delete, edit, image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            number = itemView.findViewById(R.id.sellerNumberTV);
            name = itemView.findViewById(R.id.sellerNameTV);
            delete = itemView.findViewById(R.id.sellerDeleteIV);
            edit = itemView.findViewById(R.id.sellerEditIV);
            image = itemView.findViewById(R.id.sellerIV);
        }
    }
}
