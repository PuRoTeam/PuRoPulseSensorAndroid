package it.uniroma2.pulsesensor.graph;


import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

public class RealGraph {
	
	private GraphicalView view;
	
	private TimeSeries dataset = new TimeSeries("Heart beat");
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	
	private XYSeriesRenderer renderer = new XYSeriesRenderer();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	
	public RealGraph()
	{
		//Add single dataset to multiple dataset
		mDataset.addSeries(dataset);
		
		//Customization
		renderer.setColor(Color.BLUE);
		renderer.setPointStyle(PointStyle.POINT);
		renderer.setFillPoints(true);
		
		//Enable zoom
		mRenderer.setZoomEnabled(true);
		mRenderer.setXTitle("Day #");
		mRenderer.setYTitle("Beat #");
		
		//Add single renderer to multiple renderer
		mRenderer.addSeriesRenderer(renderer);
		
	}
	
	public GraphicalView getView(Context context)
	{
		view = ChartFactory.getTimeChartView(context, mDataset, mRenderer, null);
		return view;
	}

	public void addNewPoint(Point p)
	{
		dataset.add(p.getX(), p.getY());
		
		if(dataset.getItemCount() >= 60)
			dataset.remove(0);
	}
}
