package com.implemica.krokodeal.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.implemica.krokodeal.AddPlayerListener;
import com.implemica.krokodeal.Player;
import com.implemica.krokodeal.R;
import com.implemica.krokodeal.ui.MyAdapter;
import com.implemica.krokodeal.ui.dialogs.AddPlayerDialog;
import com.implemica.krokodeal.utils.TimerData;

import java.util.ArrayList;

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
                  String secs = countDownSec.getText().toString();
                  String mins = countDownMin.getText().toString();
                  if(mins.isEmpty() || secs.isEmpty() || Integer.valueOf(secs) >= 60) {
                     // todo shot snackbar
                     return;
                  }
                  intent.putExtra("timer", new TimerData(Integer.valueOf(mins), Integer.valueOf(secs)));
               }

               intent.putParcelableArrayListExtra("players", (ArrayList<Player>) adapter.getPlayers()); // todo to constants
               intent.putExtra("host_index", hostIndex);

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
            addPlayerDialog.show(fragmentTransaction, "Dialogogog");

            addPlayerDialog.setAddPlayerListener(SettingsActivity.this);
         }
      });

      adapter = new MyAdapter();

      playerList.setLayoutManager(new LinearLayoutManager(this));
      playerList.setAdapter(adapter);
   }

   private void initViews() {
      addPlayer = (Button) findViewById(R.id.add_player);
      countDownCheck = (CheckBox) findViewById(R.id.count_down_check);
      countDownMin = (EditText) findViewById(R.id.set_count_down_min);
      countDownSec = (EditText) findViewById(R.id.set_count_down_sec);
      playerList = (RecyclerView) findViewById(R.id.user_list);
      playBtn = (Button) findViewById(R.id.start_playing_button);
      playerList = (RecyclerView) findViewById(R.id.user_list);
   }

   @Override
   public void onPlayerAdded(Player player) {
      adapter.addPlayer(player);
      playerList.scrollToPosition(adapter.getItemCount() - 1);
   }
}
