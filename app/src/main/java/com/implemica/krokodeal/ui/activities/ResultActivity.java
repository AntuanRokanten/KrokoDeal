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
import com.implemica.krokodeal.R;

/**
 * @author ant
 */
public class ResultActivity extends AppCompatActivity {

   private HorizontalBarChartView guessesChart;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.results_layout);

      guessesChart = (HorizontalBarChartView) findViewById(R.id.guessesChart);

      BarSet barSet = new BarSet();

      populateBarSet(barSet);

      guessesChart.addData(barSet);

      guessesChart.setBarSpacing(40);

      guessesChart
            .setXAxis(false).setBorderSpacing(60)
            .setYAxis(false)
            .setLabelsColor(Color.parseColor("#FF8E8A84"))
            .setXLabels(XController.LabelPosition.NONE);

      guessesChart.show();

      int[] order = {2, 0, 1};

      guessesChart.show(new Animation()
            .setOverlap(0.5f, order).setDuration(3000));
   }

   private void populateBarSet(BarSet barSet) {
      Bar bar1 = new Bar("Frst", 22);
      bar1.setColor(Color.parseColor("#77c63d"));
      Bar bar2 = new Bar("Scnd", 1);
      bar2.setColor(Color.parseColor("#27ae60"));
      Bar bar3 = new Bar("Thrd", 52);
      bar3.setColor(Color.parseColor("#47bac1"));

      barSet.addBar(bar1);
      barSet.addBar(bar2);
      barSet.addBar(bar3);
   }
}
