import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;

public class Client implements Runnable{

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    @Override
    public void run() {

        try {
            Socket socket = new Socket("192.168.0.2", 7778);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader brk = new BufferedReader(new InputStreamReader(System.in));

            for(;;){
                if(br.ready()){
                    System.out.println(br.readLine());
                }
                if(brk.ready()){
                    String dataToSend = brk.readLine();
                    if (Objects.equals(dataToSend.toUpperCase(), Command.EXIT.toString())) {
                        dos.writeBytes(Command.EXIT.toString());
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
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
