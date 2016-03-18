package com.implemica.krokodeal.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.implemica.krokodeal.R;
import com.implemica.krokodeal.util.UiUtils;

/**
 * @author ant
 */
public class PlayShowFragment extends Fragment {

   private Button start;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      final View rootView = inflater.inflate(R.layout.play_show_fragment, container, false);
      start = (Button) rootView.findViewById(R.id.start_showing_button);

      start.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            FragmentActivity activity = getActivity();
            FragmentManager supportFragmentManager = activity.getSupportFragmentManager();

            String wordToGuess = ((SetWordFragment) supportFragmentManager.findFragmentById(R.id.word_container)).getWordToGuess();
            if (wordToGuess.isEmpty()) {
               UiUtils.showPinkSnackbar(rootView, activity, R.string.snackbar_empty_word);
               return;
            }

            FragmentTransaction transaction = supportFragmentManager.beginTransaction();
            transaction.replace(R.id.play_fragment_container, new PlayFragment());

            WordToGuessFragment guessFragment = new WordToGuessFragment();

            Bundle bundle = new Bundle();

            bundle.putString("word_to_guess", wordToGuess); // todo string to constants
            guessFragment.setArguments(bundle);

            transaction.replace(R.id.word_container, guessFragment);
            transaction.commit();
         }
      });

      return rootView;
   }

}
