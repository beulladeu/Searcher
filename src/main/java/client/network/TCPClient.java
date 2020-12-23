package client.network;


import client.controller.Transmitter;
import com.sun.xml.internal.ws.util.StringUtils;

import java.io.*;
import java.net.*;

public class TCPClient {

    public static Socket socket;
    static PrintWriter out;
    static BufferedReader in;


    public TCPClient(int portNumber, String hostName){
        try {
            socket = new Socket(hostName, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader((socket.getInputStream())));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void work() {
         ReceiveThread receiver = new ReceiveThread();
         try{
             receiver.start();
         }catch(Exception ex){
             ex.printStackTrace();
         }
    }

    public static void send(String message){
        if(!message.equals("")) {
            out.println(message);
        }
    }

    public static String[][] parse_message(String message){
        String[] arr = message.split("}");
        String[][] str = new String[arr.length][2];
        for(int i = 0; i < arr.length; i++){
            str[i][0] = arr[i].substring(0, arr[i].indexOf(' '));
            str[i][1] = arr[i].substring(arr[i].indexOf(' ') + 1);
        }
        return str;
    }
}

class ReceiveThread extends Thread{

    public void run() {
        String fromServer = "";
        try{
            while((fromServer = TCPClient.in.readLine()) != null){
                if(fromServer.equals("_bye_")){
                    break;
                }
                String[][] arr = null;
                if(!fromServer.equals("")){
                    arr = TCPClient.parse_message(fromServer);
                    String[][] finalArr = arr;
                }
                String[][] finalArr1 = arr;
                String finalFromServer = fromServer;
                javafx.application.Platform.runLater(() -> {
                    try {
                        if(!finalFromServer.equals("")) Transmitter.showMessage(finalArr1);
                        else {
                            Transmitter.showLabel("Request not found");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
            try {
                TCPClient.out.close();
                TCPClient.in.close();
                TCPClient.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
