package blq.ssnb.trive.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blq.ssnb.trive.R;
import blq.ssnb.trive.View.ExitDialog;
import blq.ssnb.trive.View.MyDialog;
import blq.ssnb.trive.app.AppManager;
import blq.ssnb.trive.app.MyApplication;
import blq.ssnb.trive.constant.CommonConstant;
import blq.ssnb.trive.constant.HttpConstant;
import blq.ssnb.trive.constant.PlistConstant;
import blq.ssnb.trive.constant.SetConstant;
import blq.ssnb.trive.db.HistoryListDB;
import blq.ssnb.trive.db.TripPointDB;
import blq.ssnb.trive.db.json.AllTripToJson;
import blq.ssnb.trive.http.okhttp.OkHttpUtils;
import blq.ssnb.trive.http.okhttp.callback.StringCallback;
import blq.ssnb.trive.model.MyMarker;
import blq.ssnb.trive.model.TripPointInfo;
import blq.ssnb.trive.service.RecordCallBackHandler;
import blq.ssnb.trive.service.RecordManager;
import blq.ssnb.trive.service.RecordingService;
import blq.ssnb.trive.util.DateConvertUtil;
import blq.ssnb.trive.util.DialogUtil;
import blq.ssnb.trive.util.TUtil;
import okhttp3.Call;

public class EditActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener{

    private Context context;
    private GoogleMap googleMap;

    private List<LatLng> mLines;
    private Map<String,MyMarker> markerHashMap;
    private Marker chooseMarker;


    private TextView uploadBtn;
    private LinearLayout chooseMenu;
    private TextView reasonBtn;
    private TextView wayBtn;
    private TextView deleteBtn;

    private int MarkIndex=0;
    private TripPointDB tdb ;
    private int chooseDay=1;

