package de.heuremo.steelmeasureapp.components.login.Registration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import de.heuremo.R;
import de.heuremo.commons.common.constant.SharedPreferencesConstant;
import de.heuremo.commons.common.helpers.SharedPreferenceHelper;

public class RegistrationFragment extends Fragment {

    View view;
    // ********* declarations *********
    private MaterialButton continue_button;
    private TextInputEditText invitation_Key_TextView, customer_Number_TextView;
    private TextView data_protection_text, conditions_text;
    private MaterialCheckBox data_protection_checkbox, conditions_checkbox;
    private String invitationKey = "";
    private String customerNumber = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registration, container, false);

        // *********  initialization of views  *********
        invitation_Key_TextView = (TextInputEditText) view.findViewById(R.id.invitation_key);
        customer_Number_TextView = (TextInputEditText) view.findViewById(R.id.customer_number);
        conditions_checkbox = (MaterialCheckBox) view.findViewById(R.id.conditions_checkbox);
        data_protection_checkbox = (MaterialCheckBox) view.findViewById(R.id.data_protection_checkbox);
        conditions_text = (TextView) view.findViewById(R.id.conditions_checkbox_text);
        data_protection_text = (TextView) view.findViewById(R.id.data_protection_text);
        continue_button = (MaterialButton) view.findViewById(R.id.continue_button);


        data_protection_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTermsAndConditionsLink();
            }
        });

        conditions_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTermsAndConditionsLink();
            }
        });

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onContinue();
            }
        });

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getActivity().getIntent();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkData != null) {
            invitationKey = appLinkData.getQueryParameter("token");
            customerNumber = appLinkData.getQueryParameter("customerID");
            invitation_Key_TextView.setText(invitationKey);
            customer_Number_TextView.setText(customerNumber);
        }
        return view;
    }

    public void onContinue() {
        Boolean conditions, dataProtection;
        conditions = conditions_checkbox.isChecked();
        dataProtection = data_protection_checkbox.isChecked();

        if (conditions && dataProtection) {
            SharedPreferenceHelper.saveCustomerNumberAndKey(getContext(), customerNumber, invitationKey);
            NavHostFragment.findNavController(RegistrationFragment.this).navigate(R.id.action_registrationFragment_to_registrationFormFragment);
        } else if (!conditions && !dataProtection) {
            Toast.makeText(getContext(), getString(R.string.st_legal_conditions_error), Toast.LENGTH_SHORT).show();
        } else if (!conditions) {
            Toast.makeText(getContext(), getString(R.string.st_error_conditions_must_accept), Toast.LENGTH_SHORT).show();
        } else if (!dataProtection) {
            Toast.makeText(getContext(), getString(R.string.st_error_data_protection_must_accept), Toast.LENGTH_SHORT).show();
        }
    }

    public void openTermsAndConditionsLink() {
        Uri termsAndConditionURL = Uri.parse("https://www.heuremo.de");
        Intent intent = new Intent(Intent.ACTION_VIEW, termsAndConditionURL);
        startActivity(intent);
    }
}
