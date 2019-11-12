package de.heuremo.steelmeasureapp.components.login.Registration;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import javax.inject.Inject;

import de.heuremo.R;
import de.heuremo.commons.common.api.CommonApiComponent;
import de.heuremo.commons.common.api.DaggerCommonApiComponent;
import de.heuremo.commons.common.constant.SharedPreferencesConstant;
import de.heuremo.commons.common.helpers.SharedPreferenceHelper;
import de.heuremo.commons.common.model.user_registration.User;
import de.heuremo.commons.common.model.user_registration.userRegistrationDTO;
import de.heuremo.commons.common.repository.RegistrationRepository;
import de.heuremo.commons.common.helpers.ValidatorHelper;
import de.heuremo.commons.common.viewmodel.RegistrationViewModel;

public class RegistrationFormFragment extends Fragment {

    @Inject
    RegistrationRepository registrationRepository;
    CommonApiComponent commonApiComponent;
    // ********* declarations *********
    private ScrollView scrollView;
    public TextInputEditText first_name, surname, email, mobileNumber, password, confirm_password, user_name;
    private TextInputLayout first_name_layout, surname_layout, email_layout, mobileNumber_layout, password_layout, confirm_password_layout, user_name_layout;
    private MaterialButton SingUpBtn;
    private RegistrationViewModel registrationViewModel;
    private String invitationKey = "";
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
        View view = inflater.inflate(R.layout.fragment_registration__form, container, false);

        // *********  initialization of views  *********
        first_name = (TextInputEditText) view.findViewById(R.id.firstname);
        surname = (TextInputEditText) view.findViewById(R.id.surname);
        email = (TextInputEditText) view.findViewById(R.id.email);
        mobileNumber = (TextInputEditText) view.findViewById(R.id.mobile_number);
        password = (TextInputEditText) view.findViewById(R.id.password);
        confirm_password = (TextInputEditText) view.findViewById(R.id.confirm_password);
        user_name = (TextInputEditText) view.findViewById(R.id.user_name);
        first_name_layout = (TextInputLayout) view.findViewById(R.id.firstname_layout);
        surname_layout = (TextInputLayout) view.findViewById(R.id.surname_layout);
        email_layout = (TextInputLayout) view.findViewById(R.id.email_layout);
        mobileNumber_layout = (TextInputLayout) view.findViewById(R.id.mobile_number_layout);
        password_layout = (TextInputLayout) view.findViewById(R.id.password_layout);
        confirm_password_layout = (TextInputLayout) view.findViewById(R.id.confirm_password_layout);
        user_name_layout = (TextInputLayout) view.findViewById(R.id.user_name_layout);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);


        registrationViewModel = ViewModelProviders.of(RegistrationFormFragment.this).get(RegistrationViewModel.class);


        SingUpBtn = (MaterialButton) view.findViewById(R.id.continue_button);
        SingUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUpButtonClick(view);
            }
        });

        //Setting Emoji filter to all textinput fields
        TextInputEditText allTextInputFields[] = {first_name, surname, email, mobileNumber, password, confirm_password, user_name};
        for (int i = 0; i < allTextInputFields.length; i++) {
            allTextInputFields[i].setFilters(new InputFilter[]{
                    ValidatorHelper.getEmojiFilter()
            });
        }

        invitationKey = SharedPreferenceHelper.getInvitationKey(getContext());
        progressDialog = new ProgressDialog(getContext(), R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Loading...");
        return view;
    }

    public void onSignUpButtonClick(View view) {
        closeKeyboard();
        if (validateInputFields()) {
            progressDialog.show();
            userRegistrationDTO userToRegistor = new userRegistrationDTO();
            userToRegistor.setToken(invitationKey);
            userToRegistor.setUser(getUserObjectToRegister());

            LiveData<userRegistrationDTO> data = registrationRepository
                    .Registration(userToRegistor);

            data.observe(
                    this,
                    userRegistrationDTO -> {
                        if (userRegistrationDTO != null) {
                            progressDialog.hide();
                            SharedPreferenceHelper.saveRegisteredUser(getContext(), email.getText().toString(), mobileNumber.getText().toString());
                            Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.fragment_container).navigate(R.id.action_registrationFormFragment_to_registrationSuccessfulFragment);
                        } else {
                            progressDialog.hide();

                            // Temprary for demo purpose.. Api is not working here
                            SharedPreferenceHelper.saveRegisteredUser(getContext(), email.getText().toString(), mobileNumber.getText().toString());
                            Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.fragment_container).navigate(R.id.action_registrationFormFragment_to_registrationSuccessfulFragment);
                            Toast.makeText(getContext(), "Registration Failure", Toast.LENGTH_LONG).show();
                        }
                    });

        } else {
            scrollView.fullScroll(ScrollView.FOCUS_UP);
        }
    }

    public Boolean validateInputFields() {
        if (ValidatorHelper.isFieldEmpty(first_name, first_name_layout, getContext())
                && ValidatorHelper.isFieldEmpty(surname, surname_layout, getContext())
                && ValidatorHelper.validateEmail(email, email_layout, getContext())
                && ValidatorHelper.validateMobileNumber(mobileNumber, mobileNumber_layout, getContext())
                && ValidatorHelper.validateUserName(user_name, user_name_layout, getContext())
                && ValidatorHelper.validatePassword(password, password_layout, getContext())
                && ValidatorHelper.isFieldEmpty(confirm_password, confirm_password_layout, getContext())
                && ValidatorHelper.validatePassAndRepeatPassword(password, confirm_password, confirm_password_layout, getContext())) {
            return true;
        }
        return false;
    }

    public User getUserObjectToRegister() {
        User user = new User();
        user.setActive(true);
        user.setUsername(first_name.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        user.setPhoneNumber(mobileNumber.getText().toString());
        user.setFirstname(first_name.getText().toString());
        user.setSurname(surname.getText().toString());
        return user;
    }

    public void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
