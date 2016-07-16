package blq.ssnb.trive.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import blq.ssnb.trive.R;
import blq.ssnb.trive.app.AppManager;
import blq.ssnb.trive.app.MyApplication;
import blq.ssnb.trive.constant.CommonConstant;
import blq.ssnb.trive.constant.HttpConstant;
import blq.ssnb.trive.db.HistoryListDB;
import blq.ssnb.trive.db.TripPointDB;
import blq.ssnb.trive.db.json.AllTripToJson;
import blq.ssnb.trive.http.okhttp.OkHttpUtils;
import blq.ssnb.trive.http.okhttp.callback.StringCallback;
import blq.ssnb.trive.model.TripPointInfo;
import blq.ssnb.trive.model.TripPointInfo.DrawStyle;
import blq.ssnb.trive.service.RecordCallBackHandler;
import blq.ssnb.trive.service.RecordManager;
import blq.ssnb.trive.service.RecordingService;
import blq.ssnb.trive.util.DateConvertUtil;
import blq.ssnb.trive.util.MLog;
import blq.ssnb.trive.util.MapUtil;
import blq.ssnb.trive.util.TUtil;
import okhttp3.Call;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
		OnMarkerClickListener, OnMapClickListener {

	private Context context;
//	private GoogleMap mMap;
	private boolean isBind = false;
	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			RecordManager.getInstance().setActivityRequestCallBack(myHandler);
			isBind = true;
		}
	};

	private RecordCallBackHandler myHandler = new RecordCallBackHandler() {

		@Override
		public void mark(Location location) {

			LatLng address = new LatLng(location.getLatitude(), location.getLongitude());
			MarkerOptions mark = new MarkerOptions();
			mark.position(address);
			mark.title(MapUtil.LatLng2String(address));
			mark.snippet(DateConvertUtil.yyyy_MM_dd_HH_mm_ss(location.getTime()));
			MarkList.add(mark);
			mMap.addMarker(mark);
			line(location);
		}

		@Override
		public void line(Location location) {
			LatLng address = new LatLng(location.getLatitude(), location.getLongitude());
			lineList.add(address);
			updateLine(mMap, lineList);
		}
	};

	private List<LatLng> lineList = new ArrayList<>();
	private List<MarkerOptions> MarkList = new ArrayList<>();
	private List<TripPointInfo> MarkTripInfo = new ArrayList<>();

	private TripPointInfo selectTripInfo;
	private ScrollView edit_scroll;
	private Spinner resonSpinner, waySpinner;
	private EditText contentEdit;
	private int resonIndex, wayIndex;
	private Button updateBtn;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (canShow()) {
			recordUpdate();
		}
	}

	private void recordUpdate() {
		edit_scroll = (ScrollView) findViewById(R.id.edit_stop_info_scroll);
		resonSpinner = (Spinner) findViewById(R.id.marker_spinner_reson);
		waySpinner = (Spinner) findViewById(R.id.marker_spinner_way);
		contentEdit = (EditText) findViewById(R.id.mark_edit);
//		updateBtn = (Button) findViewById(R.id.main_updateBtn);
		ArrayAdapter<String> resonAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, CommonConstant.RESON);
		ArrayAdapter<String> wayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, CommonConstant.WAY);
		resonSpinner.setAdapter(resonAdapter);
		waySpinner.setAdapter(wayAdapter);

		resonSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				resonIndex = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}


		});
		waySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				wayIndex = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		updateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HashMap<String, String> map = new HashMap<String, String>();
				AllTripToJson js = new AllTripToJson(context);
				map.put("TRIPINFO", js.allTripInfoToJSON(MyApplication.getInstance().getUserInfo().getEmail()).toString());
				map.put("PERSID", MyApplication.getInstance().getUserInfo().getEmail());
				map.put("ACTTDATE", DateConvertUtil.TimeStamp() + "");
				updateInfo(map);
			}
		});
		updateBtn.setVisibility(View.VISIBLE);

	}

	private void updateInfo(HashMap<String, String> map) {
		OkHttpUtils
				.post()
				.url(HttpConstant.UPDATE)
				.params(map)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						TUtil.TLong(R.string.update_fail);
					}

					@Override
					public void onResponse(String response, int id) {
						TUtil.TLong(R.string.update_success);
						HistoryListDB db = new HistoryListDB(MyApplication.getInstance().getApplicationContext());
						db.instertHistory(MyApplication.getInstance().getUserInfo().getEmail(), DateConvertUtil.DateTodayInt());
					}
				});
	}

	@Override
	public void onMapReady(GoogleMap map) {
		draw();
		if(canShow()){
			mMap.setOnMarkerClickListener(this);
			mMap.setOnMapClickListener(this);
		}
	}

	private void draw(){
		TripPointDB db = new TripPointDB(this);
		List<TripPointInfo> info = new ArrayList<TripPointInfo>();
		if(MyApplication.getInstance().getUserInfo()!=null){
			String uid = MyApplication.getInstance().getUserInfo().getEmail();
			if(uid!=null){
				info =db.selectTripPoint(
						DateConvertUtil.DateTodayInt(),
						DateConvertUtil.DateTodayInt()+CommonConstant.ONE_DAY_INT,
						uid);		
			}
		}


		mMap.clear();
		lineList = new ArrayList<LatLng>();
		MarkList = new ArrayList<MarkerOptions>();
		int i = 0;
		for (TripPointInfo tripPointInfo : info) {
			if(tripPointInfo.getStyle()==DrawStyle.LINE){
				lineList.add(tripPointInfo.getAddress());
			}else{
				MarkerOptions mark = new MarkerOptions();
				mark.position(tripPointInfo.getAddress());
				mark.title(i+"");
				i++;
				mark.snippet(DateConvertUtil.yyyy_MM_dd_HH_mm_ss(tripPointInfo.getStamp()*1000l));
				MarkList.add(mark);
				lineList.add(tripPointInfo.getAddress());
				MarkTripInfo.add(tripPointInfo);
				mMap.addMarker(mark);
			}
		}
		updateLine(mMap,lineList);	
		db =null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(mMap!=null){
			draw();
			Intent serviceIntent = new Intent(this,RecordingService.class);
			bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
			RecordManager.getInstance().setActivityRequestCallBack(myHandler);
		}
	}
	/**
	 * onResume()之后
	 * 不安全的用于保存当前状态
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if(isBind){
			this.unbindService(conn);
			RecordManager.getInstance().setActivityRequestCallBack(null);
			isBind=false;
		}
	}

	/**
	 * onStop()之后
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MLog.e("main", "destroy");
	}
	private void updateLine(GoogleMap googleMap,List<LatLng> lines){
		if(lines==null||lines.size()<1){
			return;	
		}
		PolylineOptions polylineOptions = new PolylineOptions().addAll(lines).width(5).color(Color.GREEN);
		googleMap.addPolyline(polylineOptions);
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lines.get(lines.size()-1),16.5f));
	}


	public void cancel(View v){
		closeInfo();
	}

	public void delete(View v){
		TripPointDB tdb = new TripPointDB(context);
		tdb.deleteStopPoint(selectTripInfo.getPid());
		draw();
		tdb=null;
		closeInfo();
	}

	public void save(View v){
		selectTripInfo.setReason(resonIndex);
		selectTripInfo.setWay(wayIndex);
		selectTripInfo.setContent(contentEdit.getText().toString().trim());
		TripPointDB tdb = new TripPointDB(context);
		tdb.updateStopInfo(selectTripInfo);
		tdb = null;
		draw();
		closeInfo();
	}

	@Override
	public void onMapClick(LatLng latlng) {
		closeInfo();
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		selectTripInfo = MarkTripInfo.get(Integer.parseInt(marker.getTitle()));
		openInfo(selectTripInfo);
		return false;
	}
	private void openInfo(TripPointInfo info){
		if(edit_scroll.getVisibility()==View.GONE){
			edit_scroll.setVisibility(View.VISIBLE);			
		}
		resonSpinner.setSelection(info.getReason());
		waySpinner.setSelection(info.getWay());
		contentEdit.setText(info.getContent());
	}
	private void closeInfo(){
		if(edit_scroll.getVisibility()==View.VISIBLE){
			edit_scroll.setVisibility(View.GONE);
		}
	}

}
