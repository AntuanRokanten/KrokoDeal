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
import com.implemica.krokodeal.ui.fragments.SetWordFragment;
import com.implemica.krokodeal.util.TimerData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ant
 */
public class PlayActivity extends AppCompatActivity {

   private TextView currentPlayer;

   private List<Player> players;

   private TimerData timerData;

   private Fragment playShowFragment = new PlayShowFragment();

   private Fragment setWordFragment = new SetWordFragment();

   private int hostIndex;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.play_activity);

      initViews();

      Intent intent = getIntent();
      players = intent.getParcelableArrayListExtra("players");
      hostIndex = intent.getIntExtra("host_index", -1);
      timerData = intent.getParcelableExtra("timer");

      currentPlayer.setText(players.get(hostIndex).getName());

      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.replace(R.id.play_fragment_container, playShowFragment);
      transaction.replace(R.id.word_container, setWordFragment);
      transaction.commit();
   }

   private void initViews() {
      currentPlayer = (TextView) findViewById(R.id.player_name);
   }

   public TimerData getTimerData() {
      return timerData;
   }

   public List<Player> getPlayers() {
      return new ArrayList<>(players); // returning copy of the list since in some methods host player will be removed (that shouldn't performed on the main list)
   }

   public Player getHost() {
      return new ArrayList<>(players).get(hostIndex);
   }

   public int getHostIndex() {
      return hostIndex;
   }

   public void changeHost(Player newHost){
      String newHostName = newHost.getName();
      currentPlayer.setText(newHostName);

      players.get(hostIndex).setIsHost(false);

      hostIndex = findPlayerIndexByName(newHost.getName());
      players.get(hostIndex).setIsHost(true);
   }

   private int findPlayerIndexByName(String name) {
      for (int i = 0; i < players.size(); i++) {
         if (players.get(i).getName().equals(name)) {
            return i;
         }
      }
      return -1;
   }
}
