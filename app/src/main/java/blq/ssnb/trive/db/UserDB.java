package blq.ssnb.trive.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import blq.ssnb.trive.constant.DbConstant;
import blq.ssnb.trive.model.UserModel;

public class UserDB {
	DbHelper dbHelper;
	public UserDB(Context context) {
		dbHelper = DbHelper.getInstance(context);
	}

	/**
	 * 传入用户对象,然后存入database
	 * @param user 需要写入的user对象
	 */
	public void addUser(UserModel user){

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		String sql = "replace into "
				+DbConstant.TABLE_NAME_USER
				+" ( "+ DbConstant.USER_FIEID_EMAIL+","
				+DbConstant.USER_FIEID_GENDER+","
				+DbConstant.USER_FIEID_RESIDENT+","
				+DbConstant.USER_FIEID_AGE+","
				+DbConstant.USER_FIEID_OTHER +","
				+DbConstant.USER_FIEID_DRIVER_LICENCE +","
				+DbConstant.USER_FIEID_EMPLOYMENT+","
				+DbConstant.USER_FIEID_STUDYING +","
				+DbConstant.USER_FIEID_WORKSPACE +","
				+DbConstant.USER_FIEID_OCCUPATION +","
				+DbConstant.USER_FIEID_INDUSTRY +","
				+DbConstant.USER_FIEID_STUDY_LEVEL+","
				+DbConstant.USER_FIEID_OTHER_ACTIVITY +" ) "
				+" values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

		db.execSQL(sql, new Object[]{
				user.getEmail(),
				user.getGender(),
				user.getResident(),
				user.getAge(),
				user.getBirth_country(),
				user.getDriver_licence(),
				user.getEmployment(),
				user.getStudying(),
				user.getWorkspace(),
				user.getOccupation(),
				user.getIndustry(),
				user.getStudy_level(),
				user.getOther_activity()
		});
		db.close();
	}

	/**
	 * 通过 用户的登陆账号（既手机号码）查询用户的信息
	 * @param userID
	 * @return
	 */
	public UserModel selectUsers(String userID){
		UserModel model = null;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "select * from "+
				DbConstant.TABLE_NAME_USER
				+" where "
				+DbConstant.USER_FIEID_EMAIL
				+" = ?";

		Cursor cursor = db.rawQuery(sql,new String[]{userID});
		if(cursor.moveToFirst()){
			model = new UserModel();

			model.setEmail(cursor.getString(cursor.getColumnIndex(DbConstant.USER_FIEID_EMAIL)));
			model.setGender(cursor.getInt(cursor.getColumnIndex(DbConstant.USER_FIEID_GENDER)));
			model.setResident(cursor.getInt(cursor.getColumnIndex(DbConstant.USER_FIEID_RESIDENT)));
			model.setAge(cursor.getInt(cursor.getColumnIndex(DbConstant.USER_FIEID_AGE)));
			model.setBirth_country(cursor.getString(cursor.getColumnIndex(DbConstant.USER_FIEID_OTHER)));
			model.setDriver_licence(cursor.getInt(cursor.getColumnIndex(DbConstant.USER_FIEID_DRIVER_LICENCE)));
			model.setEmployment(cursor.getInt(cursor.getColumnIndex(DbConstant.USER_FIEID_EMPLOYMENT)));

			model.setStudying(cursor.getInt(cursor.getColumnIndex(DbConstant.USER_FIEID_STUDYING)));
			model.setWorkspace(cursor.getInt(cursor.getColumnIndex(DbConstant.USER_FIEID_WORKSPACE)));
			model.setOccupation(cursor.getString(cursor.getColumnIndex(DbConstant.USER_FIEID_OCCUPATION)));
			model.setIndustry(cursor.getInt(cursor.getColumnIndex(DbConstant.USER_FIEID_INDUSTRY)));
			model.setStudy_level(cursor.getInt(cursor.getColumnIndex(DbConstant.USER_FIEID_STUDY_LEVEL)));
			model.setOther_activity(cursor.getInt(cursor.getColumnIndex(DbConstant.USER_FIEID_OTHER_ACTIVITY)));	
		}
		cursor.close();
		return model;
	}
}
