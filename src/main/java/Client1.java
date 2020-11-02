import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client1 {

    public static void main(String[] args) {

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
                    if (dataToSend.equals("exit")) {
                        //todo
                        //wyslij info do serwera o zamknieciu klienta (Handlera)
                        break;
                    } else {
                        dos.writeBytes(dataToSend + "\n");
                    }
                    Thread.sleep(500);
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
