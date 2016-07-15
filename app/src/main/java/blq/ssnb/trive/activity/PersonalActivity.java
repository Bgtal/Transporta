package blq.ssnb.trive.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import blq.ssnb.trive.R;

public class PersonalActivity extends Activity {

	private ListView listView ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		listView = (ListView)findViewById(R.id.personal_listview);
		ListAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1,
				getDate());
		listView.setAdapter(adapter);
	}
	
	private List<String> getDate(){
//		UserInfo info = MyApplication.getInstance().getUserInfo();
		List<String> list = new ArrayList<String>();
	/*	list.add("用户名:"+info.getPhone());
		list.add("所属家庭编号："+info.getHhid());
		list.add("年龄："+info.getAge());
		list.add("性别:"+info.getSex());
		list.add("行业:"+info.getIndustry());
		list.add("职业:"+info.getOccup());
		list.add(
				"注册时间："+DateConvertUtil.yyyy_MM_dd_HH_mm_ss(
						DateConvertUtil.TimeStamp(
								info.getRegisterTime()
								)
				)
		);*/
		return  list;
	}
}
