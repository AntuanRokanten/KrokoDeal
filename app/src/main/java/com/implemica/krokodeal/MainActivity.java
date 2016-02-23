package com.implemica.krokodeal;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @author ant
 */
public class MainActivity extends AppCompatActivity {

   Fragment fragment;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main_activity);

      fragment = new LaunchFragment();

      FragmentTransaction transaction = getFragmentManager().beginTransaction();
      transaction.add(R.id.fragment_container, fragment);
      transaction.commit();
   }

}
