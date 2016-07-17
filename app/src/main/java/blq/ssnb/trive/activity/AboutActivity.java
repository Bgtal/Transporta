package blq.ssnb.trive.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import blq.ssnb.trive.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initToolBarView();
    }

    private void initToolBarView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView backBtn = (ImageView) findViewById(R.id.nv_back);
        assert backBtn != null;
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.this.finish();
            }
        });
        TextView titleView = (TextView) findViewById(R.id.nv_title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        assert titleView != null;
        titleView.setText("About Transporta");
    }

}
