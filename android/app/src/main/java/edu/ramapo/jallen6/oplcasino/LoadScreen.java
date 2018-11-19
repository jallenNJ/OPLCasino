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

        //Enviroment.getExternalStroagePublicDirectory(Enviroment.DIRECTORY_DOWNLOAD

        Vector<String> result = new Vector<String>();

        File folder =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] filesInFolder = folder.listFiles();
        if(filesInFolder == null){
            return;
        }
        for (File file : filesInFolder) { //For each of the entries do:
            if (file.isFile()) { //check that it's not a dir
                //if(file.getAbsolutePath() )
                int extensionLocated = file.getAbsolutePath().lastIndexOf(".");
                if(extensionLocated < 0 || extensionLocated >= file.getAbsolutePath().length()){
                    continue;
                }
                String extension =  file.getAbsolutePath().substring(extensionLocated);
                if(extension.equals(Serializer.fileExtension)){
                    result.add(new String(file.getName()));
                }
            }
        }

        LinearLayout layout = findViewById(R.id.saveButtonLayout);
        for(String res : result){
            layout.addView(generateButton(res));
        }

    }

    private Button generateButton(String displayText){
        //Generate the button and give it an ID
        Button newButton = new Button(this);
        newButton.setId(View.generateViewId());

        //Give it a default image
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
        newButton.setBackgroundResource(R.drawable.rounded_rectangle);

        return newButton;

    }


    //Copied from the gameLoop file
    private int intAsDP(int target){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, target,
                getResources().getDisplayMetrics());
    }

    private void swapToGameActivity(){
        Intent intent = new Intent(this, GameLoop.class);
        intent.putExtra(GameLoop.humanFirstExtra, Serializer.isHumanFirst());
        intent.putExtra(GameLoop.fromSaveGameExtra, true );
        intent.putExtra(GameLoop.humanPlayerStartScore, Serializer.getHumanSaveData().getScore());
        intent.putExtra(GameLoop.compPlayerStartScore, Serializer.getComputerSaveData().getScore());
        startActivity(intent);
        finish();
    }

}
