package blq.ssnb.trive.util;

import blq.ssnb.trive.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class DialogUtil {
	
	
	private static Dialog Dialog(Activity context,int message){
		final Dialog dialog = new Dialog(context, R.style.Dialogs);
		dialog.setContentView(R.layout.firset_dialog_view);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();

		int screenW = getScreenWidth(context);
		lp.width = (int) (0.6 * screenW);

		TextView titleTxtv = (TextView) dialog.findViewById(R.id.tvLoad);
		titleTxtv.setText(message);
		return dialog;
	
	}

	public static Dialog upLoadDialog(Activity activity){
		return Dialog(activity,R.string.upload_dialog_hint);
	}

	public static Dialog LoginDialog(Activity context) {
		return Dialog(context, R.string.first_start_dialog_text);
	}
	
	public static Dialog getYanzhenDialog(Activity context) {
		return Dialog(context, R.string.yanzhen_dialog);
	}

	public static Dialog getCustomDialog(Activity context) {
		final Dialog dialog = new Dialog(context, R.style.Dialogs);
		return dialog;
	}
	
	/**
	 * 非activity的context获取自定义对话框
	 * @param context
	 * @return
	 */
	public static Dialog getWinDialog(Context context) {
		final Dialog dialog = new Dialog(context, R.style.Dialogs);
		dialog.getWindow()
				.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		return dialog;
	}

	public static int getScreenWidth(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getScreenHeight(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
}
