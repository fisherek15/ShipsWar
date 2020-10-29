import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Client1 {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("192.168.0.2", 7777);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader bufferedReaderKeyboard = new BufferedReader(new InputStreamReader(System.in));
            String dataToSend, receivedData;
            while (!(dataToSend = bufferedReaderKeyboard.readLine()).equals("exit")) {
                dataOutputStream.writeBytes(dataToSend + "\n");
                receivedData= bufferedReader.readLine();
                System.out.println(receivedData);
            }
            dataOutputStream.close();
            bufferedReader.close();
            bufferedReaderKeyboard.close();
            socket.close();
        } catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
