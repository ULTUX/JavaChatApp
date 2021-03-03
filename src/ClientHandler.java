/*
    Program: Aplikacja typu klient-serwer realizująca funkcje książki telefonicznej.
    Plik: ClientHandler.java
          Klasa zarządzająca połączeniem z jednym z klientów.

    Autor: Władysław Nowak
    Grupa: Poniedziałek, 11:00-12:30 TP
    Data: 8.01.2020
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;
    private String name;
    private PhoneBookServer phoneBookServer;
    private ObjectOutputStream oStream;
    private ObjectInputStream iStream;


    ClientHandler(PhoneBookServer phoneBookServer, Socket socket) {
        this.phoneBookServer = phoneBookServer;
        this.socket = socket;
        new Thread(this).start();  // Utworzenie dodatkowego watka
        // do obslugi komunikacji sieciowej
    }
    public void exit() throws Exception{
        oStream.writeObject("BYE");
        iStream.close();
        oStream.close();
        socket.close();
    }

    public String toString(){ return name; }

    public void sendMessage(String message){
        try {
            oStream.writeObject(message);
            if (message.equals("exit")){
                phoneBookServer.delClient(this);
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run(){
        String message;
        try(
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                )
        {
            iStream = inputStream;
            oStream = outputStream;
            String handShake = ((String)iStream.readObject());
            if (!handShake.startsWith("NAME:")) throw new Exception("Unknown client connected.");
            System.out.println(handShake);
            name = handShake.split("NAME:")[1];
            phoneBookServer.addClient(this);
            while(true){
                message = (String)iStream.readObject();
                if (message.equals("BYE")){
                    phoneBookServer.delClient(this);
                    break;
                }
                else {
                    Command command = new Command(message);
                    String response;
                    try {
                        response = command.runCommand(PhoneBookServer.phoneBook);
                    } catch (UnknownCommandException e) {
                        response = "Komenda posiada złą składnię";
                    }
                    sendMessage(response);

                }
            }
            socket.close();
            socket = null;
        } catch(Exception e) {
            phoneBookServer.delClient(this);
        }
    }

    public String getName() {
        return name;
    }
}
