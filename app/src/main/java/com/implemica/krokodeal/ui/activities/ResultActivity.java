package com.implemica.krokodeal.ui.activities;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.implemica.krokodeal.Player;
import com.implemica.krokodeal.R;
import com.implemica.krokodeal.database.DBHelper;
import com.implemica.krokodeal.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * @author ant
 */
public class ResultActivity extends AppCompatActivity {

   private static final StatsValueFormatter VALUE_FORMATTER = new StatsValueFormatter();

   private HorizontalBarChart mChart;
   private ProgressDialog progress;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      final View rootView = getLayoutInflater().inflate(R.layout.results_layout, null);
      setContentView(rootView);

      progress = new ProgressDialog(this);
      progress.setTitle("please wait...");
      progress.setMessage("wait...");
      progress.setCancelable(false);
      progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      progress.show();

      final List<Player> playerStats = new ArrayList<>();
      final Observable<List<Player>> stats = new DBHelper(this).getStats();
      stats.subscribe(new Subscriber<List<Player>>() {
         @Override
         public void onCompleted() {
            progress.dismiss();
            showStats(playerStats);
         }

         @Override
         public void onError(Throwable e) {
            progress.dismiss();
            UiUtils.showPinkSnackbar(rootView, ResultActivity.this, R.string.stats_load_failure);
         }

         @Override
         public void onNext(List<Player> players) {
            playerStats.addAll(players);
         }
      });


   }

   private void showStats(List<Player> playerStats) {
      Typeface tf = Typeface.createFromAsset(getAssets(), "roboto.ttf");

      mChart = (HorizontalBarChart) findViewById(R.id.chart1);
      mChart.getLegend().setWordWrapEnabled(true);

      XAxis xl = mChart.getXAxis();
      xl.setTypeface(tf);
      xl.setPosition(XAxis.XAxisPosition.BOTTOM);
      xl.setDrawAxisLine(true);
      xl.setDrawGridLines(true);
      xl.setGridLineWidth(0.5f);

      YAxis yl = mChart.getAxisLeft();
      yl.setDrawAxisLine(false);
      yl.setDrawLabels(false);
      yl.setDrawGridLines(false);
      yl.setGridLineWidth(0.3f);
      yl.setAxisMinValue(0f); // this replaces setStartAtZero(true)

      YAxis yr = mChart.getAxisRight();
      yr.setDrawAxisLine(false);
      yr.setDrawLabels(false);
      yr.setDrawGridLines(false);
      yr.setAxisMinValue(0f);
      mChart.animateY(2500);
      mChart.setDescription("");
      mChart.getLegend().setTypeface(tf);

      ArrayList<BarEntry> guessesEntry = new ArrayList<>();
      ArrayList<BarEntry> showSuccessEntry = new ArrayList<>();
      ArrayList<BarEntry> showFailureEntry = new ArrayList<>();

      ArrayList<String> xVals = new ArrayList<>();

      int index = 0;
      for (Player playerStat : playerStats) {
         xVals.add(playerStat.getName());
         guessesEntry.add(new BarEntry(playerStat.getGuesses(), index));
         showSuccessEntry.add(new BarEntry(playerStat.getShowSuccess(), index));
         showFailureEntry.add(new BarEntry(playerStat.getShowFailure(), index++));
      }

      Resources resources = getResources();
      BarDataSet set1 = new BarDataSet(guessesEntry, resources.getString(R.string.guessed_stat));
      set1.setValueFormatter(VALUE_FORMATTER);

      BarDataSet set2 = new BarDataSet(showSuccessEntry, resources.getString(R.string.show_success));
      set2.setValueFormatter(VALUE_FORMATTER);

      BarDataSet set3 = new BarDataSet(showFailureEntry, resources.getString(R.string.show_failure));
      set3.setValueFormatter(VALUE_FORMATTER);

      set1.setColor(resources.getColor(R.color.colorPrimaryDark));
      set2.setColor(resources.getColor(R.color.accent));
      set3.setColor(resources.getColor(R.color.pink));

      ArrayList<IBarDataSet> dataSets = new ArrayList<>();
      dataSets.add(set1);
      dataSets.add(set2);
      dataSets.add(set3);

      BarData data = new BarData(xVals, dataSets);
      data.setValueTextSize(10f);

      mChart.setData(data);
   }

   private static class StatsValueFormatter implements ValueFormatter {
      @Override
      public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
         return Math.round(value)+"";
      }
   }

}
