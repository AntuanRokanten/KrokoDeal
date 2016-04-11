package com.implemica.krokodeal.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.implemica.krokodeal.Player;
import com.implemica.krokodeal.R;
import com.implemica.krokodeal.ui.fragments.PlayShowFragment;
import com.implemica.krokodeal.ui.fragments.SetWordFragment;
import com.implemica.krokodeal.util.TimerData;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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

   private Drawer drawer;

   private int hostIndex;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.play_activity);

      initViews();
      Toolbar toolbar = (Toolbar) findViewById(R.id.play_toolbar);
      setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      configDrawer(toolbar);

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

   @Override
   protected void onResume() {
      super.onResume();

//      if(drawer.isDrawerOpen()) {
//         drawer.setSelection(-1);
//      }
   }

   private void configDrawer(Toolbar toolbar) {

      drawer = new DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true)
            .withHeader(R.layout.drayer_header)
            .addDrawerItems(
                  createSecondaryDrawerItem(R.string.results, R.drawable.result_icon, 1),
                  createSecondaryDrawerItem(R.string.settings, R.drawable.settings_icon, 2),
                  new DividerDrawerItem())
            .withOnDrawerItemClickListener(new DrawerOnClickListener())
            .withSliderBackgroundColorRes(R.color.accent)
            .withStickyFooterShadow(false)
            .withFooterDivider(true)
            .withSelectedItem(-1)
            .build();
//      drawer.addStickyFooterItem(new SecondaryDrawerItem()
//            .withName("О программе")
//            .withSelectedColor(Color.CYAN).withSelectedColor(Color.MAGENTA)
//            .withTextColor(Color.BLUE)); // todo
   }

   private SecondaryDrawerItem createSecondaryDrawerItem(int nameRes, int iconRes, long identifier) {
      return new SecondaryDrawerItem()
            .withName(getResources().getString(nameRes))
            .withIdentifier(identifier)
            .withTextColor(Color.WHITE)
            .withSelectedTextColorRes(R.color.colorAccent)
            .withIcon(iconRes)
            .withSelectedColorRes(R.color.colorPrimary)
            .withSelectedIconColorRes(R.color.colorAccent);
   }

   @Override
   public void onBackPressed() {
      if (drawer.isDrawerOpen()) {
         drawer.closeDrawer(); // closing drawing on back pressed if it's open
      } else {
         super.onBackPressed();
      }
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

   public void changeHost(Player newHost) {
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

   private class DrawerOnClickListener implements Drawer.OnDrawerItemClickListener {

      @Override
      public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
         boolean result = false;
         long identifier = drawerItem.getIdentifier();

         if (identifier == 1) { // 'results' item clicked
            startActivity(new Intent(PlayActivity.this, ResultActivity.class));
            result = true;
         } else if (identifier == 2) { // 'settings' item clicked
            startActivity(new Intent(PlayActivity.this,  SettingsActivity.class));
            result = true;
         }

         return result;
      }

   }
}
