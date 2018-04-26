package com.nguyennghi.incomingfilter;

import android.content.Context;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nguyennghi on 3/20/18 4:41 PM
 */
public class JsonHelper {
    public static void writeJson(JSONObject jsonObject, String name, String path)
    {
        try {
            FileWriter writer = new FileWriter("/data/data/com.nguyennghi.incomingfilter/"+path+name+".json");
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String list()
    {

        File folder = new File("/data/data/com.nguyennghi.incomingfilter/");
        File[] listOfFiles = folder.listFiles();

        return (String.valueOf( listOfFiles.length));
    }
}
