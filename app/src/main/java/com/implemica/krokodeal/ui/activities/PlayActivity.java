package com.implemica.krokodeal.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.implemica.krokodeal.Player;
import com.implemica.krokodeal.R;
import com.implemica.krokodeal.ui.fragments.PlayShowFragment;
import com.implemica.krokodeal.utils.TimerData;

import java.util.List;

/**
 * @author ant
 */
public class PlayActivity extends AppCompatActivity {

   private TextView currentPlayer;

   private List<Player> players;

   private TimerData timerData;

   private Fragment playShowFragment = new PlayShowFragment();

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.play_activity);

      initViews();

      Intent intent = getIntent();
      players = intent.getParcelableArrayListExtra("players");
      int hostIndex = intent.getIntExtra("host_index", -1);
      timerData = intent.getParcelableExtra("timer");

      currentPlayer.setText(players.get(hostIndex).getName());

      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.replace(R.id.play_fragment_container, playShowFragment);
      transaction.commit();

   }

   private void initViews() {
      currentPlayer = (TextView) findViewById(R.id.player_name);
   }

   public TimerData getTimerData() {
      return timerData;
   }
}
