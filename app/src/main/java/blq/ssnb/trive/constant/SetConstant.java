package blq.ssnb.trive.constant;

/**
 * Created by SSNB on 2016/7/16.
 */
public class SetConstant {
    private static SetConstant singleton;
    private int googleMapType;
    private int updateTime;
    private int recordStartTime;
    private int recordStopTime;
    private SetConstant(){
        googleMapType = 2;
        updateTime = 20;
        recordStartTime = 6;
        recordStopTime = 20;
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
    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public int getRecordStartTime() {
        return recordStartTime;
    }

    public void setRecordStartTime(int recordStartTime) {
        this.recordStartTime = recordStartTime;
    }

    public int getRecordStopTime() {
        return recordStopTime;
    }

    public void setRecordStopTime(int recordStopTime) {
        this.recordStopTime = recordStopTime;
    }
}
