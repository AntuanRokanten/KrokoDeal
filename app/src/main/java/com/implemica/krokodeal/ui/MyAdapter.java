package com.implemica.krokodeal.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.implemica.krokodeal.Player;
import com.implemica.krokodeal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ant
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> { //  todo rename and refactor

   private List<Player> players;
   int hostIndex = -1;

   // Provide a suitable constructor (depends on the kind of dataset)
   public MyAdapter() {
      players = new ArrayList<>();
      players.add(new Player("Ant", false));
      players.add(new Player("Kuz", false));
   }

   public void addPlayer(Player newPlayer) {
      int newPlayerPosition = getItemCount();
      players.add(newPlayerPosition, newPlayer);
      notifyItemInserted(newPlayerPosition);

      if (newPlayer.isHost()) {
         changeHost(newPlayerPosition);
      }
   }

   private void changeHost(int newHostIndex) {
      if (hostIndex >= 0) {
         players.get(hostIndex).setIsHost(false);
         notifyItemChanged(hostIndex);
      }
      hostIndex = newHostIndex;
   }


   @Override
   public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      // create a new view
      View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.player_card, parent, false);
      // set the view's size, margins, paddings and layout parameters
      return new ViewHolder(v);
   }

   @Override
   public void onBindViewHolder(ViewHolder holder, int position) {
      Player player = players.get(position);
      holder.mTextView.setText(player.getName());

      int visibility;
      if (player.isHost()) {
         visibility = View.VISIBLE;
      } else {
         visibility = View.INVISIBLE;
      }
      holder.host.setVisibility(visibility);
   }

   public List<Player> getPlayers() {
      return players;
   }

   public int getHostIndex() {
      return hostIndex;
   }

   @Override
   public int getItemCount() {
      return players.size();
   }

   // Provide a reference to the views for each data item
   // Complex data items may need more than one view per item, and
   // you provide access to all the views for a data item in a view holder
   public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
      // each data item is just a string in this case
      TextView mTextView; //  todo rename and refactor
      TextView host;

      public ViewHolder(View v) {
         super(v);
         mTextView = (TextView) v.findViewById(R.id.player_card_name);
         host = (TextView) v.findViewById(R.id.host_text_field);

         v.setOnClickListener(this);
      }

      @Override
      public void onClick(View v) {
         int clickedPosition = getLayoutPosition();

         if(clickedPosition == hostIndex) {
            return;
         }

         players.get(clickedPosition).setIsHost(true); // todo refactor
         notifyItemChanged(clickedPosition);

         changeHost(clickedPosition);
      }
   }

}
