/**
 * @author Nicole Mandel and Anna Freiman
 * Takes user input, divides that message into packets, sends each packet to client
 * Drop with 20% probabilty and continues dropping until client has received all packets
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private Socket socket;
    private static ServerSocket server;
    public Server(int port) {
        try {
            //create server, socket, and connect
            server = new ServerSocket(port);
            System.out.println("Waiting for the client request");
            socket = server.accept();
            System.out.println("Client accepted");

            //create reader and writer
            PrintWriter responseWriter = new PrintWriter(socket.getOutputStream(), true);
            InputStream inputStream = socket.getInputStream();
            BufferedReader requestReader = new BufferedReader(new InputStreamReader(inputStream));


            //tales user message
            Scanner s = new Scanner(System.in);
            System.out.println("Input message: ");
            String message = s.nextLine();
            final int totalPackets = message.length();
            int packetsReceived = 0;
            String clientRequest;

            //divide user message into packets array
            String[] abc = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
                    "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
            ArrayList<String> packetArray = new ArrayList<>();

            //every packet gets two letters which represent the position of the packets & then the character
            for (int i = 0; i < message.length(); i++) {
                packetArray.add(abc[i / 26] + abc[i % 26] + ": " + message.substring(i, i + 1));
            }

            //as long as the client doesn't have the full message, keep going
            outer:
            while(true) {
                Collections.shuffle(packetArray);
                //send every packet in the Array
                for (String packet : packetArray) {
                    Random rand = new Random();
                    if (rand.nextInt(100) > 20) {
                        responseWriter.println(packet);
                        System.out.println("Sending: " + packet);
                    }
                }

                //send that it has completed this round of sending
                responseWriter.println("Complete1");


                //reads user response
                clientRequest = requestReader.readLine();
                String currPacket;

                //finds it in PacketArray & removes it
                if (clientRequest != null) {
                    for (int i = 0; i < clientRequest.length(); i += 5) {
                        currPacket = clientRequest.substring(i, i + 5);
                        packetArray.remove(currPacket);
                        packetsReceived += 1;
                        if(packetsReceived == totalPackets) {
                            break outer;
                        }
                    }
                }
            }

            responseWriter.println("FINALLY FINISHED!");
            System.out.println("Process complete!");

            responseWriter.close();
            requestReader.close();
            socket.close();
            server.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        Server server = new Server(1234);
    }
}