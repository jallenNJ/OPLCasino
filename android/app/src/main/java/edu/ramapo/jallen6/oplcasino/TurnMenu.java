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


    /**
     * Saves the game with the specified file name
     * @param view The view which was clicked
     */
    public void saveGame(View view){
        //Get the input
       EditText inputField =  findViewById(R.id.saveTextName);

       String input = inputField.getText().toString();

       //If length is zero, not a valid file name, so display error
       if(input.length() == 0){
           TextView title = findViewById(R.id.turnMenuTitle);
           title.setText(getString(R.string.turnMenuTitle) + "\n Please enter a file name!");
           inputField.setBackgroundColor(Color.RED);
           return;
       }

       //Else, set the file name and return to all game to save
       Serializer.setFileName(input, false);

        setResult(RESULT_OK);
        finish();
    }

    /**
     * Close the current activity and allow the screen that called it to resume
     * @param view The button that was clicked
     */
    public void cancelMenu (View view){
        finish();
    }

    /**
     * Close the application after ending the affinity
     * @param view The button that was clicked
     */
    public void quitGame (View view){
        finishAffinity();
        System.exit(0);
    }


}
