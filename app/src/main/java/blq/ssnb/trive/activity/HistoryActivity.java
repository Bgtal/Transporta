package blq.ssnb.trive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import blq.ssnb.trive.R;
import blq.ssnb.trive.app.MyApplication;
import blq.ssnb.trive.db.HistoryListDB;
import blq.ssnb.trive.util.DateConvertUtil;
import blq.ssnb.trive.util.MLog;

public class HistoryActivity extends AppCompatActivity {
    public static final String DATE_TAG ="cdate";
    private List<Integer> historyListDate;

    private AdapterView.OnItemClickListener listItemClickListener =new AdapterView.OnItemClickListener() {

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
        setContentView(R.layout.activity_new_history);
        initToolBarView();
        init();

    }

    private void initToolBarView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView backBtn = (ImageView) findViewById(R.id.nv_back);
        assert backBtn != null;
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryActivity.this.finish();
            }
        });
        TextView titleView = (TextView) findViewById(R.id.nv_title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        assert titleView != null;
        titleView.setText("HistoryList");
    }

    private void init() {
        historyListDate = getHistoryList();
        if(historyListDate.size()!=0){
            ListView historyList = (ListView) findViewById(R.id.history_list);
            ListAdapter adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_expandable_list_item_1,
                    getDate(historyListDate));
            historyList.setAdapter(adapter);
            historyList.setOnItemClickListener(listItemClickListener);
        }else{
            MLog.e("history", "meiyou shuju ");
            TextView view = new TextView(this);
            view.setText("No History");
            view.setTextSize(20);
            view.setGravity(Gravity.CENTER);
            findViewById(R.id.no_history_record).setVisibility(View.VISIBLE);
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
