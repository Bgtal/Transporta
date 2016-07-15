package blq.ssnb.trive.constant;


public class AlarmConstant {
	
	public enum AlarmObject{
		/**
		 * 用于晚上8点的更新闹铃
		 */
		UPDATE("blq.ssnb.trive.alarm.update",5280),
		/**
		 * 用于启动服务的闹铃
		 */
		STARTSERVICE("blq.ssnb.trive.alarm.starservice",2254),
		/**
		 * 用于启动凌晨的更新的闹铃
		 */
		UPDATECONFIG("blq.ssnb.trive.alarm.updateconfig",2125);
		
		private String action;

		private int requestCode;
		private AlarmObject(String action ,int requestCode){
			this.action = action;
			this.requestCode = requestCode;
		}
		public String getAction() {
			return action;
		}
		public int getRequestCode() {
			return requestCode;
		}
	}

}
