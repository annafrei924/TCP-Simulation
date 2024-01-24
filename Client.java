/**
 * @author Nicole Mandel and Anna Freiman
 * Receives packets from server and rearranges them to be in order then prints out final message
 */

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;


public class Client {

    public Client(String address, int port) {
        try {
            //create socket and connect
            Socket socket = new Socket(address, port);
            System.out.println("Connected");

            //create reader and writer
            PrintWriter requestWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            String packet = "";
            String currMessage = "";
            ArrayList<String> packets = new ArrayList<>();
            ArrayList<String> currentPackets = new ArrayList<>();
            String currPackets = "";

            outer:
            while(true) {

                //reads packets from sever
                while (!packet.equals("Complete1")) {
                    packet = responseReader.readLine();
                    if (!packet.equals("Complete1")) {
                        packets.add(packet);
                        currentPackets.add(packet);
                    }
                    if (packet.equals("FINALLY FINISHED!")) {
                        break outer;
                    }
                }

                //sorts & complies into string
                for (String p : currentPackets) {
                    currPackets += p;
                }

                //writes back current packets
                requestWriter.println(currPackets);
                requestWriter.flush();

                //compile and print current message
                Collections.sort(packets);
                for (String p : packets) {
                    currMessage += p.charAt(p.length() - 1);
                }
                System.out.println("Received Message: " + currMessage);

                currPackets = "";
                currMessage = "";
                packet = "";

                currentPackets.clear();
            }

            System.out.println("The above message is the final message. \nDisconnecting");
            requestWriter.close();
            responseReader.close();
            socket.close();

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 1234);
    }
}
