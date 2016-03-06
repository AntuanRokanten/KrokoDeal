package com.implemica.krokodeal.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;

import com.implemica.krokodeal.R;

/**
 * @author ant
 */
public class PlayShowFragment extends Fragment {

   private Button start;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      final View view = inflater.inflate(R.layout.play_show_fragment, container, false);
      start = (Button) view.findViewById(R.id.start_showing_button);

      start.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            String wordToGuess = ((SetWordFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.word_container)).getWordToGuess();
            if(wordToGuess.isEmpty()) {
               // todo show snack
               return;
            }

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.play_fragment_container, new PlayFragment());

            WordToGuessFragment guessFragment = new WordToGuessFragment();

            Bundle bundle = new Bundle();

            bundle.putString("word_to_guess", wordToGuess); // todo string to constants
            guessFragment.setArguments(bundle);

            transaction.replace(R.id.word_container, guessFragment);
            transaction.commit();
         }
      });

      return view;
   }

}
