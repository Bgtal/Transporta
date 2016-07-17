package blq.ssnb.trive.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import blq.ssnb.trive.R;
import blq.ssnb.trive.R.string;
import blq.ssnb.trive.app.AppManager;
import blq.ssnb.trive.app.MyApplication;
import blq.ssnb.trive.constant.CommonConstant;
import blq.ssnb.trive.constant.HttpConstant;
import blq.ssnb.trive.constant.PlistConstant;
import blq.ssnb.trive.constant.ServiceConstant;
import blq.ssnb.trive.http.okhttp.OkHttpUtils;
import blq.ssnb.trive.http.okhttp.callback.StringCallback;
import blq.ssnb.trive.model.UserModel;
import blq.ssnb.trive.service.RecordingService;
import blq.ssnb.trive.util.PreferenceUtil;
import blq.ssnb.trive.util.ServiceUtil;
import blq.ssnb.trive.util.TUtil;
import okhttp3.Call;

public class StartActivity extends BaseActivity {
	private PreferenceUtil preference;
	private Context context;

	protected void initData() {
		context = this;
		AppManager.addActivity(this);
		preference = new PreferenceUtil(context,PlistConstant.FILE_NAME_AUTO_LOGIN);
	}

	@Override
	void initView() {
		setContentView(R.layout.activity_start);

	}
	@Override
	void updateView() {
		login();
	}
	@Override
	void bindEvent() {

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void login(){
		String locationEmail = preference.readString(PlistConstant.AUTO_LOGIN_EMAIL);
		//判断本地是否有登录记录
		if(!locationEmail.equals("")){
			long lastLoginTime = preference.readLong(PlistConstant.AUTO_LOGIN_TIME);
			//判断是否超时
			if(System.currentTimeMillis()-lastLoginTime>CommonConstant.ONE_DAY_LONG){
				boolean isauto = preference.readBoolean(PlistConstant.AUTO_LOGIN_ISAUTO);
				//判断是否是自动登录
				if(isauto){
					autoLogin(locationEmail);
				}else{
					loginActivity();
				}
			}else{
				//没有超时就进入主界面
				loginSuccess();
			}
		}else{
			//没有就返回失败，到登录界面
			loginActivity();
		}
	}
	private void autoLogin(String email) {
		OkHttpUtils
				.post()
				.url(HttpConstant.LOGIN)
				.addParams("email",email)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {

					}

					@Override
					public void onResponse(String response, int id) {
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(response);

							if(jsonObject.getBoolean("key")){
								UserModel model = new UserModel();
								model.setEmail(jsonObject.getString("email"));
								model.setGender(jsonObject.getInt("gender"));
								model.setResident(jsonObject.getInt("resident"));
								model.setAge(jsonObject.getInt("age"));
								model.setBirth_country(jsonObject.getString("birth_country"));
								model.setDriver_licence(jsonObject.getInt("driver_licence"));
								model.setEmployment(jsonObject.getInt("employment"));

								model.setStudying(jsonObject.getInt("studying"));
								model.setWorkspace(jsonObject.getInt("workspace"));
								model.setOccupation(jsonObject.getString("occupation"));
								model.setIndustry(jsonObject.getInt("industry"));
								model.setStudy_level(jsonObject.getInt("study_level"));
								model.setOther_activity(jsonObject.getInt("other_activity"));
								MyApplication.getInstance().setUserInfo(model);
								if(!ServiceUtil.isServiceRunning(context,ServiceConstant.SERVICE_NAME_RECORD)){
									Intent service = new Intent(context,RecordingService.class);
									startService(service);
								}
								loginSuccess();
							}else{
								loginActivity();
							}
						} catch (JSONException e) {
							loginActivity();
						}
					}
				});
	}


	private void loginSuccess() {
		preference.saveLong(PlistConstant.AUTO_LOGIN_TIME, System.currentTimeMillis());
		preference.saveBoolean(PlistConstant.AUTO_LOGIN_ISAUTO, true);
		startActivity(new Intent(StartActivity.this,MainActivity.class));
		AppManager.finishActivity(StartActivity.class);
	}
	private void loginActivity(){
		TUtil.TShort(string.auto_login_fail);
		preference.saveBoolean(PlistConstant.AUTO_LOGIN_ISAUTO, false);
		preference.saveLong(PlistConstant.AUTO_LOGIN_TIME, 0);
		preference.saveString(PlistConstant.AUTO_LOGIN_EMAIL,"");
		MyApplication.getInstance().setUserInfo(null);
		AppManager.finishActivity(MainActivity.class);
		startActivity(new Intent(StartActivity.this,LoginActivity.class));
		AppManager.finishActivity(StartActivity.class);
	}
}
