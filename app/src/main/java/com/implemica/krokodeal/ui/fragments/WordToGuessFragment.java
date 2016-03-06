package com.implemica.krokodeal.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.implemica.krokodeal.R;

/**
 * @author ant
 */
public class WordToGuessFragment extends Fragment {

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.word_to_guess_container, container, false);
      TextView wordView = (TextView) view.findViewById(R.id.word_to_guess);
      String wordToGuess = getArguments().getString("word_to_guess");

      wordView.setText(wordToGuess);
      return view;
   }
}
