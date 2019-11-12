package de.heuremo.commons.common.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import de.heuremo.commons.common.constant.SharedPreferencesConstant;

public
class SharedPreferenceHelper {


    public static Boolean saveCustomerNumberAndKey(Context context, String customerNumber, String invitationKey) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferencesConstant.KEY_CUSTOMER_NUMBER, customerNumber);
        editor.putString(SharedPreferencesConstant.KEY_INVITATION_KEY, invitationKey);
        return editor.commit();
    }

    public static Boolean saveRegisteredUser(Context context, String email, String mobileNumber){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferencesConstant.KEY_USER_EMAIL, email);
        editor.putString(SharedPreferencesConstant.KEY_USER_MOBILE_NUMBER, mobileNumber);
        editor.putBoolean(SharedPreferencesConstant.KEY_IS_USER_REGISTER, true);
        return editor.commit();
    }

    public static String getInvitationKey(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(SharedPreferencesConstant.KEY_INVITATION_KEY, "");
    }

    public static Boolean saveLoggedInUserInfo(Context context, String user_name, String password, String token){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferencesConstant.KEY_USERNAME, user_name);
        editor.putString(SharedPreferencesConstant.KEY_USER_PASSWORD, password);
        editor.putString(SharedPreferencesConstant.KEY_JWT, token);
        editor.putBoolean(SharedPreferencesConstant.KEY_IS_USER_LOGIN, true);
        return editor.commit();
    }

    public static boolean getUserRegisteredKey(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(SharedPreferencesConstant.KEY_IS_USER_REGISTER, false);
    }

    public static boolean getUserLoggedInKey(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(SharedPreferencesConstant.KEY_IS_USER_LOGIN, false);
    }

    public static String getUserEmail(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(SharedPreferencesConstant.KEY_USER_EMAIL, "");
    }

    public static String getUserMobileNumber(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(SharedPreferencesConstant.KEY_USER_MOBILE_NUMBER, "");
    }

    public static String getCustomerNumber(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(SharedPreferencesConstant.KEY_CUSTOMER_NUMBER, "");
    }

    public static String getJWTAuth(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(SharedPreferencesConstant.KEY_JWT, "");
    }

    public static Boolean  removeSharedPrefsOnLogOut(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SharedPreferencesConstant.KEY_USERNAME);
        editor.remove(SharedPreferencesConstant.KEY_JWT);
        editor.remove(SharedPreferencesConstant.KEY_IS_USER_LOGIN);
        editor.remove(SharedPreferencesConstant.KEY_USER_PASSWORD);
        return editor.commit();
    }

}
