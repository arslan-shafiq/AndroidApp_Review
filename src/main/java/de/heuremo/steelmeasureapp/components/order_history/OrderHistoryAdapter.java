package de.heuremo.steelmeasureapp.components.order_history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.heuremo.R;
import de.heuremo.commons.common.model.order.OrderDTO;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> implements Filterable {

    private List<OrderDTO> allOrders;
    private List<OrderDTO> allOrdersFullList;
    private Context context;
    private OnItemClickListner mListener;
    private Filter orderFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<OrderDTO> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(allOrdersFullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (OrderDTO order : allOrdersFullList) {
                    if (order.getCreatedAt().toLowerCase().contains(filterPattern)) {
                        filteredList.add(order);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            allOrders.clear();
            allOrders.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public OrderHistoryAdapter(List<OrderDTO> allOrders, Context context) {
        this.allOrders = allOrders;
        this.context = context;
        allOrdersFullList = new ArrayList<>(allOrders);
    }

    public void setOnItemClickListener(OnItemClickListner listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDTO order = allOrders.get(position);
        holder.order_place_date.setText(order.getCreatedAt());
        holder.Comission_text.setText(order.getCommission());
    }

    @Override
    public int getItemCount() {
        return allOrders.size();
    }

    @Override
    public Filter getFilter() {
        return orderFilter;
    }

    public interface OnItemClickListner {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView order_place_date, Comission_text;
        public ImageView order_image_1, order_image_2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_place_date = (TextView) itemView.findViewById(R.id.order_place_date);
            Comission_text = (TextView) itemView.findViewById(R.id.Comission_text);
            order_image_1 = (ImageView) itemView.findViewById(R.id.order_history_image_1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
