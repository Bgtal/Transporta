package blq.ssnb.trive.activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import blq.ssnb.trive.R;
import blq.ssnb.trive.model.MyMarker;
import blq.ssnb.trive.util.MLog;

public class TestView extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener{

    private GoogleMap mMap;
    private Map<String,MyMarker> myMarkerMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        myMarkerMap = new HashMap<>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMarkerDragListener(this);
    }


    double x=0;
    double y=0;
    private Marker chooseMark;
    public void addMark(View v){
        MarkerOptions mark = new MarkerOptions();
        mark.position(new LatLng(x,y));
        mark.title("x,y");
        mark.draggable(true);
        Marker marker = mMap.addMarker(mark);
        MyMarker myMarker = new MyMarker();
        myMarker.setMarker(marker);
        myMarker.setMarkerTag(marker.getId());
        myMarker.setMarkerOptions(mark);
        myMarkerMap.put(marker.getId(),myMarker);
        x+=5%180;
        y+=5%90;
    }
    public void removeMark(View v){
        mMap.clear();
        Map<String,MyMarker> doubleMarkerMap =new HashMap<>();
        myMarkerMap.remove(chooseMark.getId());
        for(MyMarker marker : myMarkerMap.values()){
            Marker mar = mMap.addMarker(marker.getMarkerOptions());
            marker.setMarker(mar);
            doubleMarkerMap.put(mar.getId(),marker);
        }
        myMarkerMap=doubleMarkerMap;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        MLog.e("onMark","id"+marker.getId());
        MLog.e("myOnMark",myMarkerMap.get(marker.getId()).getMarkerTag());
        MLog.e("onMark","latlng:"+marker.getPosition().toString());
        chooseMark = marker;
        return false;
    }
    public void shoInfo(View v){
        MLog.e("shoInfo","choose:"+chooseMark.getPosition().toString());
        MLog.e("shoInfo","my:"+myMarkerMap.get(chooseMark.getId()).getMarker().getPosition().toString());


    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}
