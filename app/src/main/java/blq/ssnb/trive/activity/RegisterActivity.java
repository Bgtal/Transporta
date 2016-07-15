package blq.ssnb.trive.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import blq.ssnb.trive.NewMainActivity;
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
import blq.ssnb.trive.util.RegularUtil;
import blq.ssnb.trive.util.ServiceUtil;
import blq.ssnb.trive.util.TUtil;
import okhttp3.Call;

public class RegisterActivity extends BaseActivity {

	private Context context;

	private EditText emailEdit;//邮箱

	private RadioButton manRadioBtn;//男

	private Spinner ageSpinner;//年龄
	private Integer[] ageStrings;//年龄选项

	private RadioButton residentRadioBtn;//本地人

	private Spinner birthSpinner;//出生地
	private String[] birthStrings;//出生地选项

	private Spinner driverSpinner;//驾驶证
	private String[] driverStrings;//驾驶证选项

	private Spinner employmentSpinner;//就业
	private String[] employmentStrings;//就业选项

	private Spinner studyingSpinner;//学习状态
	private String[] studyingStrings;//学习状态选项

	private LinearLayout nextPageLayout;

	private Spinner workspaceSpinner;//工作地点
	private String[] workspaceStrings;//工作地点选项

	private EditText occupationEdit;

	private Spinner industrySpinner;//行业
	private String[] industryStrings;//行业选项

	private Spinner studyLevelSpinner;//学历
	private String[] studyLevelStrings;//学历选项

	private Spinner otherActivitySpinner;//其他活动
	private String[] otherActivityStrings;//其他活动选项

	private TextView registerBtn;

	private TextView backLoginBtn;//返回登录按键

	private UserModel registerUser;


	private PreferenceUtil preferenceUtil;

	@Override
	protected void initData() {
		context = this;
		AppManager.addActivity(this);
		preferenceUtil = new PreferenceUtil(context, PlistConstant.FILE_NAME_AUTO_LOGIN);
		registerUser= new UserModel();
		ageStrings = CommonConstant.getAges();
		birthStrings = getResources().getStringArray(R.array.birth_array);
		driverStrings = getResources().getStringArray(R.array.driver_array);
		employmentStrings = getResources().getStringArray(R.array.employment_array);
		studyingStrings = getResources().getStringArray(R.array.studying_array);

		workspaceStrings = getResources().getStringArray(R.array.workspace_array);
		industryStrings = getResources().getStringArray(R.array.industry_array);
		studyLevelStrings = getResources().getStringArray(R.array.studying_level_array);
		otherActivityStrings = getResources().getStringArray(R.array.other_activity_array);

	}

