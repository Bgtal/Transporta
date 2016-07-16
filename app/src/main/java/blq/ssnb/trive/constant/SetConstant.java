package blq.ssnb.trive.constant;

/**
 * Created by SSNB on 2016/7/16.
 */
public class SetConstant {
    private static SetConstant singleton;
    private int googleMapType;
    private long updateTime;
    private long recordStartTime;
    private long recordStopTime;
    private SetConstant(){
        googleMapType = 2;
        updateTime = 20*CommonConstant.ONE_HOUR_LONG;
        recordStartTime = 6*CommonConstant.ONE_HOUR_LONG;
        recordStopTime = 20*CommonConstant.ONE_HOUR_LONG;
    }
    public static SetConstant singleton(){
        if(singleton == null){
            singleton = new SetConstant();
        }
        return singleton;
    }

    public int getGoogleMapType() {
        return googleMapType;
    }

    /**
     * 获得可更新和停止记录的时间
     * @return
     */
    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
