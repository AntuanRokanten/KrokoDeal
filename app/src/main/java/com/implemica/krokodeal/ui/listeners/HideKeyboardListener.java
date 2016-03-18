package com.implemica.krokodeal.ui.listeners;

import android.view.View;

import com.implemica.krokodeal.util.UiUtils;

/**
 * @author ant
 */
public class HideKeyboardListener implements View.OnFocusChangeListener {

   private static final HideKeyboardListener instance = new HideKeyboardListener();

   private HideKeyboardListener() {
      // singleton
   }

   public static HideKeyboardListener getInstance() {
      return instance;
   }

   @Override
   public void onFocusChange(View v, boolean hasFocus) {
      if(!hasFocus) {
         UiUtils.hideSoftKeyboard(v);
      }
   }
}
