package com.implemica.krokodeal.util;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.implemica.krokodeal.R;

/**
 * @author ant
 */
public class UiUtils {

   public static void hideSoftKeyboard(View view) {
      InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
   }

   public static void showPinkSnackbar(View rootView, Context context, int stringRes) {
      Snackbar snackbar = Snackbar.make(rootView, stringRes, Snackbar.LENGTH_LONG);
      snackbar.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.pink));
      snackbar.show();
   }

}
