package com.implemica.krokodeal.ui.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.implemica.krokodeal.R;
import com.implemica.krokodeal.ui.activities.PlayActivity;
import com.implemica.krokodeal.utils.TimerData;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author ant
 */
public class PlayFragment extends Fragment {

   private static final String LOG_TAG = PlayFragment.class.getCanonicalName();

   private TextView minutes;

   private TextView seconds;

   private LinearLayoutCompat linearLayout;

   private Button succesButton;

   private Button failureButton;

   private Fragment playShowFragment = new PlayShowFragment();

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      PlayActivity activity = (PlayActivity) getActivity();
      TimerData timerData = activity.getTimerData();

      View view = inflater.inflate(R.layout.play_process_fragment, container, false);

      linearLayout = (LinearLayoutCompat) view.findViewById(R.id.count_down);
      minutes = (TextView) view.findViewById(R.id.count_down_mins);
      seconds = (TextView) view.findViewById(R.id.count_down_secs);
      succesButton = (Button) view.findViewById(R.id.success_button);
      failureButton = (Button) view.findViewById(R.id.failure_button);

      if (timerData == null) {
         linearLayout.setVisibility(View.GONE);
      } else {
         long millis = MINUTES.toMillis(timerData.getMinutes()) + SECONDS.toMillis(timerData.getSeconds());

         KrokoCountDownTimer krokoCountDownTimer = new KrokoCountDownTimer(millis);
         krokoCountDownTimer.start();
      }

      succesButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            // todo show players list
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.play_fragment_container, playShowFragment);
            transaction.commit();
         }
      });

      return view;
   }

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
            secondsText = "0" + secondsText;
         }

         minutes.setText(String.valueOf(minutesLeft));
         seconds.setText(secondsText);
      }

      @Override
      public void onFinish() {
         if (getView() != null) {
            Snackbar.make(getView(), R.string.snackbar_fail, Snackbar.LENGTH_LONG).show();
         } else {
            Log.e(LOG_TAG, "Unable to show snackbar with message: " + getString(R.string.snackbar_fail));
         }
      }
   }
}
