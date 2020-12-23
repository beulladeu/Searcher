package server.network;
import server.io.FileWork;
import server.model.Searcher;
import server.model.Spider;
import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;


public class TCPServer{
    public static ArrayList<ClientThread> serverList = new ArrayList<>();
    public static boolean flag = false;

    public void work(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            int count = 1;
            while (!flag) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ClientThread clientThread = new ClientThread(clientSocket, out, in, count);
                FileWork.logFileWriter("Client " + count + " connected to server");
                serverList.add(clientThread);
                clientThread.start();
                count++;
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientThread extends Thread {
    Socket clientSocket;
    PrintWriter out;
    BufferedReader in;
    int id;

    public ClientThread(Socket s, PrintWriter out, BufferedReader in, int id) {
        this.clientSocket = s;
        this.out = out;
        this.in = in;
        this.id = id;
    }

    public void run() {
        try {
            String inputLine = "";//запрос клиента
            String outputLine = "";//аутпутлайн это то что отвечает сервер

            while((inputLine = in.readLine()) != null){
                FileWork.logFileWriter("Client " + getID() + " made a request -- " + inputLine);
                send(parse_message(inputLine));
                FileWork.logFileWriter("Client " + getID() + " received a response from the server");
                if(inputLine.equals("_bye_")){
                    FileWork.logFileWriter("Client " + getID() + " disconnected");
                    send("_bye_");
                    break;
                }

            }
            TCPServer.serverList.remove(this);
            clientSocket.close();
            out.close();
            in.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private String parse_message(String message) throws IOException {
        ArrayList<String> arr = new ArrayList<>();
        message = message.trim();
        String[] words = message.split(" ");
        Collections.addAll(arr, words);
        String[][] search_result = Searcher.searcher(arr);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < search_result.length; i++){
            sb.append(search_result[i][0]);
            sb.append(" ");
            sb.append(search_result[i][1]);
            sb.append("}");
        }
        System.out.println(sb);
        return sb.toString().trim();
    }

    private void send(String message) {
        out.println(message);
    }

    public int getID() {
        return this.id;
    }
}


