package blq.ssnb.trive.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import blq.ssnb.trive.R;
import blq.ssnb.trive.constant.CommonConstant;
import blq.ssnb.trive.util.DateConvertUtil;

public class HomeInfoActivity extends FragmentActivity {
	private ListView listView ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homeinfo);
		listView = (ListView)findViewById(R.id.homeinfo_listview);
		ListAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1,
				getDate());
		listView.setAdapter(adapter);
	
	}
	private List<String> getDate() {
		
		List<String> list = new ArrayList<String>();
		list.add("所属家庭编号：20003");
		list.add("DWELTYPE:"+CommonConstant.DWELTYPES[1]);
		list.add("OWNED:"+CommonConstant.OWNEDS[1]);
		list.add("PASSPRIV:"+10);
		list.add("PASSGOVT:"+9);
		list.add("MBIKEPRI:"+4);
		list.add("MBIKEGOV:"+12);
		list.add("OTHVPRIV:"+3);
		list.add("OTHVGOVT:"+4);
		list.add("BICYCLES:"+1);
		list.add("注册时间："+DateConvertUtil.yyyy_MM_dd_HH_mm_ss(4294967295000l));
		return list;
	}
}