	@Override
	void initView() {
		setContentView(R.layout.activity_register);
		//email
		emailEdit = (EditText) findViewById(R.id.register_email);
		//sex
		LinearLayout radioGroup = (LinearLayout) findViewById(R.id.register_radio_group);
		((TextView)radioGroup.findViewById(R.id.gradio_title)).setText("Gender");
		manRadioBtn = (RadioButton) radioGroup.findViewById(R.id.gradio_btn_fist);
		manRadioBtn.setChecked(true);

		// age
		LinearLayout ageLayout = (LinearLayout) findViewById(R.id.register_age_layout);
		((TextView)ageLayout.findViewById(R.id.register_title)).setText("Age:");
		ageSpinner = (Spinner) ageLayout.findViewById(R.id.register_spinner);

		//location or visitor
		LinearLayout waLayout = (LinearLayout)findViewById(R.id.register_wa);
		((TextView)waLayout.findViewById(R.id.gradio_title)).setText("WA resident or visitor?");
		residentRadioBtn = (RadioButton) waLayout.findViewById(R.id.gradio_btn_fist);
		residentRadioBtn.setChecked(true);

		//birth 
		LinearLayout birthLayout = (LinearLayout) findViewById(R.id.country_of_birth);
		((TextView)birthLayout.findViewById(R.id.register_title)).setText("Country of birth:");
		birthSpinner = (Spinner) birthLayout.findViewById(R.id.register_spinner);

		//driver's licence
		LinearLayout dirverLayout = (LinearLayout) findViewById(R.id.dirver);
		((TextView)dirverLayout.findViewById(R.id.register_title)).setText("Driver's Licence:");
		driverSpinner = (Spinner) dirverLayout.findViewById(R.id.register_spinner);

		//employment
		LinearLayout employmentLayout = (LinearLayout) findViewById(R.id.employment);
		((TextView)employmentLayout.findViewById(R.id.register_title)).setText("Employment:");
		employmentSpinner = (Spinner) employmentLayout.findViewById(R.id.register_spinner);

		//studying
		LinearLayout studyingLayout = (LinearLayout) findViewById(R.id.studying);
		((TextView)studyingLayout.findViewById(R.id.register_title)).setText("Studying:");
		studyingSpinner = (Spinner) studyingLayout.findViewById(R.id.register_spinner);

		nextPageLayout = (LinearLayout) findViewById(R.id.register_second_page);
		nextPageLayout.setVisibility(View.GONE);

		//Place of work
		LinearLayout workspaceLayout = (LinearLayout) findViewById(R.id.place_of_work);
		((TextView)workspaceLayout.findViewById(R.id.register_title)).setText("Place of work:");
		workspaceSpinner = (Spinner) workspaceLayout.findViewById(R.id.register_spinner);

		occupationEdit = (EditText) findViewById(R.id.occupation);

		//industry
		LinearLayout industryLayout = (LinearLayout) findViewById(R.id.industry);
		((TextView)industryLayout.findViewById(R.id.register_title)).setText("Industry:");
		industrySpinner = (Spinner) industryLayout.findViewById(R.id.register_spinner);

		//studyLevel
		LinearLayout studyLevelLayout = (LinearLayout) findViewById(R.id.study_at_which_level);
		((TextView)studyLevelLayout.findViewById(R.id.register_title)).setText("Studying at which level:");
		studyLevelSpinner = (Spinner) studyLevelLayout.findViewById(R.id.register_spinner);

		//other_activites
		LinearLayout otherLayout = (LinearLayout) findViewById(R.id.other_activites);
		((TextView)otherLayout.findViewById(R.id.register_title)).setText("Other activites:");
		otherActivitySpinner = (Spinner) otherLayout.findViewById(R.id.register_spinner);

		//register_btn
		registerBtn = (TextView) findViewById(R.id.register_btn);

		//back login
		backLoginBtn = (TextView) findViewById(R.id.register_login);
	}

	@Override
	void updateView() {

		bindAdapter(ageSpinner,ageStrings);
		bindAdapter(birthSpinner,birthStrings);
		bindAdapter(driverSpinner,driverStrings);
		bindAdapter(employmentSpinner,employmentStrings);
		bindAdapter(studyingSpinner,studyingStrings);

		bindAdapter(workspaceSpinner,workspaceStrings);
		bindAdapter(industrySpinner,industryStrings);
		bindAdapter(studyLevelSpinner,studyLevelStrings);
		bindAdapter(otherActivitySpinner,otherActivityStrings);

	}

	private <T>void bindAdapter(Spinner spinner,T[] arrayStrings){

		ArrayAdapter<T> _Adapter=new ArrayAdapter<T>(this,android.R.layout.simple_spinner_item, arrayStrings);
		spinner.setAdapter(_Adapter);
	}