    private RadioButton day1Radio;
    private RadioButton day2Radio;
    private RadioButton day3Radio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        context = this;
        initToolBarView();
        initData();
        initView();
        bindEvent();
    }
    private void initToolBarView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView backBtn = (ImageView) findViewById(R.id.nv_back);
        assert backBtn != null;
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditActivity.this.finish();
            }
        });
        TextView titleView = (TextView) findViewById(R.id.nv_title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        assert titleView != null;
        titleView.setText("Edit");
    }

    private void initData() {
        mLines = new ArrayList<>();
        markerHashMap = new HashMap<>();
        tdb = new TripPointDB(context);
    }

    private void initView() {
        setContentView(R.layout.activity_edit);
        uploadBtn = (TextView) findViewById(R.id.content_main_btn_upload);
        chooseMenu = (LinearLayout) findViewById(R.id.rl_ll_menu_choose);
        reasonBtn = (TextView) findViewById(R.id.rl_ll_tv_reason);
        wayBtn = (TextView) findViewById(R.id.rl_ll_tv_way);
        deleteBtn = (TextView) findViewById(R.id.rl_ll_tv_delete);
        day1Radio = (RadioButton) findViewById(R.id.radio_day1);
        day1Radio.setChecked(true);
        day2Radio = (RadioButton) findViewById(R.id.radio_day2);
        day3Radio = (RadioButton) findViewById(R.id.radio_day3);


        //这个最后执行，避免由于执行顺序导致的错误
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);

    }

    private Dialog uploadDialog;
    private void bindEvent() {
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDialog = DialogUtil.LoginDialog(EditActivity.this);
                uploadDialog.show();
                HashMap<String, String> map = new HashMap<>();
                AllTripToJson js = new AllTripToJson(context);
                map.put("TRIPINFO", js.allTripInfoToJSON(MyApplication.getInstance().getUserInfo().getEmail()).toString());
                map.put("PERSID", MyApplication.getInstance().getUserInfo().getEmail());
                map.put("ACTTDATE", DateConvertUtil.TimeStamp() + "");
                uploadTravel(map);
            }
        });

        reasonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReason();
            }
        });
        wayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWay();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMarker();
            }
        });

        day1Radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    chooseDay = 1;
                    drawTravel(chooseDay);
                }
            }
        });
        day2Radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    chooseDay = 2;
                    drawTravel(chooseDay);
                }
            }
        });
        day3Radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    chooseDay = 3;
                    drawTravel(chooseDay);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(SetConstant.singleton().getGoogleMapType());
        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                chooseMenu.setVisibility(View.GONE);
            }
        });
        this.googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return false;
            }
        });
        this.googleMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
            @Override
            public void onInfoWindowClose(Marker marker) {
                chooseMenu.setVisibility(View.GONE);
            }
        });
        drawTravel(chooseDay);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        chooseMarker = marker;
        chooseMenu.setVisibility(View.VISIBLE);
        return false;
    }

    /**
     * 画图，当第一次进入或者回调添加marker
     */
    private void drawTravel(int chooseDay) {
        List<TripPointInfo> tripPointInfos = listTripPointInfo(chooseDay);
        googleMap.clear();
        mLines.clear();
        MarkIndex = 0;
        markerHashMap.clear();
        for (TripPointInfo info : tripPointInfos){

            mLines.add(info.getAddress());
            if(info.getStyle()== TripPointInfo.DrawStyle.MARK){
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(info.getAddress());
                markerOptions.title("stop"+MarkIndex);
                MarkIndex++;
                markerOptions.snippet(DateConvertUtil.MM_dd_HH_mm(info.getStamp()*1000L));
                Marker mMarker = googleMap.addMarker(markerOptions);

                MyMarker myMarker = new MyMarker();
                myMarker.setMarkerTag(mMarker.getId());
                myMarker.setTripPointInfo(info);
                myMarker.setMarkerOptions(markerOptions);
                myMarker.setMarker(mMarker);

                markerHashMap.put(mMarker.getId(),myMarker);
            }
        }


        drawLine(mLines);

    }

    /**
     * 划线
     * @param lines 所需划线的点
     */
    private void drawLine(List<LatLng> lines) {
        if(lines==null||lines.size()<1){
            return;
        }
        PolylineOptions polylineOptions = new PolylineOptions().addAll(lines).width(5).color(Color.GREEN);
        googleMap.addPolyline(polylineOptions);
    }

    /**
     * 画 marker
     */
    private void drawMarker() {
        Map<String,MyMarker> markerMap = new HashMap<>();
        for (MyMarker myMarker : markerHashMap.values()){
            Marker mMarker=googleMap.addMarker(myMarker.getMarkerOptions());
            myMarker.setMarkerTag(mMarker.getId());
            markerMap.put(mMarker.getId(),myMarker);
        }
        markerHashMap.clear();
        markerHashMap.putAll(markerMap);
    }


    /**
     * 更新当前界面数据(marker 数据不更新)
     * @param mLines 改变的线条数据
     */
    private void updateMapView(List<LatLng> mLines) {
        googleMap.clear();
        drawLine(mLines);
        drawMarker();
    }

    /**
     * 获得当天的记录点的数据
     * @return 所有当天记录点的数据
     */
    private List<TripPointInfo> listTripPointInfo(int chooseDay){
        List<TripPointInfo> infos = new ArrayList<>();

        if(MyApplication.getInstance().getUserInfo()!=null){
            String uid = MyApplication.getInstance().getUserInfo().getEmail();
            if(uid!=null&&!uid.isEmpty()&&!uid.equals("")){
                int today = DateConvertUtil.DateTodayInt();
                infos = tdb.selectTripPoint(today-chooseDay*CommonConstant.ONE_DAY_INT,today,uid);
            }
        }
        return infos;
    }

    /**
     * ----------------三个选项最终的执行
     */
    private void deleteMarker(){
        chooseMenu.setVisibility(View.GONE);
        MyMarker myMarker = markerHashMap.remove(chooseMarker.getId());
        if(myMarker==null){
            chooseMarker = null;
            updateMapView(mLines);
            return;
        }
        tdb.deleteStopPoint(myMarker.getTripPointInfo().getPid());
        updateMapView(mLines);
    }
    private void saveReason(){
        final MyMarker myMarker = markerHashMap.get(chooseMarker.getId());
        if(myMarker==null){
            chooseMarker = null;
            updateMapView(mLines);
            return;
        }
        MyDialog dialog =MyDialog.chooseReasonDialog(context, new MyDialog.ChooseCallBack() {
            @Override
            public void choose(int position) {
                myMarker.getTripPointInfo().setReason(position);
                tdb.updateReson(myMarker.getTripPointInfo().getPid(),position);
            }
        });
        dialog.setIndex(myMarker.getTripPointInfo().getWay());
        dialog.show();
    }
    private void saveWay(){
        final MyMarker myMarker = markerHashMap.get(chooseMarker.getId());
        if(myMarker==null){
            chooseMarker = null;
            updateMapView(mLines);
            return;
        }
        MyDialog dialog = MyDialog.chooseReasonDialog(context, new MyDialog.ChooseCallBack() {
            @Override
            public void choose(int position) {
                myMarker.getTripPointInfo().setWay(position);
                tdb.updateWay(myMarker.getTripPointInfo().getPid(),position);
            }
        });
        dialog.setIndex(myMarker.getTripPointInfo().getWay());
        dialog.show();
    }

    /**
     * 上传路径数据
     * @param map 带上传的数据格式
     */
    private void uploadTravel(HashMap<String, String> map) {

        OkHttpUtils
                .post()
                .url(HttpConstant.UPDATE)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        uploadDialog.dismiss();
                        TUtil.TLong(R.string.update_fail);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        uploadDialog.dismiss();
                        TUtil.TLong(R.string.update_success);
                        HistoryListDB db = new HistoryListDB(MyApplication.getInstance().getApplicationContext());
                        db.instertHistory(MyApplication.getInstance().getUserInfo().getEmail(), DateConvertUtil.DateTodayInt());
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(googleMap!=null){
            drawTravel(chooseDay);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleMap = null;
        markerHashMap = null;
        mLines = null;
    }

}
