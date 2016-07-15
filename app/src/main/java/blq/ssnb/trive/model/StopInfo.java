package blq.ssnb.trive.model;

import com.google.android.gms.maps.model.LatLng;

public class StopInfo {
	private long stoptiem ;//停止的时间
	private int stopmode;//用什么方式去的
	private String stopcontent;//去用什么车，几个人，私有车还是共有车等信息
	private LatLng stoppoint;//停止的点
	private int destpurp ;//去目的地的原因

	private String stoppointString;
	private int stoptiemint;
	public StopInfo() {
		// TODO Auto-generated constructor stub
	}
	public StopInfo(long stoptiem,int stopmode,String stopcontent,
			LatLng stoppoint,int destpurp,int stoptiemint ) {
		this.stoptiem=stoptiem;
		this.stopmode=stopmode;
		this.stopcontent=stopcontent;
		this.stoppoint=stoppoint;
		this.destpurp=destpurp;
		this.stoptiemint=stoptiemint;
	}
	public StopInfo(int stopmode,String stopcontent,String stoppointString
			,int destpurp,int stoptiemint ) {
		this.stopmode=stopmode;
		this.stopcontent=stopcontent;
		this.stoppointString=stoppointString;
		this.destpurp=destpurp;
		this.stoptiemint=stoptiemint;
	}
	public StopInfo(long stoptiem,LatLng stoppoint ) {
		this.stoptiem=stoptiem;
		this.stoppoint=stoppoint;
	}

	public long getStoptiem() {
		return stoptiem;
	}
	public void setStoptiem(long stoptiem) {
		this.stoptiem = stoptiem;
	}
	public int getStopmode() {
		return stopmode;
	}
	public void setStopmode(int stopmode) {
		this.stopmode = stopmode;
	}
	public String getStopcontent() {
		return stopcontent;
	}
	public void setStopcontent(String stopcontent) {
		this.stopcontent = stopcontent;
	}
	public String getstoppointString() {
		return stoppointString;
	}
	public void setstoppointString(String stoppointString) {
		this.stoppointString = stoppointString;
	}
	public LatLng getStoppoint() {
		return stoppoint;
	}
	public void setStoppoint(LatLng stoppoint) {
		this.stoppoint = stoppoint;
	}
	public int getDestpurp() {
		return destpurp;
	}
	public void setDestpurp(int destpurp) {
		this.destpurp = destpurp;
	}
	public int getStoptimeInt() {
		return stoptiemint;
	}
	public void setStoptimeInt(int stoptiemint) {
		this.stoptiemint = stoptiemint;
	}
	@Override
	public String toString() {
		String a = "{'stoptiem':"+stoptiem+",'stopmode':"+stopmode+",'stopcontent':'"+stopcontent+"','destpurp':"+destpurp+"}";
		return a;
	}
	
	public String tosString2(){
		//{'STOPMODE':stopmode,'STOPCONTENT':stopcontent,'STOPPOINT':stoppointString,'DESTPURP':destpurp}
		String a ="{'STOPMODE':"+stopmode+",'STOPCONTENT':"+stopcontent+",'STOPPOINT':"+stoppointString+",'DESTPURP':"+destpurp+"}";
		return a; 
	}

}
