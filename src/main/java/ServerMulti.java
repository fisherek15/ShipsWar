import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMulti implements Runnable{

    private ServerSocket serverSocket;
    private ExecutorService pool;
    private List<Handler> clients;

    public ServerMulti(int port, int poolSize){
        try{
            this.serverSocket = new ServerSocket(port);
            this.pool = Executors.newFixedThreadPool(poolSize);
            this.clients = new ArrayList<>();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            for (; ; ) {
                Socket socket = serverSocket.accept();
                Handler handler = new Handler(socket);
                pool.execute(handler);
                clients.add(handler);
            }
        } catch (IOException ex){
            pool.shutdown();
        }
    }

    private class Handler implements Runnable{
        private Socket socket;
        private BufferedReader bufferedReader;
        private PrintStream printStream;
        private String username;
        private String receiverUsername;
        private String mode;
        private Handler opponentHandler;


        private Handler(Socket socket) throws IOException{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            this.printStream = new PrintStream(socket.getOutputStream());
            this.username = RandomString.getAlphaNumericString(10);
            this.mode = "CONFIGURING";
            this.opponentHandler = null;
        }

        public BufferedReader getBufferedReader() {
            return bufferedReader;
        }

        public PrintStream getPrintStream() {
            return printStream;
        }

        public String getUsername() {
            return username;
        }

        public String getReceiverUsername() {
            return receiverUsername;
        }

        public void setReceiverUsername(String receiverUsername) {
            this.receiverUsername = receiverUsername;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public Handler getOpponentHandler() {
            return opponentHandler;
        }

        public void setOpponentHandler(Handler opponentHandler) {
            this.opponentHandler = opponentHandler;
        }

        @Override
        public void run() {
            String receivedData = new String();
            System.out.println(username + " is connected.");
            String newUsername = receiveData(bufferedReader);
            while(findUser(clients, newUsername) != null){
                sendData("The given name is taken.");
                sendData("Please enter a different username: ");
                newUsername = receiveData(bufferedReader);
            }
            username = newUsername;
            sendData(username + ", you are connected to the server!");
            sendData("Select option: ");
            sendData("1. I want to wait for auto connection with random user.");
            sendData("2. I want to select a user to connect with.");
            sendData("Option: ");
            while((receivedData = receiveData(bufferedReader)) != null){
                if(receivedData.equals("1")){
                    mode = "WAITING";
                    sendData("Waiting for random user...");
                    break;
                } else if(receivedData.equals("2")){
                    sendData("Available users:");
                    String freeUsers = prepareUsersList(clients, "WAITING");
                    sendData(freeUsers);
                    sendData("Please enter a username you want to connect with: ");
                    String receivedUsername = receiveData(bufferedReader);
                    while((opponentHandler = findUser(clients, receivedUsername)) == null){
                        sendData("User " + receivedUsername + " is not connected.");
                        sendData(" Please enter a different username:");
                    }
                    receiverUsername = receivedUsername;
                    mode = "PLAYING";
                    opponentHandler.setMode("PLAYING");
                    opponentHandler.setOpponentHandler(findUser(clients, username));
                    sendData("You have been connected to user " + receiverUsername);
                    opponentHandler.sendData("You have been connected to user " + username);
                    sendData(username + " your turn: ");
                    break;
                } else {
                    sendData("There is no such option. Select the proper option:");
                }
            }
            while((receivedData = receiveData(bufferedReader)) != null){
                opponentHandler.sendData(receivedData);
            }
        }

        private String receiveData(BufferedReader bufferedReader){
            String receivedData;
            try {
                receivedData = bufferedReader.readLine();
                return receivedData;
            }catch (IOException ex){
                ex.printStackTrace();
            }
            return null;
        }

        private void sendData(String data){
            printStream.println(data);
        }

        private void closeConnection(){

        }

        private Handler findUser(List<Handler> usersList, String username){
            for (Handler user : usersList){
                if(user.getUsername().equals(username)){
                    return user;
                }
            }
            return null;
        }

        private void addClientToClientList(){

        }

        private void removeClientFromClientList() {

        }

        private String prepareUsersList(List<Handler> clients, String mode){
            String users = new String();
            for(Handler client : clients){
                if(client.getMode().equals(mode)){
                    users += client.getUsername();
                }
            }
            return users;
        }
    }


}
