package blq.ssnb.trive.activity;

import java.util.ArrayList;
import java.util.List;

import blq.ssnb.trive.R;
import blq.ssnb.trive.app.MyApplication;
import blq.ssnb.trive.db.HistoryListDB;
import blq.ssnb.trive.util.DateConvertUtil;
import blq.ssnb.trive.util.MLog;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryActivity extends Activity{

	public static final String DATE_TAG ="cdate";
	private ListView historyList ;
	private List<Integer> historyListDate;
	private OnItemClickListener listItemClickListener =new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(HistoryActivity.this,HistoryShowActivity.class);
			intent.putExtra(DATE_TAG, historyListDate.get(position));
			startActivity(intent);
		}
	} ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		historyListDate = getHistoryList();
		if(historyListDate.size()!=0){
			historyList = (ListView)findViewById(R.id.history_list);
			ListAdapter adapter = new ArrayAdapter<String>(this, 
					android.R.layout.simple_expandable_list_item_1,
					getDate(historyListDate));
			historyList.setAdapter(adapter);
			historyList.setOnItemClickListener(listItemClickListener);
		}else{
			MLog.e("history", "meiyou shuju ");
			TextView view = new TextView(this);
			view.setText("没有历史记录");
			view.setTextSize(20);
			view.setGravity(Gravity.CENTER);
			LinearLayout layout =(LinearLayout)findViewById(R.id.history_main_layout);
			layout.addView(view);
		}
	}	
	
	private List<String> getDate(List<Integer> integerList){
		List<String> list = new ArrayList<String>();
		for (Integer integer : integerList) {
			list.add(
					DateConvertUtil.yyyy_MM_dd(
							DateConvertUtil.TimeStamp(integer.intValue())
							)
					);
		}
		return list;
	}
	
	private List<Integer> getHistoryList(){
		HistoryListDB db = new HistoryListDB(this);
		return db.getHistoryList(MyApplication.getInstance().getUserInfo().getEmail());
	}
}
