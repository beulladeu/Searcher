package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import server.io.FileWork;
import server.model.Spider;
import server.network.TCPServer;


public class Main {
    public static void main( String[] args ) throws IOException{
        ArrayList<String> urls = new ArrayList<>();
        String inputFileName = "utils\\urls";
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String line = reader.readLine();
            urls.add(line);
            while ((line = reader.readLine()) != null){
                urls.add(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(urls);

        Spider spider = new Spider();
        for(String url : urls){
            spider.addWebSite(url);
        }
        spider.work();
        System.out.println("Spider finished work");
        System.out.println("Server started work");
        FileWork.logFileWriter("Server started work");
        TCPServer server = new TCPServer();
        server.work(32000);
        FileWork.logFileWriter("Server finished work");
    }
}
