package de.heuremo.steelmeasureapp.components.order_history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

import de.heuremo.R;
import de.heuremo.commons.common.model.order.ColliList;
import de.heuremo.commons.common.model.order.DropOffInformation;
import de.heuremo.commons.common.model.order.OrderDTO;
import de.heuremo.commons.common.model.order.PickupInformation;

public class OrderDetailsFragment extends Fragment {

    // ********* declarations *********
    private MaterialCheckBox pickup_deviation_checkbox, delivery_deviation_checkbox;
    private TextView pickup_date_textview, delivery_date_textview, pickup_address_textview, delivery_address_textview, commission_textView, est_weight_textView;
    private ImageView delivery_note_image;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ColliList> listitems;
    private PickupInformation pickupInformation;
    private DropOffInformation dropOffInformation;

    private String imageName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        getActivity().setTitle("Order Details");

        // *********  initialization of views  *********
        pickup_deviation_checkbox = (MaterialCheckBox) view.findViewById(R.id.pickup_deviation_checkbox);
        delivery_deviation_checkbox = (MaterialCheckBox) view.findViewById(R.id.delivery_deviation_checkbox);
        pickup_date_textview = (TextView) view.findViewById(R.id.pickup_date_TextView);
        delivery_date_textview = (TextView) view.findViewById(R.id.delivery_date_TextView);
        pickup_address_textview = (TextView) view.findViewById(R.id.pickup_Address_TextView);
        delivery_address_textview = (TextView) view.findViewById(R.id.delivery_Address_TextView);
        commission_textView = (TextView) view.findViewById(R.id.commision_text);
        est_weight_textView = (TextView) view.findViewById(R.id.est_weight_text);

        // getting order from Arguments sent from order history fragment

        OrderDTO orderDTO = getArguments().getParcelable("OrderDTO");
        commission_textView.setText(orderDTO.getCommission().toString());
        //est_weight_textView.setText(orderDTO.getEstimatedWeight().toString());

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        listitems = new ArrayList<>();
        loadRecyclerViewData();
        adapter = new OrderDetailsMeasurementsListAdapter(listitems, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void loadRecyclerViewData() {
        for (int i = 0; i < 10; i++) {
            ColliList listitem = new ColliList(14, 13, 16, 1, "m");
            listitems.add(listitem);
        }
    }


}
