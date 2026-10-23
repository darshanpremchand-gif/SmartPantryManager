package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantrymanager.data.PantryItem;

import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    // the ingredients shown in the list
    private List<PantryItem> items = new ArrayList<>();

    // swap in new data and refresh the list
    public void setItems(List<PantryItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    // make a blank row using item_pantry
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    // fills a row with the data of one ingredient
    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = items.get(position);

        holder.textName.setText(item.getName());
        holder.textQuantity.setText(item.getQuantity() + " " + item.getUnit());

        // only show the expiry line if the user entered a date
        if (item.getExpiryDate() != null) {
            holder.textExpiry.setText("Expires: " + item.getExpiryDate());
            holder.textExpiry.setVisibility(View.VISIBLE);
        } else {
            holder.textExpiry.setVisibility(View.GONE);
        }
    }

    // tells the list how many rows there are
    @Override
    public int getItemCount() {
        return items.size();
    }

    // holds the 3 text boxes of one row
    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textQuantity, textExpiry;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textItemName);
            textQuantity = itemView.findViewById(R.id.textItemQuantity);
            textExpiry = itemView.findViewById(R.id.textItemExpiry);
        }
    }
}