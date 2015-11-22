/**
 * Created by Yevhen Shcherbynskyi on 05.11.2015.
 */

package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    private JTextArea area;
    private String ipServ;
    private String p;
    private  String Port;
    private String[] ips;


    Server(){

        JFrame f = new JFrame("Server v0.1");

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(200, 250);
        f.setLayout(new BorderLayout());


        JMenuBar menuBar = new JMenuBar();
// File Menu, F - Mnemonic
        JMenu fileMenu = new JMenu("Option");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
// File->New, N - Mnemonic
        JMenuItem newMenuItem = new JMenuItem("IpList", KeyEvent.VK_N);
        newMenuItem.addActionListener(new MenuActionListener());
        fileMenu.add(newMenuItem);
        f.setJMenuBar(menuBar);
// Server Ip
        ipServ = JOptionPane.showInputDialog(
                f,
                "IP:",
                "Enter IP for Server",
                JOptionPane.QUESTION_MESSAGE);
        if(ipServ.equals("")) ipServ="127.0.0.1";
        ips=ipServ.split(" ");
// Port Server
        Port = JOptionPane.showInputDialog(
                f,
                "Port:",
                "Enter Port for Server:",
                JOptionPane.QUESTION_MESSAGE);
        if(Port.equals("")) Port="8080";
//  Download to (Path)
        p = JOptionPane.showInputDialog(
                f,
                "Path",
                "Save the file",
                JOptionPane.QUESTION_MESSAGE);
/////checkpath
        if (isInteger(p)) {

            //custom title, error icon
            JOptionPane.showMessageDialog(f,
                    "Error at the specified path. Restart the program.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            new Server();
        }


        area = new JTextArea();
        f.add(area);
        f.setAlwaysOnTop(true);
        f.setVisible(true);
        connect2();

    }
/////Check integer or string path
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public void connect2(){

        File myPath = new File(p);
        try {
            ServerSocket ss = new ServerSocket(Integer.parseInt(Port));
            area.append("Wait connect...");

            while(true) {
                Socket soket = ss.accept();
                String clientIp = ((InetSocketAddress) soket.getRemoteSocketAddress()).getAddress().toString();
                myPath.mkdir();
                InputStream in = soket.getInputStream();
                DataInputStream din = new DataInputStream(in);

                boolean isAllowed = false;
                if (ips.length == 1 && ips[0].equals("")) isAllowed = true;
                else for (String ip : ips) {
                    if (clientIp.equals("/" + ip)) {
                        isAllowed = true;
                        break;
                    }
                }

                if (isAllowed) {
                    int type;
                    while ((type = din.readInt()) != 0) {
                        if (type == 1) {
                            String path = myPath + "\\" + din.readUTF();
                            //String path =    p + "\\" + din.readUTF();
                            String fileName = din.readUTF(); //прием имени файла
                            long fileSize = din.readLong(); // получаем размер файла
                            double fileSizeByte = fileSize * 0.001;

                            area.append("File name: " + fileName + "\n");
                            area.append("File size: " + fileSize + " byte\n");

                            byte[] buffer = new byte[64 * 1024];
                            FileOutputStream outF = new FileOutputStream(path + fileName);
                            int count, total = 0;

                            Date startTime = new Date();
                            while ((count = din.read(buffer, 0, (int) Math.min(buffer.length, fileSize - total))) != -1) {
                                total += count;
                                outF.write(buffer, 0, count);

                                if (total == fileSize) {
                                    break;
                                }
                            }
                            outF.flush();
                            outF.close();

                            double sizeTime = (new Date().getTime() - startTime.getTime()) * 0.001;
                            area.append(String.valueOf((new Date().getTime() - startTime.getTime()) / 1000) + " s" + "\n");
                            area.append((double) (fileSizeByte / sizeTime) + " Kb/s" + "\n");
                            area.append("File accepted\n---------------------------------\n");
                        } else {
                            String path = myPath + "\\" + din.readUTF();
                            //String path =    p + "\\" + din.readUTF();
                            String fileName = din.readUTF(); //прием имени файла
                            File file = new File(path + fileName);
                            file.mkdirs();
                        }
                    }
                }
            }
        }
        catch(Exception e){
                    e.printStackTrace();
        }

    }

    public static void main(String[] arg){
        new Server();
    }
}

//////////// c:\