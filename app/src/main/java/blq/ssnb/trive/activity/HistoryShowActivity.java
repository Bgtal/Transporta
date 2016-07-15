package blq.ssnb.trive.activity;

import java.util.ArrayList;
import java.util.List;

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

import blq.ssnb.trive.R;
import blq.ssnb.trive.app.MyApplication;
import blq.ssnb.trive.constant.CommonConstant;
import blq.ssnb.trive.db.TripPointDB;
import blq.ssnb.trive.model.TripPointInfo;
import blq.ssnb.trive.model.TripPointInfo.DrawStyle;
import blq.ssnb.trive.util.DateConvertUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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


	private ScrollView showStopInfoScrollView;
	private TextView resonView;
	private TextView wayView;
	private TextView contentView;


	private int date = 0;//用来获取上一个activity传过来的日期 20150101

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_show);
		Intent inte = getIntent();
		date = inte.getIntExtra(HistoryActivity.DATE_TAG, 0);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.history_show_map);
		mapFragment.getMapAsync(this);
		InitView();
	}

	private void InitView() {
		showStopInfoScrollView = (ScrollView) findViewById(R.id.show_stop_info_scroll);
		resonView = (TextView) findViewById(R.id.show_history_reson);
		wayView = (TextView) findViewById(R.id.show_history_way);
		contentView = (TextView) findViewById(R.id.show_history_content);
	}

	@Override
	public void onMapReady(GoogleMap Map) {
		mGoogleMap = Map;
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		mGoogleMap.setMyLocationEnabled(false);
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
					i++;
					mark.snippet(DateConvertUtil.yyyy_MM_dd_HH_mm_ss(tripPointInfo.getStamp()*1000l));
					
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
		resonView.setText(CommonConstant.RESON[info.getReason()]);
		wayView.setText(CommonConstant.WAY[info.getWay()]);
		contentView.setText(info.getContent());
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
