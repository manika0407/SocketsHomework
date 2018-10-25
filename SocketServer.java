/**
 *
 * @author Ian Gortan
 */


import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;


public class SocketServer {
  private static int TPOOL_SIZE = 30;
  private static boolean RUN_IN_EXECUTOR = true;

  public static void main(String[] args) throws Exception {
    ServerSocket m_ServerSocket = new ServerSocket(12031, 100);
    ActiveCount threadCount = new ActiveCount();
    System.out.println("Server started .....");
    Executor executor = Executors.newFixedThreadPool(TPOOL_SIZE);
    while (true) {
      Socket clientSocket = m_ServerSocket.accept();
      SocketHandlerThread socketHandlerThread = new SocketHandlerThread (clientSocket, threadCount);
      if (RUN_IN_EXECUTOR) {
        executor.execute(socketHandlerThread);
      } else {
        socketHandlerThread.start();
      }
    }
  }
}

