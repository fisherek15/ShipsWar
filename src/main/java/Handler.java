import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

public class Handler implements Runnable {
    private final Socket socket;
    private final BufferedReader bufferedReader;
    private final PrintStream printStream;
    private String username;
    private final String mode;
    private final List<Handler> clientHandlers;


    public Handler(Socket socket, List<Handler> clientHandlers) throws IOException {
        this.socket = socket;
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
                    boolean result = sendDataToReceiver(message.getUsername(), message.getMessage());
                    if (!result) {
                        sendDataToSender("Given username not exist! The message was not sent.");
                    }
                } else if (Objects.equals(message.getCommand(), Command.SET_USERNAME.toString())) {
                    setUsername(message.getMessage());
                } else if (Objects.equals(message.getCommand(), Command.SHOW_OPTIONS.toString())) {
                    sendDataToSender(Command.getCommandList());
                } else if (Objects.equals(message.getCommand(), Command.GET_USERS.toString())) {
                    sendDataToSender(showUserList(Mode.READY.toString()));
                } else if (Objects.equals(message.getCommand(), Command.EXIT.toString())) {
                    closeConnection();
                } else {
                    sendDataToSender("Wrong command.");
                }
            }
            sleep(100);
        }
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

    private boolean sendDataToReceiver(String username, String text) {
        Handler handler = findUser(username);
        if (handler != null) {
            handler.sendDataToSender(getUsername() + ": " + text);
            return true;
        }
        return false;
    }

    private void closeConnection() {
        //todo
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
                userList.append("* ").append(handler.getUsername());
            }
        }
        return userList.toString();
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}