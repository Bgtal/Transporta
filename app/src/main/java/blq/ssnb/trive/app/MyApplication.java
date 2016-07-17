package blq.ssnb.trive.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import blq.ssnb.trive.constant.PlistConstant;
import blq.ssnb.trive.db.UserDB;
import blq.ssnb.trive.http.okhttp.OkHttpUtils;
import blq.ssnb.trive.http.okhttp.https.HttpsUtils;
import blq.ssnb.trive.http.okhttp.log.LoggerInterceptor;
import blq.ssnb.trive.model.UserModel;
import blq.ssnb.trive.util.MLog;
import blq.ssnb.trive.util.PreferenceUtil;
import okhttp3.OkHttpClient;

/**
 * Created by SSNB on 2016/7/15.
 *
 */
public class MyApplication extends MultiDexApplication {
    private static MyApplication mApplication;

    private UserModel userModel;
    private PreferenceUtil autoLoginPf;
    private PreferenceUtil appSettingConfig ;
    private UserDB userDB;
    public MyApplication(){}

    public PreferenceUtil getLoginPf(){
        if(autoLoginPf ==null){
            autoLoginPf = new PreferenceUtil(mApplication, PlistConstant.FILE_NAME_AUTO_LOGIN);
        }
        return autoLoginPf;
    }
    public UserDB getUserDb(){
        if(userDB==null){
            userDB = new UserDB(mApplication);
        }
        return userDB;
    }

    public synchronized static MyApplication getInstance(){
        if(mApplication ==null){
            mApplication =new MyApplication();
        }
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        initOkHttp();
    }

    public synchronized void setUserInfo(UserModel userModel){
        this.userModel = userModel;
        if(userModel!=null){
            getUserDb().addUser(userModel);
        }
    }
    public UserModel getUserInfo(){
        String emailName = getLoginPf().readString(PlistConstant.AUTO_LOGIN_EMAIL);
        if(!emailName.equals("")){
            if(userModel==null){
                userModel=getUserDb().selectUsers(emailName);
            }
        }
        return userModel;
    }

    public PreferenceUtil getAppSettingConfig() {
        if(appSettingConfig == null){
            appSettingConfig = new PreferenceUtil(mApplication, PlistConstant.FILE_NAME_APP_CONFIGURE);
        }
        return appSettingConfig;
    }

    public void setAppSettingConfig(PreferenceUtil appSettingConfig) {
        this.appSettingConfig = appSettingConfig;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initOkHttp() {
        try {
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(new InputStream[]{getAssets().open("elinkpay.crt")}, null, null);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                    .readTimeout(10000L, TimeUnit.MILLISECONDS)
                    .addInterceptor(new LoggerInterceptor("TAG",true))
                    .hostnameVerifier(new HostnameVerifier()
                    {
                        @Override
                        public boolean verify(String hostname, SSLSession session)
                        {
                            return true;
                        }
                    })
                    .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                    .build();
            OkHttpUtils.initClient(okHttpClient);

        } catch (IOException e) {
            MLog.e("BsnApp", "initOKHttp"+e);
        }
    }
}
