package de.heuremo.steelmeasureapp.components.order_history;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import de.heuremo.R;
import de.heuremo.commons.common.api.CommonApiComponent;
import de.heuremo.commons.common.api.DaggerCommonApiComponent;
import de.heuremo.commons.common.constant.SharedPreferencesConstant;
import de.heuremo.commons.common.helpers.SharedPreferenceHelper;
import de.heuremo.commons.common.model.order.OrderDTO;
import de.heuremo.commons.common.repository.OrderHistoryRepository;
import de.heuremo.commons.common.viewmodel.OrderHistoryViewModel;

public class PickupOrdersFragment extends Fragment implements OrderHistoryAdapter.OnItemClickListner {

    private static final String URL_DATA_API = "https://simplifiedcoding.net/demos/marvel/";
    ProgressDialog dialog;
    @Inject
    OrderHistoryRepository orderHistoryRepository;
    CommonApiComponent commonApiComponent;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private OrderHistoryViewModel orderHistoryViewModel;
    private List<OrderDTO> allOrders;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.commonApiComponent = DaggerCommonApiComponent.builder().build();
        commonApiComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        getActivity().setTitle("History");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        allOrders = new ArrayList<>();

        Integer limit = 5;
        String auth = SharedPreferenceHelper.getJWTAuth(getContext());
        LiveData<List<OrderDTO>> response = orderHistoryRepository.getAllOrders(auth, limit);
        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        response.observe(this, orderDTOS -> {

            allOrders = orderDTOS;
            adapter = new OrderHistoryAdapter(allOrders, getContext());
            ((OrderHistoryAdapter) adapter).setOnItemClickListener(this::onItemClick);
            recyclerView.setAdapter(adapter);
            progressDialog.dismiss();
        });


        setHasOptionsMenu(true);

        return view;
    }


    private void loadRecyclerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        for (int i = 0; i < 10; i++) {
            OrderDTO order = new OrderDTO();
            final int random = new Random().nextInt(10000) + 1000;
            int commision = (random + i) * i;
            order.setCommission(Integer.toString(commision));
            Integer day = 14 + i;
            Integer hours = 18 + i;
            Integer minutes = 29 + i;
            String createdDate = "Order from " + day + "-03-2019 at " + hours + ":" + minutes + " clock";
            order.setCreatedAt(createdDate);

            allOrders.add(order);
        }
        progressDialog.dismiss();
    }

    @Override
    public void onItemClick(int position) {

        OrderDTO order = allOrders.get(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable("OrderDTO", order);
        Navigation.findNavController(getActivity(), R.id.fragment_container).navigate(R.id.action_pickupOrdersFragment_dest_to_orderDetailsFragment, bundle);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        searchView.setQueryHint("Search by order date");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((OrderHistoryAdapter) adapter).getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void loadingDialog() {
        dialog = ProgressDialog.show(getActivity(), "",
                "Fetching. Please wait...", true);
    }

}
