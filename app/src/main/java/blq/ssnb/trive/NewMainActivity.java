package blq.ssnb.trive;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
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
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import blq.ssnb.trive.View.MyDialog;
import blq.ssnb.trive.app.AppManager;
import blq.ssnb.trive.app.MyApplication;
import blq.ssnb.trive.constant.CommonConstant;
import blq.ssnb.trive.constant.SetConstant;
import blq.ssnb.trive.service.RecordingService;
import blq.ssnb.trive.util.DateConvertUtil;
import blq.ssnb.trive.util.MLog;
import blq.ssnb.trive.util.TUtil;

public class NewMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private Context context;
    private GoogleMap googleMap;
    private ServiceConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.addActivity(this);
        context = this;
        initData();
        initView();
    }

    private void initData() {
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_map);
        mapFragment.getMapAsync(this);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_log_out) {
            // TODO: 2016/7/15  new Dialog 询问是否退出
            new MyDialog(context, "退出", getResources().getStringArray(R.array.reson), new MyDialog.ButtonEvent() {
                @Override
                public void onClick() {
                    MLog.e("Buttion","leftClick");
                }
            }, new MyDialog.ButtonEvent() {
                @Override
                public void onClick() {
                    MLog.e("Buttion","rightClick");
                }
            }, new MyDialog.ChooseCallBack() {
                @Override
                public void choose(int position) {
                    MLog.e("Choose","position:"+position);
                }
            }).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(SetConstant.singleton().getGoogleMapType());
        Intent serviceIntent = new Intent(this,RecordingService.class);
        bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
        drawTrive();
        if(canUpdate()){

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

    private boolean canUpdate() {

        return System.currentTimeMillis()>(DateConvertUtil.DateTodayLong()+SetConstant.singleton().getUpdateTime()* CommonConstant.ONE_HOUR_LONG);;
    }

    /**
     * 画图
     */
    private void drawTrive() {

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
}
