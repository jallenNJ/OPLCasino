package edu.ramapo.jallen6.oplcasino;


import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
                if(extension.equals(".csav")){
                    result.add(new String(file.getName()));
                }
            }
        }




    }


}
