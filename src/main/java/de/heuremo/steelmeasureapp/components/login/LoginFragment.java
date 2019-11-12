package de.heuremo.steelmeasureapp.components.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import javax.inject.Inject;

import de.heuremo.R;
import de.heuremo.commons.common.api.AuthRepository;
import de.heuremo.commons.common.api.CommonApiComponent;
import de.heuremo.commons.common.api.DaggerCommonApiComponent;
import de.heuremo.commons.common.constant.SharedPreferencesConstant;
import de.heuremo.commons.common.helpers.SharedPreferenceHelper;
import de.heuremo.commons.common.model.AuthLoginDTO;
import de.heuremo.commons.common.model.AuthLoginResponeTypeEnum;
import de.heuremo.commons.common.helpers.ValidatorHelper;
import de.heuremo.steelmeasureapp.components.ActivityMain;
import de.heuremo.steelmeasureapp.components.measureactivity.ArMeasureActivity;

public class LoginFragment extends Fragment {

    @Inject
    AuthRepository authRepository;
    CommonApiComponent commonApiComponent;
    // ********* declarations *********
    private TextInputEditText customer_number, user_name, password;
    private TextInputLayout customer_number_layout, user_name_layout, password_layout;
    private Button login_button;
    private TextView forget_password_button;
    private String customerNumber = "";
    private ProgressDialog progressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.commonApiComponent = DaggerCommonApiComponent.builder().build();
        commonApiComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        // *********  initialization of views  *********
        login_button = (Button) view.findViewById(R.id.login_button);
        forget_password_button = (TextView) view.findViewById(R.id.forget_password_button);
        customer_number = (TextInputEditText) view.findViewById(R.id.customer_name);
        user_name = (TextInputEditText) view.findViewById(R.id.user_name);
        customer_number_layout = (TextInputLayout) view.findViewById(R.id.customer_name_layout);
        user_name_layout = (TextInputLayout) view.findViewById(R.id.user_name_layout);
        password_layout = (TextInputLayout) view.findViewById(R.id.login_password_layout);
        password = (TextInputEditText) view.findViewById(R.id.login_password);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ValidatorHelper.isFieldEmpty(user_name, user_name_layout, getContext())
                        && ValidatorHelper.isFieldEmpty(password, password_layout, getContext())) {
                    progressDialog.show();
                    logIn(user_name.getText().toString(), password.getText().toString());
                }
            }
        });

        forget_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Forget Password Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //Setting Emoji filter to all textinput fields
        TextInputEditText allTextInputFields[] = {customer_number, user_name, password};
        for (int i = 0; i < allTextInputFields.length; i++) {
            allTextInputFields[i].setFilters(new InputFilter[]{
                    ValidatorHelper.getEmojiFilter()
            });
        }

        customerNumber = SharedPreferenceHelper.getInvitationKey(getContext());
        customer_number.setText(customerNumber);
        progressDialog = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Loading...");
        return view;
    }

    public void logIn(String userName, String password) {
        LiveData<AuthLoginDTO> data = authRepository
                .createToken(userName, password);

        data.observe(LoginFragment.this, loginResponseObserver);
    }

    private Observer<? super AuthLoginDTO> loginResponseObserver = (Observer<AuthLoginDTO>) authLoginDTO -> {

        if (authLoginDTO.getResponse() == AuthLoginResponeTypeEnum.SUCCESS) {
            progressDialog.hide();
            String token = authLoginDTO.getEncodedToken();
            SharedPreferenceHelper.saveLoggedInUserInfo(getContext(), user_name.getText().toString(), password.getText().toString(), token);
            // Intent intent = new Intent(getActivity(), ArMeasureActivity.class);
            Intent intent = new Intent(getActivity(), ActivityMain.class);
            startActivity(intent);
        } else {
            progressDialog.hide();
            Toast.makeText(getContext(), authLoginDTO.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

}
