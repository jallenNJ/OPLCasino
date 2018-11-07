package edu.ramapo.jallen6.oplcasino;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActionLogPopup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_log_popup);


        LinearLayout linear = findViewById(R.id.logPopUpLinear);
        for(int i =ActionLog.getLogSize()-1; i >= 0; i--){
            TextView text = new TextView(this);
            text.setText(ActionLog.getLogEntry(i));
            text.setTextSize(20);
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            text.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            text.setBackgroundColor(Color.LTGRAY);
            linear.addView(text);
        }

    }
}
