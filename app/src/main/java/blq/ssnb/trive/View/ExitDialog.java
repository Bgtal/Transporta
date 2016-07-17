package blq.ssnb.trive.View;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import blq.ssnb.trive.R;

/**
 * 类描述
 *
 * @author SSNB
 *         date 2016/7/17 12:06
 */
public class ExitDialog extends Dialog{
    private static final String TAG = ExitDialog.class.getSimpleName();
    private Context context;
    private TextView textView;
    private String title;

    private TextView exitContentView;
    private String exitContent;

    private TextView leftBtn;
    private TextView rightBtn;
    private MyDialog.ButtonEvent leftBtnEvent;
    private MyDialog.ButtonEvent rightBtnEvent;


    public ExitDialog(Context context,
                    MyDialog.ButtonEvent leftBtnEvent,
                    MyDialog.ButtonEvent rightBtnEvent) {
        super(context);
        this.context = context;
        this.title = "Exit";
        this.leftBtnEvent = leftBtnEvent;
        this.rightBtnEvent = rightBtnEvent;
        exitContent = "Logout will not delete any data.But we will stop recoding travel.You can still login with this account";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        bindEvent();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_exit,null);
        setContentView(view);

        textView = (TextView)view.findViewById(R.id.dialog_title);
        textView.setText(title);

        exitContentView = (TextView) view.findViewById(R.id.dialog_exit_content);
        exitContentView.setText(exitContent);
        leftBtn = (TextView) view.findViewById(R.id.dialog_left_btn);
        rightBtn = (TextView) view.findViewById(R.id.dialog_right_btn);

        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }

    private void bindEvent() {

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(leftBtnEvent!=null){
                    leftBtnEvent.onClick();
                }
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(rightBtnEvent!=null){
                    rightBtnEvent.onClick();
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
    }
}
