package com.implemica.krokodeal.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.implemica.krokodeal.AddPlayerListener;
import com.implemica.krokodeal.Player;
import com.implemica.krokodeal.R;

/**
 * @author ant
 */
public class AddPlayerDialog extends AppCompatDialogFragment {

   private AddPlayerListener listener;

   @Override
   public Dialog onCreateDialog(Bundle savedInstanceState) {

      final View view = getActivity().getLayoutInflater().inflate(R.layout.add_player_dialog, null);

      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);

      builder.setView(view);
      builder.setMessage(R.string.add_new_player);
      builder.setPositiveButton(R.string.add_confirm, new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
            if (listener != null) {
               EditText newPlayerName = (EditText) view.findViewById(R.id.new_player_name);
               CheckBox hostCheckBox = (CheckBox) view.findViewById(R.id.host_check_box);

               String name = newPlayerName.getText().toString();

               if (!name.isEmpty()) {
                  boolean isHost = hostCheckBox.isChecked();
                  listener.onPlayerAdded(new Player(name, isHost));
               }
            }
         }
      });
      builder.setNegativeButton(R.string.add_cancel, null);

      return builder.create();
   }

   public void setAddPlayerListener(AddPlayerListener listener) {
      this.listener = listener;
   }
}
