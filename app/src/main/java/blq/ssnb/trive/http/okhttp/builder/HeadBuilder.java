package blq.ssnb.trive.http.okhttp.builder;


import blq.ssnb.trive.http.okhttp.OkHttpUtils;
import blq.ssnb.trive.http.okhttp.request.OtherRequest;
import blq.ssnb.trive.http.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
