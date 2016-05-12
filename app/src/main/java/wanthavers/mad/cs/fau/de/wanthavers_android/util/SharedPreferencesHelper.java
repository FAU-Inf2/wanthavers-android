package wanthavers.mad.cs.fau.de.wanthavers_android.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import javax.inject.Inject;

public class SharedPreferencesHelper {
    protected final String sharedPreferencesName;
    protected final SharedPreferences sharedPreferences;
    protected final Context context;
    protected final SharedPreferences.Editor editor;

    //Shared Preferences Names
    public static final String NAME_SETTINGS = "settings";

    //Key Strings
    public static final String KEY_API_URL = "api_url";

    @Inject
    public SharedPreferencesHelper(String sharedPreferencesName, Context context) {
        if(context == null) {
            throw new IllegalArgumentException("Context must not be null!");
        }
        if(sharedPreferencesName.isEmpty() || sharedPreferencesName == null) {
            throw new IllegalArgumentException("SharedPreferencesName must not be empty/null!");
        }

        this.sharedPreferencesName = sharedPreferencesName;
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String loadString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public int loadInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void saveBoolean(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean loadBoolean(String key, Boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void saveStringSet(String key, Set<String> values) {
        editor.putStringSet(key, values);
        editor.apply();
    }

    public Set<String> loadStringSet(String key, Set<String> defaultValues) {
        return sharedPreferences.getStringSet(key, defaultValues);
    }
}
