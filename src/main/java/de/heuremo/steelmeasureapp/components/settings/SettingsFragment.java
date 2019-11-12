package de.heuremo.steelmeasureapp.components.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.heuremo.R;
import de.heuremo.commons.common.constant.SharedPreferencesConstant;
import de.heuremo.commons.common.helpers.SharedPreferenceHelper;
import de.heuremo.steelmeasureapp.components.LoginActivity;

public class SettingsFragment extends Fragment {

    // ********* declarations *********
    private MaterialButton change_password_button, logout_button, save_button;
    private TextInputLayout email_layout, mobile_number_layout;
    private TextInputEditText email_text_input, mobile_number_text_input;
    private SwitchMaterial email_notification_switch, sms_notification_switch;
    private ImageView data_protection, conditions;
    private String user_email = "";
    private String user_mobile_number = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        getActivity().setTitle("Settings");

        // *********  initialization of views  *********
        email_layout = (TextInputLayout) view.findViewById(R.id.E_mail_layout);
        mobile_number_layout = (TextInputLayout) view.findViewById(R.id.Mobile_Number_layout);
        email_text_input = (TextInputEditText) view.findViewById(R.id.E_mail);
        mobile_number_text_input = (TextInputEditText) view.findViewById(R.id.Mobile_Number);
        change_password_button = (MaterialButton) view.findViewById(R.id.change_password_button);
        email_notification_switch = (SwitchMaterial) view.findViewById(R.id.email_notification_toggle_button);
        sms_notification_switch = (SwitchMaterial) view.findViewById(R.id.sms_notification_toggle_button);
        logout_button = (MaterialButton) view.findViewById(R.id.logout_button);
        conditions = (ImageView) view.findViewById(R.id.conditions);
        data_protection = (ImageView) view.findViewById(R.id.data_protection);

        conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTermsAndConditionsLink();
            }
        });

        data_protection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTermsAndConditionsLink();
            }
        });


        change_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.fragment_container).navigate(R.id.action_settingsFragment_dest_to_changePasswordFragment);
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogOutClicked();
            }
        });

        user_email = SharedPreferenceHelper.getUserEmail(getContext());
        user_mobile_number = SharedPreferenceHelper.getUserMobileNumber(getContext());
        email_text_input.setText(user_email);
        mobile_number_text_input.setText(user_mobile_number);

        return view;
    }

    public void onLogOutClicked() {
        SharedPreferenceHelper.removeSharedPrefsOnLogOut(getContext());
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void openTermsAndConditionsLink() {
        Uri termsAndConditionURL = Uri.parse("https://www.heuremo.de");
        Intent intent = new Intent(Intent.ACTION_VIEW, termsAndConditionURL);
        startActivity(intent);
    }

}
