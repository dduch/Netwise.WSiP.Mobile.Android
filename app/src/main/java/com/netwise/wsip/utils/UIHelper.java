package com.netwise.wsip.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by dawido on 05.04.2018.
 */

public class UIHelper {
    public static void hideSoftKeyboard(Activity activity, View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        catch(NullPointerException ex)
        {
            ex.printStackTrace();
        }
    }
}
