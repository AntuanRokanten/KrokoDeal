package com.implemica.krokodeal.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.implemica.krokodeal.R;

/**
 * @author ant
 */
public class MainActivity extends AppCompatActivity {

   private Button playButton;
   private Button rulesButton;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main_activity);

      initViews();

      playButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
         }
      });

      rulesButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, RulesActivity.class));
         }
      });
   }

   private void initViews() {
      playButton = (Button) findViewById(R.id.main_play_btn);
      rulesButton = (Button) findViewById(R.id.btn_rules);
   }

}
