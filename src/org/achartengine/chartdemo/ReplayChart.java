/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.achartengine.chartdemo;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;

/**
 * Average temperature demo chart.
 */
public class ReplayChart extends AbstractDemoChart {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	/**
	 * Returns the chart name.
	 * 
	 * @return the chart name
	 */
	public String getName() {
		return "Replay Chart";
	}

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
    return "Values from database";
  }

  /**
   * Executes the chart demo.
   * 
   * @param context the context
   * @return the built intent
   */
  public Intent execute(Context context) {
	
	Log.w("tank", "execute");

	double limitXLow = 0;
    double limitXHigh = 100;
	
//	String uid =  intent.getExtras().getString("uid"); 
//    double limitXLow = Double.valueOf(intent.getExtras().getString("from"));
//    double limitXHigh = Double.valueOf(intent.getExtras().getString("to"));
    
    /*
    JSONArray objarr = null;
    JSONObject temp = null;
    double[] timestamps = {};
    double[] values = {};
    
	try {
		objarr = new JSONArray(intent.getExtras().getString("json"));
		
		ArrayList<Double> ylist = new ArrayList<Double>();
		timestamps = new double[objarr.length()];
		values = new double[objarr.length()];
		
		for (int i = 0; i < objarr.length(); i++) {
			
			temp = (JSONObject) objarr.get(i);
			timestamps[i] = (double) (Double.valueOf(temp.getString("timestamp")));
			values[i] = (double) (Double.valueOf(temp.getString("values")));
		}
		
	} catch (JSONException e) {
		e.printStackTrace();
		Log.e("tank", "replay jsonarray error");
	}
	*/
	
    String[] titles = new String[] {"uid"};
    
    List<double[]> x = new ArrayList<double[]>(); 
//    x.add(timestamps);
    x.add(new double[]{});
    
    List<double[]> y = new ArrayList<double[]>();
//    y.add(values);
    y.add(new double[]{});
    
    int[] colors = new int[] { Color.BLUE};		//Imposto colore grafico
    PointStyle[] styles = new PointStyle[] { PointStyle.POINT};		//Imposto stile grafico, PointStyle.POINT
    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
    int length = renderer.getSeriesRendererCount();
    
    for (int i = 0; i < length; i++) {
      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
    }
    
    //Imposta le scritte intorno al grafico e i limiti da visulizzare all'inizio
    setChartSettings(renderer, "Replay", "Time", "Value", limitXLow, limitXHigh, -10, 40,  Color.WHITE, Color.WHITE);
    
    renderer.setXLabels(12);	//# label asse x 
    renderer.setYLabels(10);	//# label asse y
    renderer.setShowGrid(true);
    renderer.setXLabelsAlign(Align.RIGHT);
    renderer.setYLabelsAlign(Align.RIGHT);
    renderer.setZoomButtonsVisible(true);
    renderer.setPanLimits(new double[] { limitXLow, limitXHigh, 0, 550 });
    renderer.setZoomLimits(new double[] { limitXLow, limitXHigh, 0, 550 });
  

    XYMultipleSeriesDataset dataset = buildDataset(titles, x, y);
    XYSeries series = dataset.getSeriesAt(0);
    
    Log.w("tank", "execute return");
    return ChartFactory.getLineChartIntent(context, dataset, renderer,"Replay Mode");
  }

}
