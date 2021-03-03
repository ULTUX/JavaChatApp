/*
    Program: Aplikacja typu klient-serwer realizująca funkcje książki telefonicznej.
    Plik: PhoneBookClient.java
          Klient aplikacji książki telefonicznej.

    Autor: Władysław Nowak
    Grupa: Poniedziałek, 11:00-12:30 TP
    Data: 8.01.2020
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PhoneBookClient extends JFrame implements ActionListener, Runnable {
    private static final long serialVersionUID = 1L;

    private static final int PORT = 3517;

    static final Font FONT = new Font("Calibri", Font.PLAIN, 12);

    private String host;
    private String name;
    private Socket socket;
    private ObjectOutputStream oStream;
    private ObjectInputStream iStream;



    private JTextField inputField = new JTextField();
    private JTextArea histField = new JTextArea();
    JLabel inputLabel = new JLabel("Wejście:");
    JLabel histLabel = new JLabel("Historia:");
    JScrollPane scroll = new JScrollPane(histField);
    JPanel panel = new JPanel();
    JLabel infoLabel = new JLabel("", SwingConstants.CENTER);

    public static void main(String[] args) {
        String host = JOptionPane.showInputDialog("Podaj adres serwera");
        String name = JOptionPane.showInputDialog("Podaj nazwe klienta");
        if (host.equals("")) host = "localhost";
        if (!name.equals("")) new PhoneBookClient(name, host);
        else JOptionPane.showMessageDialog(null, "Nie podano nazwy klienta, zamykanie programu.");
    }

    public PhoneBookClient(String title, String host) throws HeadlessException {
        super(title);
        this.host = host;
        this.name = title;
        setResizable(false);
        setSize(330, 410);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        histField.setLineWrap(true);
        histLabel.setFont(FONT);
        histField.setFont(FONT);
        histField.setWrapStyleWord(true);
        inputField.addActionListener(this);
        inputLabel.setFont(FONT);
        inputLabel.setFont(FONT);

        panel.add(inputLabel);
        panel.add(inputField);
        histField.setEditable(false);
        panel.add(histLabel);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        inputField.setPreferredSize(new Dimension(220, 30));
        inputLabel.setPreferredSize(new Dimension(80, 30));
        histLabel.setPreferredSize(new Dimension(80, 300));
        scroll.setPreferredSize(new Dimension(220, 300));
        panel.add(scroll);

        infoLabel.setFont(FONT);
        infoLabel.setPreferredSize(new Dimension(getWidth(), 30));
        panel.add(infoLabel);
        setContentPane(panel);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    socket.close();
                    oStream.close();
                    iStream.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
        new Thread(this, getTitle()).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String mess;
        if (e.getSource().equals(inputField)){
            try {
                mess = inputField.getText();
                inputField.setText("");
                printOutgoingMessage(mess);
                oStream.writeObject(mess);
                if (mess.equals("BYE")) {
                    iStream.close();
                    oStream.close();
                    socket.close();
                    dispose();
                    System.exit(0);
                }
            } catch (IOException | NullPointerException ex) {
                JOptionPane.showMessageDialog(this, "Wystąpił błąd podczas próby wysłania wiadomości.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    @Override
    public void run() {
        try {
            socket = new Socket(host, PORT);
            if (socket.isConnected()){
                infoLabel.setText("Połączono z: "+host+":"+PORT);
                iStream = new ObjectInputStream(socket.getInputStream());
                oStream = new ObjectOutputStream(socket.getOutputStream());
                oStream.writeObject("NAME:"+name);
                while (true){
                    try {
                        String mess = (String) iStream.readObject();
                        if (mess.equals("BYE")){
                            iStream.close();
                            oStream.close();
                            socket.close();
                            dispose();
                            System.exit(0);
                        }
                        printIncomingMessage(mess);

                    } catch (IOException | ClassNotFoundException e) {
                        JOptionPane.showMessageDialog(this, "Wystąpił błąd podczas próby interpretacji otrzymanej wiadomości.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void printIncomingMessage(String mess) {
        histField.append(mess+"\n");
    }

    private void printOutgoingMessage(String mess){
        histField.append(mess+"\n");
    }
}
