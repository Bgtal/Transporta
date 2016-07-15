package blq.ssnb.trive.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import blq.ssnb.trive.util.MLog;

public abstract class BaseActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MLog.e("Activity", this.getClass().getSimpleName());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		initData();
		initView();
		getData();
		updateView();
		bindEvent();
	}

	protected void initData() {}

	abstract void initView();

	protected void getData() {}

	abstract void updateView();

	abstract void bindEvent();

}	
