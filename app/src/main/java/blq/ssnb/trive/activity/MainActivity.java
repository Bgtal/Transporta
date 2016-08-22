package blq.ssnb.trive.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import blq.ssnb.trive.constant.ServiceConstant;
import blq.ssnb.trive.constant.SetConstant;
import blq.ssnb.trive.db.HistoryListDB;
import blq.ssnb.trive.db.TripPointDB;
import blq.ssnb.trive.db.json.AllTripToJson;
import blq.ssnb.trive.http.okhttp.OkHttpUtils;
import blq.ssnb.trive.http.okhttp.callback.StringCallback;
import blq.ssnb.trive.http.okhttp.utils.L;
import blq.ssnb.trive.model.MyMarker;
import blq.ssnb.trive.model.TripPointInfo;
import blq.ssnb.trive.service.RecordCallBackHandler;
import blq.ssnb.trive.service.RecordManager;
import blq.ssnb.trive.service.RecordingService;
import blq.ssnb.trive.util.DateConvertUtil;
import blq.ssnb.trive.util.DialogUtil;
import blq.ssnb.trive.util.NotificationUtil;
import blq.ssnb.trive.util.ServiceUtil;
import blq.ssnb.trive.util.TUtil;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private Context context;
    private GoogleMap googleMap;

    private boolean isBind = false;
    private ServiceConnection conn;
    private RecordCallBackHandler recordCallBack;

    private List<LatLng> mLines;
    private Map<String,MyMarker> markerHashMap;
    private Marker chooseMarker;

    private TextView uploadBtn;
    private LinearLayout chooseMenu;
    private TextView reasonBtn;
    private TextView wayBtn;
    private TextView deleteBtn;

    private boolean isAutoMove=true;

    private int MarkIndex=0;
    private TripPointDB tdb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.addActivity(this);
        context = this;
        NotificationUtil.cancle(context, NotificationUtil.NotificationType.Update);
        initEvent();
        initData();
        initView();
        bindEvent();
    }

    private void initEvent() {
        recordCallBack = new RecordCallBackHandler() {
            @Override
            public void line(Location location) {
                LatLng line = new LatLng(location.getLatitude(), location.getLongitude());
                mLines.add(line);
                updateMapView(mLines);
            }

            @Override
            public void mark(Location location) {
                drawTravel();
            }
        };
    }


    private void initData() {
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

                RecordManager.getInstance().setActivityRequestCallBack(recordCallBack);
                isBind = true;
            }
        };
        mLines = new ArrayList<>();
        markerHashMap = new HashMap<>();
        tdb = new TripPointDB(context);
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_user_name)).
                setText(MyApplication.getInstance().getUserInfo().getEmail());

        uploadBtn = (TextView) findViewById(R.id.content_main_btn_upload);
        chooseMenu = (LinearLayout) findViewById(R.id.rl_ll_menu_choose);
        reasonBtn = (TextView) findViewById(R.id.rl_ll_tv_reason);
        wayBtn = (TextView) findViewById(R.id.rl_ll_tv_way);
        deleteBtn = (TextView) findViewById(R.id.rl_ll_tv_delete);
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
                uploadDialog = DialogUtil.upLoadDialog(MainActivity.this);
                uploadDialog.show();
                HashMap<String, String> map = new HashMap<>();
                AllTripToJson js = new AllTripToJson(context);
                map.put("TRIPINFO", js.allTripInfoToJSON(MyApplication.getInstance().getUserInfo().getEmail()).toString());
                map.put("PERSID", MyApplication.getInstance().getUserInfo().getEmail());
                map.put("ACTTDATE", DateConvertUtil.DateTodayInt() + "");
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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            //启动历史记录activity
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        } else if (id == R.id.nav_edit) {
            //启动编辑最近三天的activity
            startActivity(new Intent(MainActivity.this,EditActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this,AboutActivity.class));
        } else if (id == R.id.nav_log_out) {
            new ExitDialog(context, new MyDialog.ButtonEvent() {
                @Override
                public void onClick() {
                    MyApplication.getInstance().getLoginPf().saveString(PlistConstant.AUTO_LOGIN_EMAIL,"");
                    MyApplication.getInstance().getLoginPf().saveBoolean(PlistConstant.AUTO_LOGIN_ISAUTO,false);
                    MyApplication.getInstance().getLoginPf().saveLong(PlistConstant.AUTO_LOGIN_TIME,0);
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
            },null)
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(SetConstant.singleton().getGoogleMapType());
        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                isAutoMove = false;
                chooseMenu.setVisibility(View.GONE);
            }
        });
        this.googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                isAutoMove = true;
                return false;
            }
        });
        this.googleMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
            @Override
            public void onInfoWindowClose(Marker marker) {
                chooseMenu.setVisibility(View.GONE);
            }
        });
        Intent serviceIntent = new Intent(this,RecordingService.class);
        bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
        drawTravel();
        //如果可以更新就显示更新按钮
        if(canUpdate()){
            uploadBtn.setVisibility(View.VISIBLE);
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }
        this.googleMap.setMyLocationEnabled(true);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        isAutoMove =false;
        chooseMarker = marker;
        chooseMenu.setVisibility(View.VISIBLE);
        return false;
    }

    /**
     * 判断是否可以更新
     * @return true 可以更新状态，false 不可以更新状态
     */
    private boolean canUpdate() {

        return System.currentTimeMillis()>(DateConvertUtil.DateTodayLong()+SetConstant.singleton().getUpdateTime()* CommonConstant.ONE_HOUR_LONG);
    }



    /**
     * 画图，当第一次进入或者回调添加marker
     */
    private void drawTravel() {
        List<TripPointInfo> tripPointInfos = listTripPointInfo();
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
                markerOptions.icon(
                        BitmapDescriptorFactory
                                .defaultMarker(
                                        (MarkIndex%14)*360/14
                                )
                );
                MarkIndex++;
                markerOptions.snippet(DateConvertUtil.MM_dd_HH_mm(info.getStamp()*1000L));
                Marker mMarker = googleMap.addMarker(markerOptions);
                L.e("Draw:Mid="+mMarker.getId());
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
        if(isAutoMove){
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lines.get(lines.size()-1),16.5f));
        }
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
    private List<TripPointInfo> listTripPointInfo(){
        List<TripPointInfo> infos = new ArrayList<>();

        if(MyApplication.getInstance().getUserInfo()!=null){
            String uid = MyApplication.getInstance().getUserInfo().getEmail();
            if(uid!=null&&!uid.isEmpty()&&!uid.equals("")){
                infos = tdb.selectTodayTripoint(uid);
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
        dialog.setIndex(myMarker.getTripPointInfo().getReason());
        dialog.show();
    }
    private void saveWay(){
        final MyMarker myMarker = markerHashMap.get(chooseMarker.getId());
        if(myMarker==null){
            chooseMarker = null;
            updateMapView(mLines);
            return;
        }
        MyDialog dialog = MyDialog.chooseWayDialog(context, new MyDialog.ChooseCallBack() {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                boolean isGranted = true;
                for (int result :grantResults){
                    if(result != PackageManager.PERMISSION_GRANTED){
                        isGranted = false;
                    }
                }
                if(isGranted){
                    googleMap.setMyLocationEnabled(true);
                }else{
                    TUtil.TLong("WRITE_CONTACTS Denied");
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(googleMap!=null){
            drawTravel();
            Intent serviceIntent = new Intent(context,RecordingService.class);
            if(!isBind){
                bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
            }
            RecordManager.getInstance().setActivityRequestCallBack(recordCallBack);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isBind){
            this.unbindService(conn);
            RecordManager.getInstance().setActivityRequestCallBack(null);
            isBind=false;
        }
    }

    @Override
    protected void onDestroy() {
        if(ServiceUtil.isServiceRunning(this, ServiceConstant.SERVICE_NAME_RECORD)) {
            unbindService(conn);
        }
        googleMap = null;
        markerHashMap = null;
        mLines = null;
        conn = null;
        super.onDestroy();
    }

}
