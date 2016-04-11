package com.implemica.krokodeal.ui.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.implemica.krokodeal.ChooseWinnerListener;
import com.implemica.krokodeal.Player;
import com.implemica.krokodeal.R;
import com.implemica.krokodeal.database.DBHelper;
import com.implemica.krokodeal.ui.activities.PlayActivity;
import com.implemica.krokodeal.ui.dialogs.ChoosePlayerDialog;
import com.implemica.krokodeal.util.TimerData;
import com.implemica.krokodeal.util.UiUtils;
import com.mikepenz.materialize.util.UIUtils;

import java.util.ArrayList;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author ant
 */
public class PlayFragment extends Fragment implements ChooseWinnerListener {

   private static final String LOG_TAG = PlayFragment.class.getCanonicalName();

   private TextView minutes;

   private TextView seconds;

   private LinearLayoutCompat linearLayout;

   private Button successButton;

   private Button failureButton;

   private DBHelper dbHelper;

   /**
    * Host activity of this fragment
    */
   private PlayActivity playActivity;

   /**
    * Custom count down timer
    */
   private KrokoCountDownTimer krokoCountDownTimer;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      playActivity = (PlayActivity) getActivity();
      dbHelper = new DBHelper(playActivity);

      View view = inflater.inflate(R.layout.play_process_fragment, container, false);
      initViews(view);

      TimerData timerData = playActivity.getTimerData();
      initTimer(timerData);

      successButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // updating database
            Player host = playActivity.getHost();
            dbHelper.incrementShowSuccess(host);

            // prepare show winner dialog
            ChoosePlayerDialog playerDialog = new ChoosePlayerDialog();

            Bundle bundle = new Bundle();
            ArrayList<Player> players = (ArrayList<Player>) playActivity.getPlayers();
            players.remove(playActivity.getHostIndex()); // removing host from the list since host cannot guess word

            bundle.putParcelableArrayList("players", players);

            playerDialog.setCancelable(false);
            playerDialog.setArguments(bundle);
            playerDialog.setChooseWinnerListener(PlayFragment.this);
            playerDialog.show(playActivity.getSupportFragmentManager(), "TAG"); // todo tag?

            if(krokoCountDownTimer != null) {
               krokoCountDownTimer.cancel();
            }
         }
      });

      failureButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

            if(krokoCountDownTimer != null) {
               krokoCountDownTimer.cancel();
            }

            dbHelper.incrementShowFail(playActivity.getHost());
            resetUI();
         }
      });

      return view;
   }

   /**
    * Shows timer data and starts it if needed
    *
    * @param timerData timer data retrieved from the play activity
    */
   private void initTimer(TimerData timerData) {
      if (timerData == null) {
         linearLayout.setVisibility(View.GONE);
      } else {
         long millis = MINUTES.toMillis(timerData.getMinutes()) + SECONDS.toMillis(timerData.getSeconds());

         krokoCountDownTimer = new KrokoCountDownTimer(millis);
         krokoCountDownTimer.start();
      }
   }

   /**
    * Initializes views of this fragment
    *
    * @param view root view
    */
   private void initViews(View view) {
      linearLayout = (LinearLayoutCompat) view.findViewById(R.id.count_down);
      minutes = (TextView) view.findViewById(R.id.count_down_mins);
      seconds = (TextView) view.findViewById(R.id.count_down_secs);
      successButton = (Button) view.findViewById(R.id.success_button);
      failureButton = (Button) view.findViewById(R.id.failure_button);
   }

   @Override
   public void onWinnerChosen(Player winner) {
      // setting fragment with show button and fragment with enter new word
      playActivity.changeHost(winner);
      dbHelper.incrementGuessesValue(winner);
      resetUI();
   }

   /**
    * Sets fragments for choosing word and starting showing again
    */
   private void resetUI() {
      FragmentTransaction transaction = playActivity.getSupportFragmentManager().beginTransaction();
      transaction.replace(R.id.play_fragment_container, new PlayShowFragment());
      transaction.replace(R.id.word_container, new SetWordFragment());
      transaction.commit();
   }

   /**
    * CountDownTimer which tick every second and updates UI. When it's finished, round of the game is considered failed
    */
   private class KrokoCountDownTimer extends CountDownTimer {

      public KrokoCountDownTimer(long millisInFuture) {
         super(millisInFuture, 1000);
      }

      @Override
      public void onTick(long millisUntilFinished) {
         long minutesLeft = MILLISECONDS.toMinutes(millisUntilFinished);
         long secondsLeft = MILLISECONDS.toSeconds(millisUntilFinished) - MINUTES.toSeconds(minutesLeft);

         String secondsText = String.valueOf(secondsLeft);
         if (secondsText.length() == 1) {
            secondsText = "0" + secondsText;// todo via formatter
         }

         minutes.setText(String.valueOf(minutesLeft));
         seconds.setText(secondsText);
      }

      @Override
      public void onFinish() {
         if (getView() != null) {
            UiUtils.showPinkSnackbar(getView(), PlayFragment.this.getActivity(), R.string.snackbar_fail);

            resetUI(); // todo remove duplicate
            dbHelper.incrementShowFail(playActivity.getHost());
         } else {
            Log.e(LOG_TAG, "Unable to show snackbar with message: " + getString(R.string.snackbar_fail));
         }
      }
   }
}
