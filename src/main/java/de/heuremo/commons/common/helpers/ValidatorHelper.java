package de.heuremo.commons.common.helpers;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.heuremo.R;

public final class ValidatorHelper {

    private static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static String mobilePattern = "^[+]?[0-9]{6,20}$";
    private static String usernamePattern = "^[a-zA-Z0-9._-]{3,}$";

    public static InputFilter getEmojiFilter() {
        InputFilter emojiFilter = new InputFilter() {
            public CharSequence filter(CharSequence src, int start,
                                       int end, Spanned dst, int dstart, int dend) {
                if (src.equals("")) { // for backspace
                    return src;
                }
                if (src.toString().matches("[\\x00-\\x7F]+")) {
                    return src;
                }
                return "";
            }
        };
        return emojiFilter;
    }

    public static boolean isFieldEmpty (TextInputEditText text_field , TextInputLayout text_field_layout , Context context) {
        String nameInput = text_field.getText().toString().trim();

        if (nameInput.isEmpty()) {
            text_field_layout.setError(context.getString(R.string.st_required_Field_error));
            return false;
        } else {
            text_field_layout.setError(null);
            return true;
        }
    }

    public static boolean validateEmail (TextInputEditText text_field , TextInputLayout text_field_layout , Context context){
        String emailInput = text_field.getText().toString().trim();

        if (emailInput.isEmpty()) {
            text_field_layout.setError(context.getString(R.string.st_required_Field_error));
            return false;
        } else if (emailInput.matches(emailPattern)) {
            text_field_layout.setError(null);
            return true;
        } else {
            text_field_layout.setError(context.getString(R.string.st_error_not_valid_email));
            return false;
        }
    }

    public static boolean validateMobileNumber (TextInputEditText text_field , TextInputLayout text_field_layout , Context context){
        String numberInput = text_field.getText().toString().trim();

        if (numberInput.isEmpty()) {
            text_field_layout.setError(context.getString(R.string.st_required_Field_error));
            return false;
        } else if (numberInput.matches(mobilePattern) && numberInput.length() > 5 && numberInput.length() <= 16) {
            text_field_layout.setError(null);
            return true;
        } else {
            text_field_layout.setError(context.getString(R.string.st_error_mobile_number));
            return false;
        }
    }

    public static boolean validateUserName (TextInputEditText text_field , TextInputLayout text_field_layout , Context context){
        String usernameInput = text_field.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            text_field_layout.setError(context.getString(R.string.st_required_Field_error));
            return false;
        } else if (usernameInput.matches(usernamePattern)) {
            text_field_layout.setError(null);
            return true;
        } else {
            text_field_layout.setError(context.getString(R.string.st_error_enter_valid_user_name));
            return false;
        }
    }

    public static boolean validatePassword (TextInputEditText text_field , TextInputLayout text_field_layout , Context context){
        String passwordInput = text_field.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            text_field_layout.setError(context.getString(R.string.st_required_Field_error));
            return false;
        } else if (passwordInput.length() < 6) {
            text_field_layout.setError(context.getString(R.string.st_error_password_too_short));
            return false;
        } else {
            text_field_layout.setError(null);
            return true;
        }
    }

    public static boolean validatePassAndRepeatPassword (TextInputEditText password,TextInputEditText confirm_password , TextInputLayout text_field_layout , Context context){
        String pass = password.getText().toString();
        String confirmPass = confirm_password.getText().toString();

        if (pass.equals(confirmPass)) {
            text_field_layout.setError(null);
            return true;
        } else {
            text_field_layout.setError(context.getString(R.string.st_pass_confirm_pass_error));
            return false;
        }
    }

}
