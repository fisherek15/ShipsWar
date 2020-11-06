import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerMulti implements Runnable{

    private ServerSocket serverSocket;
    private ExecutorService pool;
    private List<Handler> clientHandlers;

    public static void main(String[] args) {
        ServerMulti serverMulti = new ServerMulti(7778, 10);
        serverMulti.run();
    }

    public ServerMulti(int port, int poolSize){
        try{
            this.serverSocket = new ServerSocket(port);
            this.pool = Executors.newFixedThreadPool(poolSize);
            this.clientHandlers = new ArrayList<>();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            for (; ; ) {
                Socket socket = serverSocket.accept();
                Handler handler = new Handler(socket, clientHandlers);
                pool.execute(handler);
                clientHandlers.add(handler);
            }
        } catch (IOException ex){
            pool.shutdown();
        }
    }

    void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }


}
