package com.foodonet.foodonet.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.telephony.PhoneNumberUtils;
import android.widget.Toast;

import com.foodonet.foodonet.R;
import com.foodonet.foodonet.commonMethods.CommonConstants;
import com.foodonet.foodonet.commonMethods.CommonMethods;
import com.foodonet.foodonet.serverMethods.ServerMethods;

public class PrefsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private OnSignOutClickListener listener;
    private ListPreference listNotificationRadius;
    private String keyListNotificationRadius, keyUserName, keyUserPhone;
    private Preference preferenceUserPhone, preferenceUserName;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_screen);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        listener = (OnSignOutClickListener) getActivity();

        keyListNotificationRadius = getString(R.string.key_prefs_list_notification_radius);
        keyUserName = getString(R.string.key_prefs_user_name);
        keyUserPhone = getString(R.string.key_prefs_user_phone);
        String keyGetNotifications = getString(R.string.key_prefs_get_notifications);

        SwitchPreference getNotifications = (SwitchPreference) findPreference(keyGetNotifications);
        getNotifications.setChecked(sharedPreferences.getBoolean(keyGetNotifications,true));

        listNotificationRadius = (ListPreference) findPreference(keyListNotificationRadius);
        String[] notificationRadiusListKMValues = getResources().getStringArray(R.array.prefs_notification_radius_values_km);
        String currentValueNotificationRadiusListKM = sharedPreferences.getString(keyListNotificationRadius,
                notificationRadiusListKMValues[CommonConstants.DEFAULT_NOTIFICATION_RADIUS_ITEM]);
        listNotificationRadius.setSummary(getEntryStringFromList(
                R.array.prefs_notification_radius_entries_km,
                R.array.prefs_notification_radius_values_km,
                currentValueNotificationRadiusListKM));
        listNotificationRadius.setOnPreferenceChangeListener(this);

        preferenceUserName = findPreference(keyUserName);
        preferenceUserName.setSummary(sharedPreferences.getString(keyUserName,""));
        preferenceUserName.setOnPreferenceChangeListener(this);

        preferenceUserPhone = findPreference(keyUserPhone);
        preferenceUserPhone.setSummary(sharedPreferences.getString(keyUserPhone,""));
        preferenceUserPhone.setOnPreferenceChangeListener(this);

        findPreference(getString(R.string.key_prefs_sign_out)).setOnPreferenceClickListener(this);

    }

    private String getEntryStringFromList(int entriesListRes, int valuesListRes, String value){
        String[] entriesList = getResources().getStringArray(entriesListRes);
        String[] valuesList = getResources().getStringArray(valuesListRes);

        for (int i = 0; i < valuesList.length; i++) {
            if(valuesList[i].equals(value)){
                return entriesList[i];
            }
        }
        return "";
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String preferenceKey = preference.getKey();
        String userName = sharedPreferences.getString(keyUserName,"");
        String userPhone = sharedPreferences.getString(keyUserPhone,"");
        if (preferenceKey.equals(keyListNotificationRadius)) {
            listNotificationRadius.setSummary(getEntryStringFromList(
                    R.array.prefs_notification_radius_entries_km,
                    R.array.prefs_notification_radius_values_km,
                    newValue.toString()));
        } else if(preferenceKey.equals(keyUserName)){
            userName = (newValue.toString()).replace("\n","");
            int userNameLengthLimit = getResources().getInteger(R.integer.user_name_length_limit);
            if(userName.length()> userNameLengthLimit){
                userName = userName.substring(0,userNameLengthLimit);
                Toast.makeText(getActivity(), R.string.user_name_too_long, Toast.LENGTH_SHORT).show();
            }
            preferenceUserName.setSummary(userName);
            updateUser(userName,userPhone);
            return false;
        } else if(preferenceKey.equals(keyUserPhone)){
            userPhone = newValue.toString();
            if(PhoneNumberUtils.isGlobalPhoneNumber(userPhone)){
                userPhone = CommonMethods.getDigitsFromPhone(userPhone);
                preferenceUserPhone.setSummary(userPhone);
                updateUser(userName,userPhone);
                return false;
            } else{
                Toast.makeText(getActivity(), R.string.invalid_phone_number, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void updateUser(String userName, String userPhone){
        ServerMethods.updateUser(getActivity(), userPhone, userName);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        listener.onSignOutClick();
        return true;
    }

    public interface OnSignOutClickListener{
        void onSignOutClick();
    }
}
