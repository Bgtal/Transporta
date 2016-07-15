package blq.ssnb.trive.model;


import blq.ssnb.trive.util.MapUtil;

import com.google.android.gms.maps.model.LatLng;

/**
 * 单个记录点的对象
 * @author SSNB
 *
 */
public class TripPointInfo {

	public enum DrawStyle{
		LINE(0),MARK(1);
		private int mark;
		private DrawStyle(int mark) {
			this.mark = mark;
		}
		public int getTag(){
			return mark;
		}
	}
	private int pid;
	private LatLng address;
	private int stamp;
	private DrawStyle style = DrawStyle.LINE;
	private int reason;
	private int way;
	private String content =" " ;
	private String userID;

	public LatLng getAddress() {
		return address;
	}
	public void setAddress(LatLng address) {
		this.address = address;
	}
	public int getStamp() {
		return stamp;
	}
	public void setStamp(int stamp) {
		this.stamp = stamp;
	}
	public DrawStyle getStyle() {
		return style;
	}
	public void setStyle(DrawStyle style) {
		this.style = style;
	}
	public int getReason() {
		return reason;
	}
	public void setReason(int reason) {
		this.reason = reason;
	}
	public int getWay() {
		return way;
	}
	public void setWay(int way) {
		this.way = way;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public double getLatitude(){
		return address.latitude;
	}
	public double getLongitude(){

		return address.longitude;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(100);
		buffer.append("pid:").append(pid).append("\n");
		buffer.append("address:").append(MapUtil.LatLng2String(address)).append("\n");
		buffer.append("stamp:").append(stamp).append("\n");
		buffer.append("style:").append(style==DrawStyle.LINE?"Line":"Mark").append("\n");
		buffer.append("reason:").append(reason).append("\n");
		buffer.append("way:").append(way).append("\n");
		buffer.append("content:").append(content).append("\n");
		buffer.append("userID:").append(userID).append("\n");
		
		return buffer.toString();
	}

}
