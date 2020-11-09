import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerMulti {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(7778);
        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<Handler> clientHandlers = new ArrayList<>();

        Runnable task = () -> {
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
        };

        new Thread(task).start();
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
