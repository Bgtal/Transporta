package blq.ssnb.trive.service;

import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public abstract class RecordCallBackHandler extends Handler{
	public RecordCallBackHandler(){
	}
	public RecordCallBackHandler(Looper l){
		super(l);
	}
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		
		String style = msg.getData().getString("style");
		Location location = (Location)msg.getData().getParcelable("location");
		if(style.equals("line")){
			line(location);
		}else{
			mark(location);
		}
	}
	public abstract void line(Location location);
	public abstract void mark(Location location);
}
