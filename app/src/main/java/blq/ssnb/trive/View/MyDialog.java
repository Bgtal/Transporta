package blq.ssnb.trive.View;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import blq.ssnb.trive.R;
import blq.ssnb.trive.constant.CommonConstant;

/**
 * 下拉复选框的Dialog
 */
public class MyDialog extends Dialog{

    public interface ButtonEvent{
        void onClick();
    }
    public interface ChooseCallBack{
        void choose(int position);
    }

    private String[] arrayReson;
    private String[] arrayWay;
    private Context context;
    private TextView textView;
    private String title;

    private Spinner chooseSpiner;
    private String[] data;
    private ChooseCallBack chooseCallBack;

    private TextView leftBtn;
    private TextView rightBtn;
    private ButtonEvent leftBtnEvent;
    private ButtonEvent rightBtnEvent;
    private int choosePosition;
    private int olderChoosePosition;



    public static MyDialog chooseReasonDialog(Context context ,ChooseCallBack callback){
        return new MyDialog(
                context,
                "Why did you stop here?",
                context.getResources().getStringArray(R.array.reason),
                null,
                null,
                callback
        );
    }
    public static MyDialog chooseWayDialog(Context context ,ChooseCallBack callback){
        return new MyDialog(
                context,
                "How did you get here?",
                context.getResources().getStringArray(R.array.way),
                null,
                null,
                callback);
    }
    public MyDialog(Context context,
                    String title,
                    String[] data,
                    ButtonEvent leftBtnEvent,
                    ButtonEvent rightBtnEvent,ChooseCallBack chooseCallBack) {
        super(context);
        this.context = context;
        this.title = title;
        this.data = data;
        this.leftBtnEvent = leftBtnEvent;
        this.rightBtnEvent = rightBtnEvent;
        this.chooseCallBack = chooseCallBack;
        olderChoosePosition=choosePosition=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        bindEvent();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_spinner,null);
        setContentView(view);

        textView = (TextView)view.findViewById(R.id.dialog_title);
        textView.setText(title);

        chooseSpiner = (Spinner) view.findViewById(R.id.dialog_spinner);
        ArrayAdapter<String> _Adapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,data);
        chooseSpiner.setAdapter(_Adapter);

        leftBtn = (TextView) view.findViewById(R.id.dialog_left_btn);
        rightBtn = (TextView) view.findViewById(R.id.dialog_right_btn);

        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }

    private void bindEvent() {

        chooseSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choosePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(leftBtnEvent!=null){
                    leftBtnEvent.onClick();
                }
                if(chooseCallBack!=null){
                    olderChoosePosition = choosePosition;
                    chooseCallBack.choose(choosePosition);
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

                choosePosition=olderChoosePosition;
            }
        });
    }

    @Override
    public void show() {
        super.show();
        chooseSpiner.setSelection(olderChoosePosition);
    }
    public void setIndex(int position){
        olderChoosePosition = choosePosition=position;
    }

}
