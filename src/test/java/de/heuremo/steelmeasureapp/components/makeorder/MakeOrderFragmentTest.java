package de.heuremo.steelmeasureapp.components.makeorder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.List;


import de.heuremo.commons.common.model.order.ColliList;
import de.heuremo.steelmeasureapp.components.measureactivity.BundledMeasureValues;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MakeOrderFragmentTest {

    @Mock
    private MakeOrderFragment makeOrderFragment;

    @Mock
    List<BundledMeasureValues> bundledMeasureValues;

    @Mock
    List<ColliList> mColliList;

    @Mock
    File image;

    @Test
    public void testMapToColliList() {
        when(makeOrderFragment.mapToColliList(bundledMeasureValues)).thenReturn(mColliList);
    }

    @Test
    public void testCreateOrder() {
        doNothing().when(makeOrderFragment).createOrder();
    }

    @Test
    public void testSetDefaultDates() {
        doNothing().when(makeOrderFragment).setDefaultDates();
    }

    @Test
    public void testDeliveryDeviation() {
        doNothing().when(makeOrderFragment).deliveryDeviation(true);
    }

    @Test
    public void testPickUpDeviation() {
        doNothing().when(makeOrderFragment).pickUpDeviation(true);
    }

    @Test
    public void testOpenInfoDialogue() {
        doNothing().when(makeOrderFragment).openInfoDialogue();
    }

    @Test
    public void testShowWeightPicker() {
        doNothing().when(makeOrderFragment).showWeightPicker();
    }

    @Test
    public void testOnDeliveryDateClicked() {
        doNothing().when(makeOrderFragment).onDeliveryDateClicked();
    }

    @Test
    public void testOnPickUpDateClicked() {
        doNothing().when(makeOrderFragment).onPickUpDateClicked();
    }

    @Test
    public void testLoadRecyclerViewData() {
        doNothing().when(makeOrderFragment).loadRecyclerViewData();
    }

    @Test
    public void testCreatePhotoFile() {
        when(makeOrderFragment.createPhotoFile()).thenReturn(image);
    }

    @Test
    public void testCaptureImage() {
        doNothing().when(makeOrderFragment).captureImage();
    }
}