/*
    Program: Aplikacja typu klient-serwer realizująca funkcje książki telefonicznej.
    Plik: Command.java
          Zarządzanie zdarzeniami związanymi z komendami podanymi przez klienta.
          Uruchamianie odpowiednich metod w klasie PhoneBook.

    Autor: Władysław Nowak
    Grupa: Poniedziałek, 11:00-12:30 TP
    Data: 8.01.2020
 */
import java.util.Arrays;

public class Command {
    String fullCommand, command;
    String[] args;

    public Command(String fullCommand) {
        this.fullCommand = fullCommand;
        String[] commandWithArgs = fullCommand.split(" ");
        command = commandWithArgs[0];
        args = Arrays.copyOfRange(commandWithArgs, 1, commandWithArgs.length);
    }

    public String runCommand(PhoneBook phoneBook) throws UnknownCommandException {
        System.out.println(command);
        switch (command){
            case "LOAD":
                checkArgumentLength(1);
                return phoneBook.LOAD(args[0]);
            case "SAVE":
                checkArgumentLength(1);
                return phoneBook.SAVE(args[0]);
            case "GET":
                checkArgumentLength(1);
                return phoneBook.GET(args[0]);
            case "PUT":
                checkArgumentLength(2);
                return phoneBook.PUT(args[0], args[1]);
            case "REPLACE":
                checkArgumentLength(2);
                return phoneBook.REPLACE(args[0], args[1]);
            case "DELETE":
                checkArgumentLength(1);
                return phoneBook.DELETE(args[0]);
            case "LIST":
                checkArgumentLength(0);
                return phoneBook.LIST();
        }
        return "ERROR Taka komenda nie istnieje.";
    }


    private void checkArgumentLength(int len) throws UnknownCommandException {
        if (args.length != len) throw new UnknownCommandException("ERROR Zła liczba argumentów.");
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }
}
