/**
 *
 * @author Ian Gortan
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class SocketClientMultithreaded {
    
    static CyclicBarrier barrier; 
    
    public static void main(String[] args) throws InterruptedException {
        String hostName;
        final int MAX_THREADS = 100;
        int port;
        
        if (args.length == 2) {
            hostName = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            hostName= null;
            port = 12031;  // default port in SocketServer
        }
        barrier = new CyclicBarrier(MAX_THREADS);
        
        // TO DO create and start client threads
        System.out.println("Threads starting");
        long start = System.currentTimeMillis();
        List<Thread> clientThreads = new ArrayList<>();
        for(int i = 0; i < MAX_THREADS; i++){
            Thread t = new SocketClientThread(hostName, port, barrier);
            t.start();
            clientThreads.add(t);
        }

        // if barrier had MAX_THREADS + 1 we could have simply done
        // barrier.await() here as well to wait on all other threads and here
        // but since its 1 less, we will just join threads
        for (Thread t : clientThreads) {
            t.join();
        }
        long end = System.currentTimeMillis();
        System.out.println("Time taken = " + (end - start));
    }

      
}
