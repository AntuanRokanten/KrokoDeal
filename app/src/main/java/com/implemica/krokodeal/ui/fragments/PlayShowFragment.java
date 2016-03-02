package com.implemica.krokodeal.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.implemica.krokodeal.R;

/**
 * @author ant
 */
public class PlayShowFragment extends Fragment {

   private Button start;
   private Fragment playFragment = new PlayFragment();

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      View view = inflater.inflate(R.layout.play_show_fragment, container, false);
      start = (Button) view.findViewById(R.id.start_showing_button);

      start.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.play_fragment_container, playFragment);
            transaction.commit();
         }
      });

      return view;
   }

}
