package edu.ramapo.jallen6.oplcasino;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TurnMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_menu);
    }



    public void saveGame(View view){
       EditText inputField =  findViewById(R.id.saveTextName);

       String input = inputField.getText().toString();
       if(input.length() == 0){
           TextView title = findViewById(R.id.turnMenuTitle);
           title.setText(getString(R.string.turnMenuTitle) + "\n Please enter a file name!");
           inputField.setBackgroundColor(Color.RED);
           return;
       }
       Serializer.setFileName(input, false);



        setResult(RESULT_OK);
        finish();
    }

    public void cancelMenu (View view){
        finish();
    }

    public void quitGame (View view){
        finishAffinity();
        System.exit(0);
    }


}
