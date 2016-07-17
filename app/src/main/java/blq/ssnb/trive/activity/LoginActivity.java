package blq.ssnb.trive.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import blq.ssnb.trive.R;
import blq.ssnb.trive.R.string;
import blq.ssnb.trive.app.AppManager;
import blq.ssnb.trive.app.MyApplication;
import blq.ssnb.trive.constant.HttpConstant;
import blq.ssnb.trive.constant.PlistConstant;
import blq.ssnb.trive.constant.ServiceConstant;
import blq.ssnb.trive.http.okhttp.OkHttpUtils;
import blq.ssnb.trive.http.okhttp.callback.StringCallback;
import blq.ssnb.trive.model.UserModel;
import blq.ssnb.trive.service.RecordingService;
import blq.ssnb.trive.util.DialogUtil;
import blq.ssnb.trive.util.MLog;
import blq.ssnb.trive.util.PreferenceUtil;
import blq.ssnb.trive.util.RegularUtil;
import blq.ssnb.trive.util.ServiceUtil;
import blq.ssnb.trive.util.TUtil;
import okhttp3.Call;

public class LoginActivity extends BaseActivity{

	private Context context;
	private EditText emailEdit;
	private TextView loginBtn;
	private TextView registerBtn;
	private TextView instructionsBtn;
	private PreferenceUtil preference;

	@Override
	protected void initData() {
		context=this;
		preference = new PreferenceUtil(context, PlistConstant.FILE_NAME_AUTO_LOGIN);
		AppManager.addActivity(this);
	}

	@Override
	void initView() {
		setContentView(R.layout.activity_login);
		emailEdit = (EditText)findViewById(R.id.login_email);
		loginBtn = (TextView)findViewById(R.id.login_btn);
		registerBtn = (TextView)findViewById(R.id.login_register);
		instructionsBtn = (TextView)findViewById(R.id.login_instructions);
	}

	@Override
	void updateView() {
		registerBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		instructionsBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		emailEdit.setText(preference.readString(PlistConstant.AUTO_LOGIN_EMAIL));
	}

	@Override
	void bindEvent() {
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String email = emailEdit.getText().toString().trim();
				if(email.isEmpty()||email.equals("")){
					TUtil.TLong(string.no_email);
					return;
				}
				if(RegularUtil.isEmail(email)){
					httpLogin(email);
				}else{
					TUtil.TLong(string.email_address_error);
				}
			}
		});
		registerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
			}
		});

		instructionsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this,AboutActivity.class));
			}
		});

	}

	Dialog loginDialog;
	private void httpLogin(String email) {
		loginDialog = DialogUtil.LoginDialog(LoginActivity.this);
		loginDialog.show();
		OkHttpUtils
				.post()
				.url(HttpConstant.LOGIN)
				.addParams("email",email)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						TUtil.TShort(string.request_fail);
					}

					@Override
					public void onResponse(String response, int id) {
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(response);
							if(jsonObject.getBoolean("key")){
								UserModel model = new UserModel();
								MLog.e("TAG","login:"+jsonObject.toString());
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
								preference.saveBoolean(PlistConstant.AUTO_LOGIN_ISAUTO, true);
								preference.saveString(PlistConstant.AUTO_LOGIN_EMAIL, model.getEmail());
								preference.saveLong(PlistConstant.AUTO_LOGIN_TIME, System.currentTimeMillis());
								if(!ServiceUtil.isServiceRunning(context,ServiceConstant.SERVICE_NAME_RECORD)){
									Intent service = new Intent(context,RecordingService.class);
									startService(service);
								}
								loginDialog.dismiss();
								startActivity(new Intent(LoginActivity.this,MainActivity.class));
								AppManager.finishActivity(LoginActivity.class);
							}else{
								loginDialog.dismiss();
								TUtil.TLong(R.string.login_fail);
							}
						} catch (JSONException e) {
							loginDialog.dismiss();
							TUtil.TLong(R.string.login_fail);
						}
					}

				});
	}
}
