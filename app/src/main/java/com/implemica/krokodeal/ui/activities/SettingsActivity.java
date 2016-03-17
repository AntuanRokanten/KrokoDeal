package com.implemica.krokodeal.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.implemica.krokodeal.AddPlayerListener;
import com.implemica.krokodeal.Player;
import com.implemica.krokodeal.R;
import com.implemica.krokodeal.database.DBHelper;
import com.implemica.krokodeal.ui.MyAdapter;
import com.implemica.krokodeal.ui.dialogs.AddPlayerDialog;
import com.implemica.krokodeal.ui.listeners.HideKeyboardListener;
import com.implemica.krokodeal.util.TimerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ant
 */
public class SettingsActivity extends AppCompatActivity implements AddPlayerListener {

   private CheckBox countDownCheck;

   private EditText countDownMin;

   private EditText countDownSec;

   private RecyclerView playerList;

   private Button playBtn;

   private Button addPlayer;

   private MyAdapter adapter;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.settings_activity);

      initViews();

      playBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int hostIndex = adapter.getHostIndex();

            if (hostIndex < 0) {
               // todo shot snackbar
            } else {
               Intent intent = new Intent(SettingsActivity.this, PlayActivity.class);

               if(countDownCheck.isChecked()) {
                  String secondsText = countDownSec.getText().toString();
                  String minutesText = countDownMin.getText().toString();

                  if (secondsText.isEmpty() && minutesText.isEmpty()) {
                     return; // todo show snackbar
                  }

                  String secs = secondsText.isEmpty() ? "0" : secondsText;
                  String mins = minutesText.isEmpty() ? "0" : minutesText;
                  
                  if(Integer.valueOf(secs) >= 60) {
                     // todo shot snackbar
                     return;
                  }
                  intent.putExtra("timer", new TimerData(Integer.valueOf(mins), Integer.valueOf(secs)));
               }

               List<Player> players = adapter.getPlayers();
               intent.putParcelableArrayListExtra("players", (ArrayList<Player>) players); // todo to constants
               intent.putExtra("host_index", hostIndex);

               DBHelper dbHelper = new DBHelper(SettingsActivity.this);
               dbHelper.saveUsers(players);

               startActivity(intent);
            }
         }
      });

      countDownCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean useTimer) {
            countDownMin.setEnabled(useTimer);
            countDownSec.setEnabled(useTimer);
         }
      });

      addPlayer.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            AddPlayerDialog addPlayerDialog = new AddPlayerDialog();
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            addPlayerDialog.show(fragmentTransaction, "Dialogogog"); // todo wtf

            addPlayerDialog.setAddPlayerListener(SettingsActivity.this);
         }
      });

      HideKeyboardListener focusChangeListener = HideKeyboardListener.getInstance();
      countDownMin.setOnFocusChangeListener(focusChangeListener);
      countDownSec.setOnFocusChangeListener(focusChangeListener);

      adapter = new MyAdapter(this);

      playerList.setLayoutManager(new LinearLayoutManager(this));
      playerList.setAdapter(adapter);

   }

   private void initViews() {
      addPlayer = (Button) findViewById(R.id.add_player);
      countDownCheck = (CheckBox) findViewById(R.id.count_down_check);
      countDownMin = (EditText) findViewById(R.id.set_count_down_min);
      countDownSec = (EditText) findViewById(R.id.set_count_down_sec);
      playBtn = (Button) findViewById(R.id.start_playing_button);
      playerList = (RecyclerView) findViewById(R.id.user_list);
   }

   @Override
   public void onPlayerAdded(Player player) {
      // todo prohibit add new player with same name
      adapter.addPlayer(player);
      playerList.scrollToPosition(adapter.getItemCount() - 1);
   }

}
