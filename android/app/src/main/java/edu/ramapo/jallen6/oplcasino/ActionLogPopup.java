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


        //Get the linear layout
        LinearLayout linear = findViewById(R.id.logPopUpLinear);

        //For every element in the log, starting at the end
        for(int i =ActionLog.getLogSize()-1; i >= 0; i--){
            //Make a new text view, and set the text to the value
            TextView text = new TextView(this);
            text.setText(ActionLog.getLogEntry(i));

            //Format the text to be centered and size 20 to meet style desire
            text.setTextSize(20);
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            //Set the bounds of the textview to take up its entire allocated space, and to flow
            // onto next line if needed
            text.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            //Set the background and add to the layout
            text.setBackgroundColor(Color.LTGRAY);
            linear.addView(text);
        }

    }
}
