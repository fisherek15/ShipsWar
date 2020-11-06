public class Main {

    public static void main(String[] args) {

        ServerMulti serverMulti = new ServerMulti(7778, 10);
        serverMulti.run();


/*        Message message;
        message = new Message("wiadomosc");
        message = new Message("<<EXIT>>");
        message = new Message("<<get_list>>");
        message = new Message("<<set_username:adrian>>");
        message = new Message("<<text:adrian>>to jest wiaodmosc");
        System.out.println(message.getCommand());
        System.out.println(message.getMessage());
        System.out.println(message.getUsername());*/
    }
}
