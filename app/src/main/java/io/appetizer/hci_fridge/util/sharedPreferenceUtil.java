package io.appetizer.hci_fridge.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by user on 2018/6/15.
 */

public class sharedPreferenceUtil {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static String get(Context context, String spname, String fieldname){
        sharedPreferences = context.getSharedPreferences(spname,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String result = sharedPreferences.getString(fieldname, null);
        editor.commit();
        return result;
    }
}
