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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/*
public class ChartDemo extends ListActivity {
	  
	private IDemoChart[] mCharts = new IDemoChart[] { new ReplayChart() };

	private String[] mMenuText;

	private String[] mMenuSummary;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    Intent rcv_intent = getIntent();
	    
	    if(rcv_intent.getExtras().getString("mode") == "replay")
	    	replay(rcv_intent);
	    else if(rcv_intent.getExtras().getString("mode") == "realtime")
	    	realtime(rcv_intent);
	    	
	}
	
	public void replay(Intent i) {
	
		Log.w("tank", "replay execute");
		Intent intent = mCharts[0].execute(this);
		
		intent.putExtra("mode", i.getStringExtra("mode"));
		intent.putExtra("uid", i.getStringExtra("uid"));
		intent.putExtra("from", i.getStringExtra("from"));
		intent.putExtra("to", i.getStringExtra("to"));
		intent.putExtra("json", i.getStringExtra("json"));
		
		Log.w("tank", "intent replay");
		startActivity(intent);	
	}
	
	public void realtime(Intent i) {
		
	}

}
*/

public class ChartDemo extends ListActivity {
	
  private IDemoChart[] mCharts = new IDemoChart[] { new ReplayChart() };

  private String[] mMenuText;

  private String[] mMenuSummary;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    int length = mCharts.length;
    mMenuText = new String[length];
    mMenuSummary = new String[length];
    
    for (int i = 0; i < length; i++) {
      mMenuText[i] = mCharts[i].getName();
      mMenuSummary[i] = mCharts[i].getDesc();
    }
    
    setListAdapter(new SimpleAdapter(this, getListValues(), android.R.layout.simple_list_item_2,
        new String[] { IDemoChart.NAME, IDemoChart.DESC }, new int[] { android.R.id.text1,
            android.R.id.text2 }));
  }

  private List<Map<String, String>> getListValues() {
    List<Map<String, String>> values = new ArrayList<Map<String, String>>();
    int length = mMenuText.length;
    for (int i = 0; i < length; i++) {
      Map<String, String> v = new HashMap<String, String>();
      v.put(IDemoChart.NAME, mMenuText[i]);
      v.put(IDemoChart.DESC, mMenuSummary[i]);
      values.add(v);
    }
    return values;
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    //TODO decommentare
//    Intent rcv_intent = getIntent();
    Intent rcv = getIntent();
    Intent intent = null;
    if (position < mCharts.length) {
      intent = mCharts[position].execute(this);
      //TODO aggiungere blocco sotto per passaggio extra
      if(mCharts[position].getName() == "Replay Chart"){
    	
    	intent.putExtra("uid", rcv.getExtras().getString("uid"));
      	intent.putExtra("from", rcv.getExtras().getString("from"));
      	intent.putExtra("to", rcv.getExtras().getString("to"));
      	intent.putExtra("json", rcv.getExtras().getString("json"));
      
      }else if(mCharts[position].getName() == "Realtime Chart"){
    	intent.putExtra("uid", rcv.getExtras().getString("uid"));
      }
    }
    startActivity(intent);
  }
}
