/**
 * Created by Yevhen Shcherbynskyi on 05.11.2015.
 */

package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Client {

    JTextArea area;
    JTextField field;
    Socket socket;
    ArrayList<String> selectFiles;
    String serverAddress;
    String serverPort;
    Client(){

        JFrame f = new JFrame("Client v0.1");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(300, 200);
        f.setLayout(new BorderLayout());
        f.setVisible(true);

        area = new JTextArea();
        field = new JTextField(20);
        final JButton selectBut = new JButton("Select");

        final JButton but = new JButton("Send");
        but.setEnabled(false);
        f.add(but, BorderLayout.SOUTH);
        f.add(area);
        f.add(selectBut, BorderLayout.NORTH);

        ///Ip Config

        serverAddress = JOptionPane.showInputDialog(
                f,
                "Ip Server:",
                "Enter IP Address of the Server" ,
                JOptionPane.QUESTION_MESSAGE);
        if(serverAddress.equals("")) serverAddress="127.0.0.1";
        ///--------------------------------------------------
        serverPort = JOptionPane.showInputDialog(
                f,
                "Port Server:",
                "Enter Port of the Server",
                JOptionPane.QUESTION_MESSAGE);
        if(serverPort.equals("")) serverPort="8080";

        but.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                sendFiles2(selectFiles,"");
            }
        });

        selectBut.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();

                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setMultiSelectionEnabled(true);
                selectFiles = new ArrayList<String>();
                area.setText("");
                int returnVal = chooser.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION){
                    area.append("Selected files for transfer:\n" );
                    File[] file = chooser.getSelectedFiles();
                    for (File d : file){
                        selectFiles.add(d+"");
                        area.append(d+"\n");
                    }
                    but.setEnabled(true);
                }
            }
        });
       
    }

    private void sendFiles2(ArrayList<String> list,String path){

        if (socket==null){
            try {
                socket = new Socket(InetAddress.getByName(serverAddress), Integer.parseInt(serverPort));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        try{
            DataOutputStream outD = new DataOutputStream(socket.getOutputStream());

            int countFiles = list.size();
            for(int i = 0; i<countFiles; i++){
                File f = new File(list.get(i));

                if(f.isFile()){
                    outD.writeInt(1);
                    outD.writeUTF(path);
                    outD.writeUTF(f.getName());//מעסûכאול טלÿ פאיכא
                    outD.writeLong(f.length());//מעסûכאול נאחלונ פאיכא

                    FileInputStream in = new FileInputStream(f);
                    byte [] buffer = new byte[64*1024];
                    int count;

                    while((count = in.read(buffer)) != -1){
                        outD.write(buffer, 0, count);
                    }
                    outD.flush();
                    in.close();
                }
                else{
                    outD.writeInt(2);
                    outD.writeUTF(path);
                    outD.writeUTF(f.getName());
                    ArrayList<String> sFiles=new ArrayList<String>();
                    File[] file = f.listFiles();
                    for (File d : file){
                        sFiles.add(d+"");
                    }
                    sendFiles2(sFiles,path+f.getName()+"/");
                }
            }
            if(path.equals("")){
                outD.writeInt(0);
                area.append("Files transferred");
                socket.close();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        

    }

    public static void main(String[] args) {
        new Client();
    }
}