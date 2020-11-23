import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

public class Handler implements Runnable {
    private final BufferedReader bufferedReader;
    private final PrintStream printStream;
    private String username;
    private String mode;
    private final List<Handler> clientHandlers;


    public Handler(Socket socket, List<Handler> clientHandlers) throws IOException {
        this.bufferedReader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
        this.printStream = new PrintStream(socket.getOutputStream());
        this.username = RandomString.getAlphaNumericString(10);
        this.mode = Mode.READY.toString();
        this.clientHandlers = clientHandlers;
    }

    public String getUsername() {
        return username;
    }

    public String getMode() {
        return mode;
    }

    public boolean setMode(String mode) {
        if(Objects.equals(mode.toUpperCase(), Mode.BUSY.toString())){
            this.mode = Mode.BUSY.toString();
            return true;
        } else if (Objects.equals(mode.toUpperCase(), Mode.READY.toString())){
            this.mode = Mode.READY.toString();
            return true;
        } else return false;
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
        for (; ; ) {
            receivedData = getData();
            if (receivedData != null) {
                message = new Message(receivedData);
                if (Objects.equals(message.getCommand(), Command.TEXT.toString())) {
                    sendDataToReceiver(message.getUsername(), message.getMessage());
                } else if (Objects.equals(message.getCommand(), Command.SET_USERNAME.toString())) {
                    setUsername(message.getMessage());
                    sendDataToSender("Your username has been set to " + getUsername());
                } else if (Objects.equals(message.getCommand(), Command.SHOW_OPTIONS.toString())) {
                    sendDataToSender(Command.getCommandList());
                } else if (Objects.equals(message.getCommand(), Command.GET_USERS.toString())) {
                    sendDataToSender(showUserList(Mode.READY.toString()));
                } else if (Objects.equals(message.getCommand(), Command.EXIT.toString())) {
                    closeConnection();
                    sendDataToSender("This connection has been closed.");
                    break;
                } else if (Objects.equals(message.getCommand(), Command.GET_USERNAME.toString())) {
                    sendDataToSender(getUsername());
                } else if (Objects.equals(message.getCommand(), Command.SET_USER_MODE.toString())) {
                    if (setMode(message.getMessage())) {
                        sendDataToSender("Your user mode has been set to " + getMode());
                    } else {
                        sendDataToSender("Wrong mode. You can set " + Mode.BUSY.toString() + " or " + Mode.READY.toString() + " only.");
                    }
                } else if(Objects.equals(message.getCommand(), Command.START.toString())) {
                    sendGameDataToReceiver(Command.START, message.getUsername(), "");
                } else if(Objects.equals(message.getCommand(), Command.NEW_GAME_YES.toString())) {
                    sendGameDataToReceiver(Command.NEW_GAME_YES, message.getUsername(), "");
                } else if(Objects.equals(message.getCommand(), Command.NEW_GAME_NO.toString())) {
                    sendGameDataToReceiver(Command.NEW_GAME_NO, message.getUsername(), "");
                } else if(Objects.equals(message.getCommand(), Command.SHOT.toString())) {
                    sendDataToReceiver(message.getUsername(), "<<" + Command.SHOT.toString() + ":" + message.getMessage() + ">>");
                } else {
                    sendDataToSender("Wrong command.");
                }
            }
        }
    }

    private String getOpponentMode(String username){
        Handler handler = findUser(username);
        if(handler != null){
            return handler.getMode();
        }
        return null;
    }

    private String getData() {
        try {
            if (bufferedReader.ready()) {
                return bufferedReader.readLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void sendDataToSender(String data) {
        printStream.println(data);
    }

    private void sendDataToReceiver(String username, String text) {
        Handler handler = findUser(username);
        if (handler != null) {
            handler.sendDataToSender("<<" + Command.TEXT.toString() + ":" + getUsername() + ">>" + text);
        }
        sendDataToSender("Given username not exist! The message was not sent.");
    }

    private void sendGameDataToReceiver(Command command, String username, String text) {
        Handler handler = findUser(username);
        if (handler != null) {
            if(command != null) {
                handler.sendDataToSender("<<" + command.toString() + ":" + getUsername() + ">>" + text);
            }
        }
        sendDataToSender("Given username not exist! The message was not sent.");
    }

    private void closeConnection() {
        clientHandlers.remove(findUser(getUsername()));
    }

    private Handler findUser(String username) {
        for (Handler handler : clientHandlers) {
            if (handler.getUsername().equals(username)) {
                return handler;
            }
        }
        return null;
    }

    private String showUserList(String mode) {
        StringBuilder userList = new StringBuilder();
        userList.append("Available users: \r\n");
        for (Handler handler : clientHandlers) {
            if (handler.getMode().equals(mode)) {
                userList.append("* ").append(handler.getUsername()).append("\r\n");
            }
        }
        return userList.toString();
    }
}