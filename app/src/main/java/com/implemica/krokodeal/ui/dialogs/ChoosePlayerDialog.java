package com.implemica.krokodeal.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.implemica.krokodeal.ChooseWinnerListener;
import com.implemica.krokodeal.Player;
import com.implemica.krokodeal.R;

import java.util.List;

/**
 * @author ant
 */
public class ChoosePlayerDialog extends AppCompatDialogFragment {

   private RecyclerView playersList;
   private List<Player> players;
   private ChooseWinnerListener chooseWinnerListener;

   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {
      final View view = getActivity().getLayoutInflater().inflate(R.layout.user_list_dialog, null);
      playersList = (RecyclerView) view.findViewById(R.id.player_list);

      playersList.setLayoutManager(new LinearLayoutManager(getActivity()));
      playersList.setAdapter(new PlayersListAdapter());

      players = getArguments().getParcelableArrayList("players");

      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
      builder.setView(view);

      return builder.create();
   }

   public void setChooseWinnerListener(ChooseWinnerListener chooseWinnerListener) {
      this.chooseWinnerListener = chooseWinnerListener;
   }

   private class PlayersListAdapter extends RecyclerView.Adapter<PlayersListAdapter.ViewHolder> {

      @Override
      public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View v = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.player_card, parent, false);
         return new ViewHolder(v);
      }

      @Override
      public void onBindViewHolder(ViewHolder holder, int position) {
         Player player = players.get(position);
         holder.mTextView.setText(player.getName());
         if (player.isHost()) {
            holder.cardView.setVisibility(View.GONE);
         }
      }

      @Override
      public int getItemCount() {
         return players.size();
      }

      public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         // each data item is just a string in this case
         CardView cardView;
         TextView mTextView; //  todo rename and refactor
         TextView host;

         public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.player_card);
            mTextView = (TextView) v.findViewById(R.id.player_card_name);
            host = (TextView) v.findViewById(R.id.host_text_field);

            v.setOnClickListener(this);
         }

         @Override
         public void onClick(View v) {
            chooseWinnerListener.onWinnerChosen(players.get(getLayoutPosition()));
            ChoosePlayerDialog.this.dismiss();
         }
      }

   }
}
