package com.implemica.krokodeal.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.implemica.krokodeal.R;
import com.implemica.krokodeal.database.DBHelper;
import com.implemica.krokodeal.ui.listeners.HideKeyboardListener;

/**
 * @author ant
 */
public class SetWordFragment extends Fragment {

   private EditText wordToGuess;

   private ImageButton refreshWord;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.set_word_fragment, container, false);

      refreshWord = (ImageButton) view.findViewById(R.id.refresh_word);
      wordToGuess = (EditText) view.findViewById(R.id.enter_word);

      final DBHelper dbHelper = new DBHelper(getActivity());
      refreshWord.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            String randomWord = dbHelper.retrieveRandomWord();
            wordToGuess.setText(randomWord);
         }
      });

      wordToGuess.setOnFocusChangeListener(HideKeyboardListener.getInstance());

      return view;
   }

   public String getWordToGuess() {
      return wordToGuess.getText().toString();
   }
}
