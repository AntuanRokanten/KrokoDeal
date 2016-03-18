package com.implemica.krokodeal.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.implemica.krokodeal.AddPlayerListener;
import com.implemica.krokodeal.Player;
import com.implemica.krokodeal.R;
import com.implemica.krokodeal.database.DBHelper;
import com.implemica.krokodeal.ui.PlayersAdapter;
import com.implemica.krokodeal.ui.dialogs.AddPlayerDialog;
import com.implemica.krokodeal.ui.listeners.HideKeyboardListener;
import com.implemica.krokodeal.util.TimerData;
import com.implemica.krokodeal.util.UiUtils;

import java.util.ArrayList;
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

   private PlayersAdapter adapter;

   private View rootView;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      rootView = getLayoutInflater().inflate(R.layout.settings_activity, null);
      setContentView(rootView);

      initViews();

      playBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int hostIndex = adapter.getHostIndex();

            List<Player> players = adapter.getPlayers();

            if (players.size() < 3) {
               UiUtils.showPinkSnackbar(rootView, SettingsActivity.this, R.string.snackbar_add_more_players);
               return;
            }

            if (hostIndex < 0) {
               UiUtils.showPinkSnackbar(rootView, SettingsActivity.this, R.string.snackbar_choose_host);
               return;
            }

            Intent intent = new Intent(SettingsActivity.this, PlayActivity.class);

            if (countDownCheck.isChecked()) {
               String secondsText = countDownSec.getText().toString();
               String minutesText = countDownMin.getText().toString();

               if (secondsText.isEmpty() && minutesText.isEmpty()) {
                  UiUtils.showPinkSnackbar(rootView, SettingsActivity.this, R.string.snackbar_set_timer);
                  return;
               }

               String secs = secondsText.isEmpty() ? "0" : secondsText;
               String mins = minutesText.isEmpty() ? "0" : minutesText;

               if (Integer.valueOf(secs) >= 60) {
                  UiUtils.showPinkSnackbar(rootView, SettingsActivity.this, R.string.snackbar_set_timer_correct);
                  return;
               }
               intent.putExtra("timer", new TimerData(Integer.valueOf(mins), Integer.valueOf(secs)));
            }

            intent.putParcelableArrayListExtra("players", (ArrayList<Player>) players); // todo to constants
            intent.putExtra("host_index", hostIndex);

            DBHelper dbHelper = new DBHelper(SettingsActivity.this);
            dbHelper.saveUsers(players);

            startActivity(intent);

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

      adapter = new PlayersAdapter(this);

      playerList.setLayoutManager(new LinearLayoutManager(this));
      playerList.setAdapter(adapter);

      ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
         @Override
         public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false; // always return false
         }

         @Override
         public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            adapter.removePlayer(viewHolder.getAdapterPosition());
         }
      });
      itemTouchHelper.attachToRecyclerView(playerList);

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
      try {
         adapter.addPlayer(player);
         playerList.scrollToPosition(adapter.getItemCount() - 1);
      } catch (PlayersAdapter.PlayerWithThisNameExistsException e) {
         UiUtils.showPinkSnackbar(rootView, SettingsActivity.this, R.string.snackbar_player_exists);
      }
   }

}
