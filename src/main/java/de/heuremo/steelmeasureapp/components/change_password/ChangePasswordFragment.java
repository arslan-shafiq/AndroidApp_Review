package de.heuremo.steelmeasureapp.components.change_password;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
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

public class ChangePasswordFragment extends Fragment {
    @Inject
    AuthRepository authRepository;
    CommonApiComponent commonApiComponent;
    // ********* declarations *********
    private MaterialButton take_over_button, cancel_button;
    private TextInputLayout current_password_layout, new_password_layout, repeat_password_layout;
    private TextInputEditText current_password, new_password, repeat_password;
    private String userName = "";
    private ProgressDialog progressDialog;
    private Observer<? super AuthLoginDTO> changePasswordResponseObserver = (Observer<AuthLoginDTO>) authLoginDTO -> {

        if (authLoginDTO.getResponse() == AuthLoginResponeTypeEnum.SUCCESS) {
            Toast.makeText(getContext(), "Change Password Successful", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(getActivity(), R.id.fragment_container).navigate(R.id.action_changePasswordFragment_to_settingsFragment_dest);
            progressDialog.hide();
        } else {
            Toast.makeText(getContext(), authLoginDTO.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.hide();
        }

    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.commonApiComponent = DaggerCommonApiComponent.builder().build();
        commonApiComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        current_password_layout = (TextInputLayout) view.findViewById(R.id.old_password_layout);
        new_password_layout = (TextInputLayout) view.findViewById(R.id.new_password_layout);
        repeat_password_layout = (TextInputLayout) view.findViewById(R.id.retype_new_password_layout);
        current_password = (TextInputEditText) view.findViewById(R.id.old_password);
        new_password = (TextInputEditText) view.findViewById(R.id.new_password);
        repeat_password = (TextInputEditText) view.findViewById(R.id.retype_new_password);
        take_over_button = (MaterialButton) view.findViewById(R.id.take_over_button);
        cancel_button = (MaterialButton) view.findViewById(R.id.cancel_button);

        take_over_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                ;
            }
        });

        //Setting Emoji filter to all textinput fields
        TextInputEditText allTextInputFields[] = {current_password, new_password, repeat_password};
        for (int i = 0; i < allTextInputFields.length; i++) {
            allTextInputFields[i].setFilters(new InputFilter[]{
                    ValidatorHelper.getEmojiFilter()
            });
        }

        progressDialog = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Loading...");

        return view;
    }

    public void changePassword() {
        if (ValidatorHelper.isFieldEmpty(current_password, current_password_layout, getContext())
                && ValidatorHelper.validatePassword(new_password, new_password_layout, getContext())
                && ValidatorHelper.isFieldEmpty(repeat_password, repeat_password_layout, getContext())
                && ValidatorHelper.validatePassAndRepeatPassword(new_password, repeat_password, repeat_password_layout, getContext())) {

            progressDialog.show();
            userName = SharedPreferenceHelper.getJWTAuth(getContext());
            LiveData<AuthLoginDTO> data = authRepository.changePassword(userName, current_password.getText().toString(), new_password.getText().toString());
            data.observe(ChangePasswordFragment.this, changePasswordResponseObserver);

        }
    }

    public void cancel() {
        Navigation.findNavController(getActivity(), R.id.fragment_container).navigate(R.id.action_changePasswordFragment_to_settingsFragment_dest);
    }
}
