package de.heuremo.steelmeasureapp.components.makeorder;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import de.heuremo.R;
import de.heuremo.commons.common.api.CommonApiComponent;
import de.heuremo.commons.common.api.DaggerCommonApiComponent;
import de.heuremo.commons.common.constant.SharedPreferencesConstant;
import de.heuremo.commons.common.helpers.SharedPreferenceHelper;
import de.heuremo.commons.common.model.file_storage.FileStorageDTO;
import de.heuremo.commons.common.model.order.ColliList;
import de.heuremo.commons.common.model.order.DropOffInformation;
import de.heuremo.commons.common.model.order.OrderDTO;
import de.heuremo.commons.common.model.order.PickupInformation;
import de.heuremo.commons.common.repository.MakeOrderRepository;
import de.heuremo.commons.common.helpers.ValidatorHelper;
import de.heuremo.steelmeasureapp.components.measureactivity.BundledMeasureValues;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MakeOrderFragment extends Fragment {

    private static final int ARMEASUREACTIVITY = 102;
    private static final int CAMERA_IMAGE_REQUEST = 101;
    String pathToFile;
    @Inject
    MakeOrderRepository makeOrderRepository;
    CommonApiComponent commonApiComponent;
    // ********* declarations *********
    private MaterialCheckBox pickup_deviation_checkbox, delivery_deviation_checkbox;
    private TextView pickup_date_textview, delivery_date_textview;
    private ImageView info_icon, camera_icon, pickup_date_button, delivery_date_button, delivery_note_result;
    private MaterialButton create_order_button;
    private TextInputLayout commission_text_input_layout, weight_inputtext_layout, pickup_adress_layout, delivery_adress_layout;
    private TextInputEditText commission_edittext, weight_inputtext, pickup_adressinputtext, delivery_adressinputtext;
    private RelativeLayout pickup_date_layout, delivery_date_layout;
    public RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ColliList> measurementsList;
    private PickupInformation pickupInformation;
    private DropOffInformation dropOffInformation;
    private String imageName;
    private Calendar pickUpDate;
    private Calendar dropDate;
    private int est_weight = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.commonApiComponent = DaggerCommonApiComponent.builder().build();
        commonApiComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_order, container, false);
        getActivity().setTitle("Order");

        // *********  initialization of views  *********
        pickup_deviation_checkbox = (MaterialCheckBox) view.findViewById(R.id.pickup_deviation_checkbox);
        delivery_deviation_checkbox = (MaterialCheckBox) view.findViewById(R.id.delivery_deviation_checkbox);
        pickup_date_button = (ImageView) view.findViewById(R.id.pickup_date_button);
        delivery_date_button = (ImageView) view.findViewById(R.id.delivery_date_button);
        pickup_date_textview = (TextView) view.findViewById(R.id.pickup_date_TextView);
        delivery_date_textview = (TextView) view.findViewById(R.id.delivery_date_TextView);
        info_icon = (ImageView) view.findViewById(R.id.info_icon_make_order);
        camera_icon = (ImageView) view.findViewById(R.id.delivery_note_camera);
        create_order_button = (MaterialButton) view.findViewById(R.id.create_order_button);
        commission_text_input_layout = (TextInputLayout) view.findViewById(R.id.comission_inputtext_layout);
        commission_edittext = (TextInputEditText) view.findViewById(R.id.comission_inputtext);
        weight_inputtext_layout = (TextInputLayout) view.findViewById(R.id.weight_inputtext_layout);
        weight_inputtext = (TextInputEditText) view.findViewById(R.id.weight_inputtext);
        pickup_adress_layout = (TextInputLayout) view.findViewById(R.id.pickup_adress_layout);
        delivery_adress_layout = (TextInputLayout) view.findViewById(R.id.delivery_adress_layout);
        pickup_adressinputtext = (TextInputEditText) view.findViewById(R.id.pickup_adress_inputtext);
        delivery_adressinputtext = (TextInputEditText) view.findViewById(R.id.delivery_adress_inputtext);
        pickup_date_layout = (RelativeLayout) view.findViewById(R.id.pick_date_layout);
        delivery_date_layout = (RelativeLayout) view.findViewById(R.id.delivery_date_layout);
        delivery_note_result = (ImageView) view.findViewById(R.id.delivery_note_image);

        setDefaultDates();

        weight_inputtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWeightPicker();
            }
        });

        pickup_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickUpDateClicked();
            }
        });

        pickup_date_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickUpDateClicked();
            }
        });

        delivery_date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeliveryDateClicked();
            }
        });

        delivery_date_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeliveryDateClicked();
            }
        });

        camera_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                captureImage();
            }
        });

        info_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInfoDialogue();
            }
        });

        pickup_deviation_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean deviation = ((MaterialCheckBox) v).isChecked();
                pickUpDeviation(deviation);
            }
        });

        delivery_deviation_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean deviation = ((MaterialCheckBox) v).isChecked();
                deliveryDeviation(deviation);
            }
        });

        create_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrder();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        measurementsList = new ArrayList<>();

        loadRecyclerViewData();
        adapter = new ListAdapter(measurementsList, getContext());
        recyclerView.setAdapter(adapter);

        //Setting Emoji filter to all textinput fields
        TextInputEditText allTextInputFields[] = {commission_edittext, weight_inputtext, pickup_adressinputtext, delivery_adressinputtext};
        for (int i = 0; i < allTextInputFields.length; i++) {
            allTextInputFields[i].setFilters(new InputFilter[]{
                    ValidatorHelper.getEmojiFilter()
            });
        }
        return view;
    }

    public void captureImage() {

        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createPhotoFile();

            if (photoFile != null) {
                pathToFile = photoFile.getAbsolutePath();
                Uri photoURL = FileProvider.getUriForFile(getActivity(), "de.heuremo.steelmeasureapp.components.fileprovider", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoURL);
                startActivityForResult(takePic, 1);
            }
        }

    }

    public File createPhotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        File storage = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storage);
        } catch (IOException e) {
            Log.d("CameraLog", "Excep : " + e.toString());
        }

        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {

            final Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
            delivery_note_result.setImageBitmap(bitmap);

            File file = new File(pathToFile);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            RequestBody fullName =
                    RequestBody.create(MediaType.parse("text/plain"), pathToFile);
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            String auth = SharedPreferenceHelper.getJWTAuth(getContext());
            final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
            progressDialog.setMessage("Loading Data...");
            progressDialog.show();
            LiveData<FileStorageDTO> response = makeOrderRepository.uploadNotes(body, fullName, auth);


            response.observe(MakeOrderFragment.this, fileStorageDTO -> {
                if (fileStorageDTO != null) {
                    Toast.makeText(getContext(), "File Uploaded!", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Not Uploaded!", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });


        } else {
            Toast.makeText(getActivity(), "Take Picture Failed or canceled",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void loadRecyclerViewData() {

       /* Activity activity = Objects.requireNonNull(getActivity());

        if (!activity.getIntent().hasExtra(IntentConstants.MEASURED_ITEMS)) {
            throw new IllegalStateException("Cannot work without extra: " + IntentConstants.MEASURED_ITEMS);
        }

        Intent intent = activity.getIntent();

        Gson gson = new GsonBuilder().create();


        ArrayList<String> strings = intent.getStringArrayListExtra(IntentConstants.MEASURED_ITEMS);

        List<BundledMeasureValues> bundledMeasureValues = new ArrayList<>();

        for (String string : strings) {
            bundledMeasureValues.add(gson.fromJson(string, BundledMeasureValues.class));
        }

        List<ColliList> colliList = mapToColliList(bundledMeasureValues);

        measurementsList.addAll(colliList);*/

        for (int i = 0; i < 10; i++) {
            ColliList listitem = new ColliList(14, 13, 16, 1, "m");
            measurementsList.add(listitem);
        }
    }

    public List<ColliList> mapToColliList(List<BundledMeasureValues> bundledMeasureValues) {
        List<ColliList> colliLists = new ArrayList<>();

        for (BundledMeasureValues bundledMeasureValues1 : bundledMeasureValues) {
            ColliList colliList = new ColliList(Math.round(bundledMeasureValues1.getHeight()),
                    Math.round(bundledMeasureValues1.getLength()),
                    Math.round(bundledMeasureValues1.getWidth()), 1, "");
            colliLists.add(colliList);
        }

        return colliLists;
    }

    public void onPickUpDateClicked() {

        Calendar currentDate = Calendar.getInstance();
        if (pickUpDate == null) {
            pickUpDate = currentDate;
        }
        int year = pickUpDate.get(Calendar.YEAR);
        int month = pickUpDate.get(Calendar.MONTH);
        int day = pickUpDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog pickupDatePicker = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, monthOfYear);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                pickUpDate = selectedDate;
                String currentDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(selectedDate.getTime());
                pickup_date_textview.setText(currentDate);
            }
        }, year, month, day);

        pickupDatePicker.setVersion(DatePickerDialog.Version.VERSION_2);
        pickupDatePicker.setMinDate(currentDate);
        pickupDatePicker.setOkColor(Color.parseColor("#7CB973"));
        pickupDatePicker.setAccentColor(Color.parseColor("#7CB973"));
        pickupDatePicker.show(getFragmentManager(), "DatePickerDialog");
    }

    public void onDeliveryDateClicked() {

        Calendar today = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DAY_OF_MONTH, 1);
        if (dropDate == null) {
            dropDate = currentDate;
        }
        int year = dropDate.get(Calendar.YEAR);
        int month = dropDate.get(Calendar.MONTH);
        int day = dropDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog pickupDatePicker = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, monthOfYear);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dropDate = selectedDate;
                String currentDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(selectedDate.getTime());
                delivery_date_textview.setText(currentDate);
            }
        }, year, month, day);

        pickupDatePicker.setVersion(DatePickerDialog.Version.VERSION_2);
        pickupDatePicker.setMinDate(today);
        pickupDatePicker.setOkColor(Color.parseColor("#7CB973"));
        pickupDatePicker.setAccentColor(Color.parseColor("#7CB973"));
        pickupDatePicker.show(getFragmentManager(), "DatePickerDialog");
    }

    public void showWeightPicker() {

        final Dialog d = new Dialog(getActivity());
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_number_picker);
        Button b1 = (Button) d.findViewById(R.id.set_button);
        Button b2 = (Button) d.findViewById(R.id.cancel_button);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                int temp = value * 100;
                return "" + temp;
            }
        };
        np.setFormatter(formatter);
        np.setMaxValue(500); // max value 100
        np.setMinValue(0);// min value 0
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                est_weight = np.getValue();
                weight_inputtext.setText(String.valueOf(np.getValue() * 100)); //set the value to textview
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();


    }

    public void openInfoDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Information")
                .setMessage("This is dummy Text. Text will be provided by HEUREMU later")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.show();
    }

    public void pickUpDeviation(Boolean deviation) {

        if (deviation) {
            pickup_adress_layout.setVisibility(View.VISIBLE);
            pickup_date_layout.setVisibility(View.VISIBLE);

        } else {
            pickup_adress_layout.setVisibility(View.GONE);
            pickup_date_layout.setVisibility(View.GONE);
        }
    }

    public void deliveryDeviation(Boolean deviation) {

        if (deviation) {
            delivery_adress_layout.setVisibility(View.VISIBLE);
            delivery_date_layout.setVisibility(View.VISIBLE);

        } else {
            delivery_adress_layout.setVisibility(View.GONE);
            delivery_date_layout.setVisibility(View.GONE);
        }
    }

    public void createOrder() {

        pickupInformation = new PickupInformation();
        dropOffInformation = new DropOffInformation();
        pickupInformation.setDeliveryDateDeviating(pickup_deviation_checkbox.isChecked());
        dropOffInformation.setDeliveryDateDeviating(delivery_deviation_checkbox.isChecked());


        if (ValidatorHelper.isFieldEmpty(commission_edittext, commission_text_input_layout, getContext())
                && ValidatorHelper.isFieldEmpty(weight_inputtext, weight_inputtext_layout, getContext())
                && ValidatorHelper.isFieldEmpty(pickup_adressinputtext, pickup_adress_layout, getContext())
                && ValidatorHelper.isFieldEmpty(delivery_adressinputtext, delivery_adress_layout, getContext())) {
            OrderDTO newOrder = new OrderDTO();
            pickupInformation.setDesiredDeliveryDate(pickUpDate.toString());
            pickupInformation.setStreetAddress(pickup_adressinputtext.getText().toString());
            dropOffInformation.setDesiredDeliveryDate(dropDate.toString());
            dropOffInformation.setStreetAddress(delivery_adressinputtext.getText().toString());
            newOrder.setCommission(commission_edittext.getText().toString());
            newOrder.setEstimatedWeight(est_weight);
            newOrder.setColliList(measurementsList);

            final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            LiveData<OrderDTO> response = makeOrderRepository.postOrder(newOrder);
            response.observe(MakeOrderFragment.this, orderDTO -> {
                if (orderDTO != null) {
                    Toast.makeText(getContext(), "Oder Successful!", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Oder Not Successful!", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    public void setDefaultDates() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 1); //Adds a day
        Date today = cal.getTime();
        String currentDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(today);
        delivery_date_textview.setText(currentDate);
        String nextdayDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(new Date());
        pickup_date_textview.setText(nextdayDate);
        pickUpDate = cal;
        dropDate = cal;
    }
}
