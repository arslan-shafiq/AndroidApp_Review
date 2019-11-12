package de.heuremo.steelmeasureapp.components.order_history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.heuremo.R;
import de.heuremo.commons.common.model.order.ColliList;

public class OrderDetailsMeasurementsListAdapter extends RecyclerView.Adapter<OrderDetailsMeasurementsListAdapter.ViewHolder> {

    private List<ColliList> listitems;
    private Context context;

    public OrderDetailsMeasurementsListAdapter(List<ColliList> listitems, Context context) {
        this.listitems = listitems;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_details_measurements_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ColliList listitem = listitems.get(position);

        String iWidth = Integer.toString(listitem.getBoundingWidth()) + " cm" ;
        String iLength = Integer.toString(listitem.getBoundingLength()) + " cm";
        String iHeight = Integer.toString(listitem.getBoundingHeight()) + " cm";
        String iColli = Integer.toString(listitem.getCount());

        holder.textViewWidth.setText(iWidth);
        holder.textViewLength.setText(iLength);
        holder.textViewHeight.setText(iHeight);
        holder.textViewColli.setText(iColli);
    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewWidth, textViewLength, textViewHeight, textViewColli;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWidth = (TextView) itemView.findViewById(R.id.width);
            textViewLength = (TextView) itemView.findViewById(R.id.length);
            textViewHeight = (TextView) itemView.findViewById(R.id.height);
            textViewColli = (TextView) itemView.findViewById(R.id.colli);
        }
    }
}
