package com.example.smartpantrymanager;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartpantrymanager.data.PantryItem;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    // lets the activity know which row was tapped
    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
    }

    private List<PantryItem> items = new ArrayList<>();
    private OnItemClickListener listener;

    // set from the Settings screen
    private boolean showExpiryAlerts = false;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // turn the expiring-soon alerts on or off
    public void setShowExpiryAlerts(boolean show) {
        this.showExpiryAlerts = show;
    }

    // replace the list and refresh the screen
    public void setItems(List<PantryItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    // make an empty row from item_pantry
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    // put one ingredient's details into a row
    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = items.get(position);

        holder.textName.setText(item.getName());
        holder.textQuantity.setText(item.getQuantity() + " " + item.getUnit());

        // only show expiry if the user entered one
        if (item.getExpiryDate() != null) {
            String text = "Expires: " + item.getExpiryDate();

            // flag it in red if alerts are on and it expires soon
            if (showExpiryAlerts && isExpiringSoon(item.getExpiryDate())) {
                text = text + " - expiring soon!";
                holder.textExpiry.setTextColor(Color.RED);
            } else {
                holder.textExpiry.setTextColor(holder.defaultExpiryColor);
            }

            holder.textExpiry.setText(text);
            holder.textExpiry.setVisibility(View.VISIBLE);
        } else {
            holder.textExpiry.setVisibility(View.GONE);
        }

        // when the row is tapped, tell the activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            }
        });
    }

    // true if the date is today, in the past, or within the next 3 days
    private boolean isExpiringSoon(String expiryDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        format.setLenient(false);

        try {
            Date expiry = format.parse(expiryDate);

            // today at midnight, so we compare whole days
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            long daysLeft = (expiry.getTime() - today.getTimeInMillis()) / (24L * 60 * 60 * 1000);
            return daysLeft <= 3;
        } catch (ParseException e) {
            return false;
        }
    }

    // number of rows
    @Override
    public int getItemCount() {
        return items.size();
    }

    // holds the text boxes for one row
    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textQuantity, textExpiry;
        int defaultExpiryColor;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textItemName);
            textQuantity = itemView.findViewById(R.id.textItemQuantity);
            textExpiry = itemView.findViewById(R.id.textItemExpiry);

            // remember the default colour so we can put it back
            defaultExpiryColor = textExpiry.getCurrentTextColor();
        }
    }
}