/*
    Program: Aplikacja typu klient-serwer realizująca funkcje książki telefonicznej.
    Plik: RunBoth.java
          Klasa uruchamiająca serwer i klient.

    Autor: Władysław Nowak
    Grupa: Poniedziałek, 11:00-12:30 TP
    Data: 8.01.2020
 */

public class RunBoth {
    public static void main(String[] args) {
        new PhoneBookServer("Test");
        new PhoneBookClient("Test client 1", "localhost");
    }
}
