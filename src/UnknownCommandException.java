/*
    Program: Aplikacja typu klient-serwer realizująca funkcje książki telefonicznej.
    Plik: UnknownCommandException.java
          Wyjątek wywoływany w momencie, gdy komenda podana przez klienta nie jest znana.

    Autor: Władysław Nowak
    Grupa: Poniedziałek, 11:00-12:30 TP
    Data: 8.01.2020
 */

public class UnknownCommandException extends Exception {
    public UnknownCommandException(String message) {
        super(message);
    }
}