	@Override
	void bindEvent() {
		backLoginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppManager.finishActivity(RegisterActivity.class);
			}
		});

		bindSpinnerEvent();
		bindRadioEvent();

		registerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				register();
			}
		});
	}

	private void bindSpinnerEvent() {
		ageSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				registerUser.setAge(ageStrings[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		birthSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				registerUser.setBirth_country(birthStrings[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		driverSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				registerUser.setDriver_licence(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		employmentSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				registerUser.setEmployment(position);
				if(isShowSecondPage()){
					nextPageLayout.setVisibility(View.VISIBLE);
				}else{
					nextPageLayout.setVisibility(View.GONE);
					clearNextPage();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		studyingSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				registerUser.setStudying(position);
				if(isShowSecondPage()){
					nextPageLayout.setVisibility(View.VISIBLE);
				}else{
					nextPageLayout.setVisibility(View.GONE);
					clearNextPage();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		workspaceSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				registerUser.setWorkspace(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		industrySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				registerUser.setIndustry(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		studyLevelSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				registerUser.setStudy_level(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		otherActivitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				registerUser.setOther_activity(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void bindRadioEvent() {
		manRadioBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				registerUser.setGender(isChecked?0:1);
			}
		});

		residentRadioBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				registerUser.setGender(isChecked?0:1);
			}
		});
	}

	protected void register() {
		String emailStr = emailEdit.getText().toString().trim();
		if(emailStr.isEmpty()||emailStr.equals("")){
			TUtil.TLong(string.no_email);
			return;
		}

		if(RegularUtil.isEmail(emailStr)){
			registerUser.setEmail(emailStr);
			if(isShowSecondPage()){
				String occString = occupationEdit.getText().toString().trim();
				if(occString.isEmpty()||occString.equals("")){
					TUtil.TLong(string.no_occupation);
					return;
				}
			}else{
				registerUser.setWorkspace(-1);
				registerUser.setOccupation("");
				registerUser.setIndustry(-1);
				registerUser.setStudy_level(-1);
				registerUser.setOther_activity(-1);
			}
			httpRegister();
		}else{
			TUtil.TLong(string.email_address_error);
		}
	}
	/**
	 * 请求注册
	 */
	private void httpRegister(){

		OkHttpUtils
				.post()
				.url(HttpConstant.REGISTER)
				.params(registerUser.getMap())
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						registerFail();
					}

					@Override
					public void onResponse(String response, int id) {
						if(response ==null||response.trim().isEmpty()||response.trim().equals("")){
							registerFail();
							return;
						}
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(response);
							boolean key = jsonObject.getBoolean("key");
							if(key){
								registerSuccess();
							}else{

								TUtil.TLong(jsonObject.getString("msg"));
								preferenceUtil.saveBoolean(PlistConstant.AUTO_LOGIN_ISAUTO, false);
							}
						} catch (JSONException e) {
							registerFail();
						}
					}
				});

	}

	private void registerSuccess() {
		TUtil.TLong(string.register_success);
		preferenceUtil.saveBoolean(PlistConstant.AUTO_LOGIN_ISAUTO, true);
		preferenceUtil.saveString(PlistConstant.AUTO_LOGIN_EMAIL, registerUser.getEmail());
		preferenceUtil.saveLong(PlistConstant.AUTO_LOGIN_TIME, System.currentTimeMillis());

		MyApplication.getInstance().setUserInfo(registerUser);
		if(!ServiceUtil.isServiceRunning(context,ServiceConstant.SERVICE_NAME_RECORD)){
			Intent service = new Intent(context,RecordingService.class);
			startService(service);
		}
		startActivity(new Intent(RegisterActivity.this,NewMainActivity.class));
		AppManager.finishActivity(LoginActivity.class);
		AppManager.finishActivity(RegisterActivity.class);
	}

	private void registerFail(){
		TUtil.TLong(string.register_fail);
		preferenceUtil.saveBoolean(PlistConstant.AUTO_LOGIN_ISAUTO, false);
	}
	private void clearNextPage(){

		workspaceSpinner.setSelection(0);
		industrySpinner.setSelection(0);
		studyLevelSpinner.setSelection(0);
		otherActivitySpinner.setSelection(0);
	}

	protected boolean isShowSecondPage() {
		return registerUser.getEmployment()==0&&registerUser.getStudying()==0;
	}
}