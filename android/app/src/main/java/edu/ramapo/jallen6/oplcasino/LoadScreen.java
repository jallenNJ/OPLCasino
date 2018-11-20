package edu.ramapo.jallen6.oplcasino;


import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;
import java.util.Vector;


public class LoadScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);


        Vector<String> result = new Vector<String>();

        //Get all the files in the download folder
        File folder =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] filesInFolder = folder.listFiles();

        //If none, return
        if(filesInFolder == null){
            return;
        }

        //For every file
        for (File file : filesInFolder) {
            //Make sure it is a file and not a directory
            if (file.isFile()) {
                //Get its file extension
                int extensionLocated = file.getAbsolutePath().lastIndexOf(".");
                if(extensionLocated < 0 || extensionLocated >= file.getAbsolutePath().length()){
                    //If none, skip over it
                    continue;
                }
                //If the file extension is the one for save files, add to result vector
                String extension =  file.getAbsolutePath().substring(extensionLocated);
                if(extension.equals(Serializer.fileExtension)){
                    result.add(new String(file.getName()));
                }
            }
        }

        //For every result, create and append button to the layout
        LinearLayout layout = findViewById(R.id.saveButtonLayout);
        for(String res : result){
            layout.addView(generateButton(res));
        }

    }

    /**
     Generates a button for this activity's layout
     @param displayText The text to display on the button
     @return The button with the text
     */
    private Button generateButton(String displayText){
        //Generate the button and give it an ID
        Button newButton = new Button(this);
        newButton.setId(View.generateViewId());

        //Set the text to what was provided
        newButton.setText(displayText);

        //Set the margins and size
        LinearLayout.LayoutParams lp = new
                LinearLayout.LayoutParams(intAsDP(250), intAsDP(50));
        lp.setMargins(20, 20, 20, 20);
        newButton.setLayoutParams(lp);

        //Ensure the background is the normal color and the crop is correct
        newButton.setBackgroundColor(Color.LTGRAY);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Serializer.setFileName(((Button) view).getText().toString(), true);
                Serializer.readInSaveFile();
                if(Serializer.isFileLoaded()){
                    swapToGameActivity();
                }
            }
        };
        newButton.setOnClickListener(clickListener);
        //Set its background to the round rectangle
        newButton.setBackgroundResource(R.drawable.rounded_rectangle);

        return newButton;

    }

    /**
     Convert the integer (pixels) to the amount it is in dp
     @param target the specified pixels
     @return  The converted int

     Copied from the GameLoop File
     */
    private int intAsDP(int target){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, target,
                getResources().getDisplayMetrics());
    }

    /**
     Swaps the scene to the game loop activity after a file is loaded
     */
    private void swapToGameActivity(){
        //Create the intent and put in all the extras
        // Fill in details with information for the Serializer
        Intent intent = new Intent(this, GameLoop.class);
        intent.putExtra(GameLoop.humanFirstExtra, Serializer.isHumanFirst());
        intent.putExtra(GameLoop.fromSaveGameExtra, true );
        intent.putExtra(GameLoop.humanPlayerStartScore, Serializer.getHumanSaveData().getScore());
        intent.putExtra(GameLoop.compPlayerStartScore, Serializer.getComputerSaveData().getScore());

        //Start the new activity and end the current one
        startActivity(intent);
        finish();
    }

}
