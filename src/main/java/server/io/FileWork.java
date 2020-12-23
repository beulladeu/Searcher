package server.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import server.model.Spider;
import server.model.WebSiteItem;

import java.io.*;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;

public class FileWork {

    public static ArrayList<WebSiteItem> fileReader(String fileName, Spider obj) throws IOException {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE)
                .setPrettyPrinting()
                .create();

        try (JsonReader jsonReader = gson.newJsonReader(new FileReader(fileName))) {
            obj = gson.fromJson(jsonReader, Spider.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return obj.getWebsites();
    }

    public static void saveToFile(String fileName, Spider obj) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE)
                .setPrettyPrinting()
                .create();

        try (JsonWriter writer = gson.newJsonWriter(new FileWriter(fileName))) {
            gson.toJson(obj, Spider.class, writer);
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void logFileWriter(String str){
        Date date = new Date();
        File file = new File("utils\\log.txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file,true);
            fr.write(date.toString() + ": " + str + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
