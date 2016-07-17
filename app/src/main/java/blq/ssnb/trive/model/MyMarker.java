package blq.ssnb.trive.model;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by SSNB on 2016/7/16.
 */
public class MyMarker {
    private Marker marker;
    private String markerTag;
    private MarkerOptions markerOptions;
    private TripPointInfo tripPointInfo;

    public Marker getMarker() {
        marker.getId();
        marker.getAlpha();
        marker.getPosition();
        marker.getRotation();
        marker.getSnippet();

        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getMarkerTag() {
        return markerTag;
    }

    public void setMarkerTag(String markerTag) {
        this.markerTag = markerTag;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public void setMarkerOptions(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
    }

    public TripPointInfo getTripPointInfo() {
        return tripPointInfo;
    }

    public void setTripPointInfo(TripPointInfo tripPointInfo) {
        this.tripPointInfo = tripPointInfo;
    }
}
