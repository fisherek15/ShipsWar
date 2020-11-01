import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

public class Handler implements Runnable{
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintStream printStream;
    private String username;
    private String mode;
    private List<Handler> clientHandlers;


    public Handler(Socket socket, List<Handler> clientHandlers) throws IOException{
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
        this.printStream = new PrintStream(socket.getOutputStream());
        this.username = RandomString.getAlphaNumericString(10);
        this.mode = Mode.READY.getMode();
        this.clientHandlers = clientHandlers;
    }

    public String getUsername() {
        return username;
    }

    public String getMode() {
        return mode;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        String receivedData;
        Message message;
        System.out.println(username + " is connected.");
        sendDataToSender(Command.getCommandList());
        for(;;){
            receivedData = getData();
            if(receivedData != null) {
                message = new Message(receivedData);
                if (!message.getCommand().equals(Command.WRONG_COMMAND.getCommand())) {
                    if (message.getCommand().equals(Command.TEXT.getCommand())) {
                        boolean result = sendDataToReceiver(message.getUsername(), message.getMessage());
                        if (!result) {
                            sendDataToSender("Given username not exist! The message was not sent.");
                        }
                    } else if (message.getCommand().equals(Command.SET_USERNAME.getCommand())) {
                        setUsername(message.getMessage());
                    } else if (message.getCommand().equals(Command.SHOW_OPTIONS.getCommand())) {
                        sendDataToSender(Command.getCommandList());
                    } else if (message.getCommand().equals(Command.GET_USERS.getCommand())) {
                        sendDataToSender(showUserList(Mode.READY.getMode()));
                    }
                } else {
                    sendDataToSender("Wrong command.");
                }
            }
        }
    }

    private String getData(){
        try {
            if(bufferedReader.ready()){
                return bufferedReader.readLine();
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return null;
    }

    private void sendDataToSender(String data){
        printStream.println(data);
    }

    private boolean sendDataToReceiver(String username, String text) {
        Handler handler = findUser(username);
        if (handler != null) {
            handler.sendDataToSender(text);
            return true;
        }
        return false;
    }

    private void closeConnection(){
        //todo
    }

    private Handler findUser(String username){
        for (Handler handler : clientHandlers){
            if(handler.getUsername().equals(username)){
                return handler;
            }
        }
        return null;
    }

    private String showUserList(String mode){
        StringBuilder userList = new StringBuilder();
        userList.append("Available users: \r\n");
        for(Handler handler : clientHandlers){
            if(handler.getMode().equals(mode)){
                userList.append("* ").append(handler.getUsername());
            }
        }
        return userList.toString();
    }
}