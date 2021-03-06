import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Client2 {

    private List<Board> boards;
    private String username;
    DataOutputStream dos;

    public static void main(String[] args) {
        Client2 client = new Client2();
        client.run();
    }

    private void run() {
        Runnable task = () -> {
            try {
                Socket socket = new Socket("192.168.0.2", 7778);
                dos = new DataOutputStream(socket.getOutputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader brk = new BufferedReader(new InputStreamReader(System.in));
                boards = new ArrayList<>();

                for (; ; ) {
                    if (br.ready()) {
                        processReceivedData(br.readLine());
                    }
                    if (brk.ready()) {
                        if(processDataToSend(brk.readLine()) == -1) break;
                    }
                }

                dos.close();
                br.close();
                brk.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(task).start();
    }

    private void processReceivedData(String data){
        try {
            Message message = new Message(data);
            if (Objects.equals(Command.TEXT.toString(), message.getCommand())) {
                System.out.println(message.getUsername() + ": " + message.getMessage());
            } else if (Objects.equals(Command.START.toString(), message.getCommand())) {
                System.out.println(message.getUsername() + " is ready to play.");
                Board board = new Board();
                board.setOpponentUsername(message.getUsername());
                boards.add(board);
            } else if (Objects.equals(Command.NEW_GAME.toString(), message.getCommand())) {
                System.out.println(message.getUsername() + " want to start a new game. Send accept or refuse");
            } else if (Objects.equals(Command.NEW_GAME_YES.toString(), message.getCommand())) {
                System.out.println(message.getUsername() + " accepted your proposal of new game.");
            } else if (Objects.equals(Command.NEW_GAME_NO.toString(), message.getCommand())) {
                System.out.println(message.getUsername() + " refuse your proposal of new game.");
            } else if (Objects.equals(Command.SHOT.toString(), message.getCommand())) {
                Board board = findBoard(message.getUsername());
                if (board != null) {
                    boolean result = board.checkShotAndSetField(new Point(message.getMessage()));
                    if (result) {
                        dos.writeBytes("Hit!");
                    } else {
                        dos.writeBytes("Miss");
                    }
                }
            } else {
                System.out.println(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int processDataToSend(String data){
        Message message = new Message(data);
        try {
            if (Objects.equals(Command.START.toString(), message.getCommand())) {
                dos.writeBytes("<<" + Command.START.toString() + ":" + message.getUsername() + ">>");

            } else if(Objects.equals(Command.SET_USERNAME.toString(), message.getCommand())){
                this.username = message.getMessage();
                dos.writeBytes(data);
            } else if (Objects.equals(Command.NEW_GAME.toString(), message.getCommand())) {
                dos.writeBytes("<<" + Command.NEW_GAME.toString() + ":" + message.getUsername() + ">>");
            } else if (Objects.equals(Command.NEW_GAME_YES.toString(), message.getCommand())) {
                dos.writeBytes("<<" + Command.NEW_GAME_YES.toString() + ":" + message.getUsername() + ">>");
            } else if (Objects.equals(Command.NEW_GAME_NO.toString(), message.getCommand())) {
                dos.writeBytes("<<" + Command.NEW_GAME_NO.toString() + ":" + message.getUsername() + ">>");
            } else if(Objects.equals(Command.SET_USERNAME.toString(), message.getMessage())){
                dos.writeBytes("<<" + Command.SET_USERNAME.toString() + ">>" + message.getMessage());
                this.username = message.getMessage();
            } else if (Objects.equals(Command.EXIT.toString(), message.getCommand())) {
                dos.writeBytes(Command.EXIT.toString());
                return -1;
            } else {
                dos.writeBytes(data + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    private Board findBoard(String opponentUsername){
        for(Board board : boards){
            if(board.getOpponentUsername().equals(opponentUsername)){
                return board;
            }
        }
        return null;
    }
}


