package blq.ssnb.trive.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import blq.ssnb.trive.R;

/**
 * 启动界面，用于显示一些信息
 * @author xucj
 *
 */
public class Explain extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_explain);
	}

	/**
	 * 进入按钮
	 * @param v
	 */
	public void GetInto(View v){
		Intent inte = new Intent(Explain.this,StartActivity.class);
		startActivity(inte);
		this.finish();
	}
	
}
