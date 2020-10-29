import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args){

        try {
            ServerSocket serverSocket = new ServerSocket(7777);
            Socket socket = serverSocket.accept();
            System.out.println("Connection established");
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader bufferedReaderKeyboard = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                String inputData, outputData;
                while((inputData = bufferedReader.readLine()) != null){
                    System.out.println(inputData);
                    outputData = bufferedReaderKeyboard.readLine();
                    printStream.println(outputData);
                }
                printStream.close();
                bufferedReader.close();
                bufferedReaderKeyboard.close();
                serverSocket.close();
                socket.close();

                System.exit(0);
            }
        } catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
