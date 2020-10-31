import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client2 {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("192.168.0.2", 7778);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader brk = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Input your username: ");
            dos.writeBytes(brk.readLine() + "\n");
            for(;;){
                if(br.ready()){
                    System.out.println(br.readLine());
                }
                if(brk.ready()){
                    String dataToSend = brk.readLine();
                    if (dataToSend.equals("exit")) {
                        break;
                    } else {
                        dos.writeBytes(dataToSend + "\n");
                    }
                }
            }
            dos.close();
            br.close();
            brk.close();
            socket.close();
        } catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
