package de.heuremo.steelmeasureapp.components.makeorder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.heuremo.R;
import de.heuremo.commons.common.model.order.ColliList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<ColliList> listitems;
    private Context context;

    public ListAdapter(List<ColliList> listitems, Context context) {
        this.listitems = listitems;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.make_order_measurements_list_item, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ColliList listitem = listitems.get(position);
        holder.textViewWidth.setText(listitem.getBoundingWidth() + " cm");
        holder.textViewLength.setText(listitem.getBoundingLength() + " cm");
        holder.textViewHeight.setText(listitem.getBoundingHeight() + " cm");
        holder.textViewColli.setText(Integer.toString(listitem.getCount()));

        holder.plus_icon.setOnClickListener(view -> {
            String coli = holder.textViewColli.getText().toString();
            holder.textViewColli.setText(Integer.toString(Integer.valueOf(coli) + 1));

        });

        holder.minus_icon.setOnClickListener(view -> {

            String coli = holder.textViewColli.getText().toString();
            if (Integer.valueOf(coli) > 1) {
                holder.textViewColli.setText(Integer.toString(Integer.valueOf(coli) - 1));
            } else {
                removeMeasurement(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    private void removeMeasurement(int position) {
        listitems.remove(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewWidth, textViewLength, textViewHeight, textViewColli;
        public ImageView plus_icon, minus_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWidth = itemView.findViewById(R.id.width);
            textViewLength = itemView.findViewById(R.id.length);
            textViewHeight = itemView.findViewById(R.id.height);
            textViewColli = itemView.findViewById(R.id.colli);
            plus_icon = itemView.findViewById(R.id.plus_button);
            minus_icon = itemView.findViewById(R.id.minus_button);
        }
    }
}
