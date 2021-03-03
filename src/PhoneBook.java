/*
    Program: Aplikacja typu klient-serwer realizująca funkcje książki telefonicznej.
    Plik: PhoneBook.java
          Implementacja struktury książki telefonicznej.
          Implementacja wymaganych metod.

    Autor: Władysław Nowak
    Grupa: Poniedziałek, 11:00-12:30 TP
    Data: 8.01.2020
 */

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class PhoneBook{
    private static final long serialVersionUID = 1L;

    ConcurrentHashMap<String, String> phoneBook = new ConcurrentHashMap<>();

    String LOAD(String fileName){
        try {
            ObjectInputStream iStream = new ObjectInputStream(new FileInputStream(fileName));
            Object readObject = iStream.readObject();
            if (readObject instanceof ConcurrentHashMap){
                phoneBook = (ConcurrentHashMap<String, String>) readObject;
                return "OK";

            }
        } catch (IOException | ClassNotFoundException e) {
            return "ERROR Wystąpił problem podczas próby wczytania danych z pliku.";
        }
        return "ERROR Wystąpił problem podczas próby wczytania danych z pliku.";
    }
    String SAVE(String fileName){
        try {
            ObjectOutputStream oStream = new ObjectOutputStream(new FileOutputStream(fileName));
            oStream.writeObject(phoneBook);
            return "OK";

        } catch (IOException e) {
            return "ERROR Wystąpił błąd podczas próby zapisania danych do pliku.";
        }

    }
    String GET(String name){
        return "OK "+printContact(name);
    }
    String PUT(String name, String number){
        if (phoneBook.containsKey(name)) return "ERROR Ten kontakt już istnieje! Jeśli chcesz go zamienić użyj komendy REPLACE";
        phoneBook.put(name, number);
        return "OK";
    }
    String REPLACE(String name, String number){
        if (!phoneBook.containsKey(name)) return "ERROR Kontakt o takiej nazwie nie istnieje, nie można go zamienić";
        phoneBook.put(name, number);
        return "OK";
    }
    String DELETE(String name){
        if (!phoneBook.containsKey(name)) return "ERROR Kontakt o tej nazwie nie istnieje";
        phoneBook.remove(name);
        return "OK";
    }
    String LIST(){
        if (phoneBook.size() == 0) return "ERROR Lista kontaktów jest pusta.";
        StringBuilder sb = new StringBuilder();
        sb.append("OK ");
        phoneBook.keySet().forEach(s -> {
            sb.append(s).append(" ");
        });
        return sb.toString();
    }


    private String printContact(String name){
        StringBuilder sb = new StringBuilder();
        if (phoneBook.containsKey(name))sb.append("Dane kontaktu: ").append("\n  Imię: ").append(name).append("\n  Numer telefonu: ").append(phoneBook.get(name)).append("\n");
        else sb.append("ERROR Wprowazdono błędne dane.");
        return sb.toString();
    }

}
