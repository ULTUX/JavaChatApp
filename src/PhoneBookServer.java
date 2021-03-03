/*
    Program: Aplikacja typu klient-serwer realizująca funkcje książki telefonicznej.
    Plik: PhoneBookServer.java
          Serwer aplikacji książki telefonicznej.

    Autor: Władysław Nowak
    Grupa: Poniedziałek, 11:00-12:30 TP
    Data: 8.01.2020
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

public class PhoneBookServer extends JFrame implements Runnable, ActionListener{
    private static final long serialVersionUID = 1L;

    static final Font FONT = new Font("Calibri", Font.PLAIN, 12);
    static final int SERVER_PORT = 3517;
    static PhoneBook phoneBook = new PhoneBook();
    private JLabel clientLabel   = new JLabel("Klienci:");
    private DefaultListModel<ClientHandler> clientMenuData = new DefaultListModel<>();
    JList<ClientHandler> clientMenu = new JList(clientMenuData);
    JButton stopButton = new JButton("Stop");
    JLabel infoLabel = new JLabel("Status servera: ", SwingConstants.CENTER);
    JPanel panel = new JPanel();
    JScrollPane listScroll = new JScrollPane(clientMenu);
    Thread thisThread;
    static boolean exit = false;

    private String name;

    public PhoneBookServer(String title) throws HeadlessException {
        super(title);
        this.name = title;
        setResizable(false);
        setSize(240, 180);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        clientLabel.setFont(FONT);
        clientLabel.setPreferredSize(new Dimension(220, 15));
        clientMenu.setFont(FONT);
        listScroll.setPreferredSize(new Dimension(130, 75));
        infoLabel.setPreferredSize(new Dimension(220, 30));

        listScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        clientMenu.setAutoscrolls(true);

        stopButton.setPreferredSize(new Dimension(80, 50));
        stopButton.addActionListener(this);
        panel.add(clientLabel);
        panel.add(listScroll);
        panel.add(stopButton);
        panel.add(infoLabel);
        setContentPane(panel);
        setVisible(true);
        thisThread = new Thread(this, name);
        thisThread.start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        String name = JOptionPane.showInputDialog("Podaj nazwe servera.");
        if (name != null && !name.equals("")) new PhoneBookServer(name);
        else JOptionPane.showMessageDialog(null, "Nie podano nazwy serwera, program sie wyłączy.");
    }

    public synchronized void addClient(ClientHandler clientHandler) {
        clientMenuData.addElement(clientHandler);
    }


    @Override
    public void run() {

            try {
                ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
                infoLabel.setText("Serwer działa na porcie: "+serverSocket.getLocalPort());
                while (!exit){
                    Socket socket = serverSocket.accept();
                    if (socket != null){
                      new ClientHandler(this, socket);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Nie można było stworzyć gniazdka servera sieciowego.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
    }

    public synchronized void delClient(ClientHandler clientHandler) {
        clientMenuData.removeElement(clientHandler);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(stopButton)){
                Enumeration handlers = clientMenuData.elements();
                while (handlers.hasMoreElements()){
                    try {
                        ((ClientHandler)handlers.nextElement()).exit();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Wystąpił błąd podczas próby zamknięcia wszystkich gniazdek");
                    }
                }
            setVisible(false);
            exit = true;
            dispose();
            System.exit(0);

        }
    }
}
