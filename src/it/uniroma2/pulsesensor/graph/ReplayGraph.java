package it.uniroma2.pulsesensor.graph;

import java.sql.Date;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;

public class ReplayGraph {
	
	private JSONArray jsonArray;
	private int uid;

	
	public ReplayGraph(String jsonString, int uid)
	{
		
		double[] timestamps = {};
		double[] values = {};
		
		try {
			this.jsonArray = new JSONArray(jsonString);
			this.uid = uid;
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("tank", "Errore creazione oggetto json array ");
		}
		
	}
	
	public Intent getIntent(Context context)
	{
		
//		int[] x = {1,2,3,4,5,6,7,8,9,10};
//		int[] y = {1,2,4,5,3,4,7,8,9,10};
		
		TimeSeries series = new TimeSeries(""+uid);
		JSONObject temp = null;
		long xtemp = 0;
		Double ytemp = 0.0;
		
		for(int i=0; i<jsonArray.length(); i++ ) {
//		for(int i=0; i<x.length; i++ ) {	
//			series.add(x[i], y[i]);

			try {
				temp = (JSONObject) jsonArray.get(i);
				xtemp = Long.valueOf(temp.getString("timestamp"));
				ytemp = Double.valueOf(temp.getString("value"));
				Log.w("tank", "Timestamp: "+xtemp+" Value: "+ytemp);
				Date d = new Date(xtemp);
				series.add(d, ytemp);
				
			} catch (JSONException e){
				e.printStackTrace();
			}			
			
		}
			
		XYMultipleSeriesDataset dataset =  new XYMultipleSeriesDataset();
		dataset.addSeries(series);
		
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		renderer.setColor(Color.BLUE);
		renderer.setPointStyle(PointStyle.POINT);
		renderer.setFillPoints(true);
		
		
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.setXLabels(6);	//# label asse x
		mRenderer.setXLabelsAngle(45);
		mRenderer.setYLabels(10);	//# label asse y
	    mRenderer.setShowGrid(true);
	    mRenderer.setXLabelsAlign(Align.LEFT);
	    mRenderer.setYLabelsAlign(Align.RIGHT);
	    mRenderer.setZoomButtonsVisible(true);
	    
	    double xMin = series.getX(0);
	    double xMax = series.getMaxX();
	    double yMin = 0;
	    double yMax = series.getY(jsonArray.length()-1);
	    
	    
	    mRenderer.setXAxisMin(xMin);
	    mRenderer.setXAxisMax(xMax);
	    /*
	    mRenderer.setYAxisMin(0);
	    mRenderer.setYAxisMax(150);
	    
	    mRenderer.setPanLimits(new double[] { xMin, xMax, 0, 150 });
	    mRenderer.setZoomLimits(new double[] { xMin, xMax, 0, 150 });
	    */
//	    renderer.setChartTitle(title);
//	    renderer.setXTitle(xTitle);
//	    renderer.setYTitle(yTitle);
	    Log.w("tank", series.getX(0)+" "+series.getX(0));
	    Log.w("tank", series.getX(0)+" "+series.getX(jsonArray.length()-1));
	    
//	    mRenderer.setAxesColor(Color.WHITE);
//	    mRenderer.setLabelsColor(Color.WHITE);
	    
		Intent intent = ChartFactory.getTimeChartIntent(context, dataset, mRenderer, "Replay Mode");
		
		return intent;
		
	}
	
	

}
