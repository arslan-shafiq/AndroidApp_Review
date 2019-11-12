package de.heuremo.steelmeasureapp.components.makeorder;

import android.content.Context;
import android.widget.TextView;

import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import de.heuremo.commons.common.helpers.MockEditable;
import de.heuremo.commons.common.model.order.ColliList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ListAdapterTest {

    @Mock
    private ListAdapter listAdapter;

    @Mock
    private Context context;

    @Mock
    ListAdapter.ViewHolder holder;

    @Mock
    private TextView textViewColli;

    @Test
    public void testGetItemCount() {
        List<ColliList> measurementsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ColliList listitem = new ColliList(14, 13, 16, 1, "m");
            measurementsList.add(listitem);
        }
        listAdapter = new ListAdapter(measurementsList, context);
        assertThat(listAdapter.getItemCount(),equalTo(10));
    }

}