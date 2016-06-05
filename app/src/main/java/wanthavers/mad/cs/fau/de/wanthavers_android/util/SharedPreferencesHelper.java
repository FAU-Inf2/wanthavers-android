package wanthavers.mad.cs.fau.de.wanthavers_android.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Set;

public class SharedPreferencesHelper {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private final String sharedPreferencesName;

    private static HashMap<String, SharedPreferencesHelper> INSTANCES = new HashMap<>();

    public static SharedPreferencesHelper getInstance(String sharedPreferencesName, Context context) {
        SharedPreferencesHelper ret = INSTANCES.get(sharedPreferencesName);

        if(ret == null) {
            ret = new SharedPreferencesHelper(sharedPreferencesName, context);
            INSTANCES.put(sharedPreferencesName, ret);
        }

        return ret;
    }

    //Shared Preferences Names
    public static final String NAME_SETTINGS = "settings";
    public static final String NAME_USER = "user";
    public static final String NAME_DB_TIMESTAMPS = "dbtimestamps";

    //Key Strings
    public static final String KEY_API_URL = "api_url";
    public static final String KEY_USERID = "userid";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER = "user";
    public static final String KEY_DESIRE = "desire";
    public static final String KEY_HAVER = "haver";
    public static final String KEY_MEDIA = "media";
    public static final String KEY_RATING = "rating";

    private SharedPreferencesHelper(String sharedPreferencesName, Context context) {
        if(context == null) {
            throw new IllegalArgumentException("Context must not be null!");
        }
        if(sharedPreferencesName.isEmpty() || sharedPreferencesName == null) {
            throw new IllegalArgumentException("SharedPreferencesName must not be empty/null!");
        }

        this.sharedPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
        this.sharedPreferencesName = sharedPreferencesName;
    }

    public String getSharedPreferencesName() {
        return this.sharedPreferencesName;
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

    public void saveLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    public long loadLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }
}
