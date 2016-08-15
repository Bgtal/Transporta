package blq.ssnb.trive.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import blq.ssnb.trive.R;
import blq.ssnb.trive.app.MyApplication;
import blq.ssnb.trive.constant.CommonConstant;
import blq.ssnb.trive.db.TripPointDB;
import blq.ssnb.trive.model.TripPointInfo;
import blq.ssnb.trive.model.TripPointInfo.DrawStyle;
import blq.ssnb.trive.util.DateConvertUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class HistoryShowActivity extends FragmentActivity implements
		OnMapReadyCallback, OnMarkerClickListener, OnMapClickListener {
	private GoogleMap mGoogleMap;
	private List<LatLng> lineList = new ArrayList<LatLng>();
	private List<MarkerOptions> MarkList = new ArrayList<MarkerOptions>();
	private List<TripPointInfo> MarkTripInfo = new ArrayList<TripPointInfo>();

	private String[] arrayWay ;
	private String[] arrayReson ;
	private ScrollView showStopInfoScrollView;
	private TextView resonView;
	private TextView wayView;


	private int date = 0;//用来获取上一个activity传过来的日期 20150101

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_show);
		Intent inte = getIntent();
		date = inte.getIntExtra(HistoryActivity.DATE_TAG, 0);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.history_show_map);
		mapFragment.getMapAsync(this);
		initData();
		InitView();
	}

	private void initData() {
		arrayWay = getResources().getStringArray(R.array.way);
		arrayReson = getResources().getStringArray(R.array.reson);
	}

	private void InitView() {
		showStopInfoScrollView = (ScrollView) findViewById(R.id.show_stop_info_scroll);
		resonView = (TextView) findViewById(R.id.show_history_reson);
		wayView = (TextView) findViewById(R.id.show_history_way);
	}

	@Override
	public void onMapReady(GoogleMap Map) {
		mGoogleMap = Map;
		mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		mGoogleMap.setOnMarkerClickListener(this);
		mGoogleMap.setOnMapClickListener(this);
		draw(date);
	}
	private void draw(int date){
		if(date !=0 ){
			int i =0;
			TripPointDB db = new TripPointDB(this);
			List<TripPointInfo> info =db.selectTripPoint(
					date,
					date+CommonConstant.ONE_DAY_INT,
					MyApplication.getInstance().getUserInfo().getEmail());
			for (TripPointInfo tripPointInfo : info) {
				if(tripPointInfo.getStyle()==DrawStyle.LINE){
					lineList.add(tripPointInfo.getAddress());
				}else{
					MarkerOptions mark = new MarkerOptions();
					mark.position(tripPointInfo.getAddress());
					mark.title(""+i);
					mark.snippet(DateConvertUtil.yyyy_MM_dd_HH_mm_ss(tripPointInfo.getStamp()*1000l));
					mark.icon(
							BitmapDescriptorFactory
									.defaultMarker(
											(i%14)*360/14
									)
					);
					i++;
					MarkList.add(mark);
					lineList.add(tripPointInfo.getAddress());
					mGoogleMap.addMarker(mark);
					MarkTripInfo.add(tripPointInfo);
				}
			}
			if(lineList.size()<1){
				return;
			}
			PolylineOptions options = new PolylineOptions().addAll(lineList).color(Color.GREEN).width(5);
			mGoogleMap.addPolyline(options);
			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lineList.get(lineList.size()-1),15));

		}
	}

	private void openInfo(TripPointInfo info){
		if(showStopInfoScrollView.getVisibility()==View.GONE){
			showStopInfoScrollView.setVisibility(View.VISIBLE);
		}
		resonView.setText(arrayReson[info.getReason()]);
		wayView.setText(arrayWay[info.getWay()]);
	}
	private void closeInfo(){
		if(showStopInfoScrollView.getVisibility()==View.VISIBLE){
			showStopInfoScrollView.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onMarkerClick(Marker mark) {

		TripPointInfo info = MarkTripInfo.get(Integer.parseInt(mark.getTitle()));
		openInfo(info);

		return false;
	}

	@Override
	public void onMapClick(LatLng latlng) {
		closeInfo();
	}
}
