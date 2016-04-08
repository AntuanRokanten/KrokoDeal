package com.implemica.krokodeal.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.db.chart.Tools;
import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.view.HorizontalBarChartView;
import com.db.chart.view.XController;
import com.db.chart.view.animation.Animation;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.implemica.krokodeal.Player;
import com.implemica.krokodeal.R;
import com.implemica.krokodeal.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * @author ant
 */
public class ResultActivity extends AppCompatActivity {

   private HorizontalBarChart mChart;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.results_layout);

      Observable<List<Player>> stats = new DBHelper(this).getStats();
      stats.subscribe(new Subscriber<List<Player>>() {
         @Override
         public void onCompleted() {
            System.out.println();
         }

         @Override
         public void onError(Throwable e) {

         }

         @Override
         public void onNext(List<Player> players) {
            System.out.println();
         }
      });

      mChart = (HorizontalBarChart) findViewById(R.id.chart1);

      XAxis xl = mChart.getXAxis();
      xl.setPosition(XAxis.XAxisPosition.BOTTOM);
//      xl.setTypeface(tf);
      xl.setDrawAxisLine(true);
      xl.setDrawGridLines(true);
      xl.setGridLineWidth(0.3f);

      YAxis yl = mChart.getAxisLeft();
//      yl.setTypeface(tf);
      yl.setDrawAxisLine(true);
      yl.setDrawGridLines(true);
      yl.setGridLineWidth(0.3f);
      yl.setAxisMinValue(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

      YAxis yr = mChart.getAxisRight();
//      yr.setTypeface(tf);
      yr.setDrawAxisLine(true);
      yr.setDrawGridLines(false);
      yr.setAxisMinValue(0f);
      mChart.animateY(2500);

      ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
      ArrayList<String> xVals = new ArrayList<String>();

      for (int i = 0; i < 12; i++) {
         xVals.add(String.valueOf(i));
         yVals1.add(new BarEntry((float) (Math.random() * 50), i));
      }

      BarDataSet set1 = new BarDataSet(yVals1, "DataSet 1");

      ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
      dataSets.add(set1);

      BarData data = new BarData(xVals, dataSets);
      data.setValueTextSize(10f);
//      data.setValueTypeface(tf);

      mChart.setData(data);
   }

}
