/**
 *
 * @author Ian Gortan
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

// Sockets of this class are coordinated  by a CyclicBarrier which pauses all threads 
// until the last one completes. At this stage, all threads terminate

public class SocketClientThread extends Thread {
    private long clientID;
    String hostName;
    int port;
    CyclicBarrier synk;
    private int MAX_MESSAGES = 100;
    
    public SocketClientThread(String hostName, int port, CyclicBarrier barrier) {
        this.hostName = hostName;
        this.port = port;
        this.synk = barrier;
        this.clientID = Thread.currentThread().getId();
    }
    
    public void run() {
        try {
            // TO DO insert code to pass 10k messages to the SocketServer
            // send 100 messages per thread
            for (int i = 0; i < MAX_MESSAGES; i++) {
                // server current handles only single request per socket
                // so create new socket every time to send same message multiple times
                Socket s = new Socket(hostName, port);
//                s.setSoTimeout(10000);
//                s.connect(new InetSocketAddress("localhost", port), 1000);
                PrintWriter out =
                        new PrintWriter(s.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(s.getInputStream()));
                out.println(Long.toString(clientID));
                out.flush();
                System.out.println(in.readLine());
                in.close();
                out.close();
                s.close();
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't get I/O for the connection to " +
                hostName + ", clientId: " + clientID + ", details: " + e.getMessage());
            System.exit(1);
        } 
        
        // insert code to wait on the CyclicBarrier
        try {
            this.synk.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
