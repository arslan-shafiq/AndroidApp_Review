package de.heuremo.steelmeasureapp.components.login.Registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.Objects;

import de.heuremo.R;

public class RegistrationSuccessfulFragment extends Fragment {
    Button continue_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration_successful, container, false);
        continue_button = (Button) view.findViewById(R.id.continue_button);


        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.fragment_container).navigate(R.id.action_registrationSuccessfulFragment_to_loginFragment);
            }
        });

        return view;
    }
}
