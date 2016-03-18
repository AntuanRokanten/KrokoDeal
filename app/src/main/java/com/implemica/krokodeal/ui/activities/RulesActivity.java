package com.implemica.krokodeal.ui.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import com.implemica.krokodeal.R;

/**
 * @author ant
 */
public class RulesActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.rules_layout);

      TextView rules = (TextView) findViewById(R.id.rules_text);
      rules.setText(Html.fromHtml(getString(R.string.game_rules)));

      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      ActionBar actionBar = getSupportActionBar();

      if(actionBar != null) {
         actionBar.setDisplayHomeAsUpEnabled(true);
      }
   }
}
