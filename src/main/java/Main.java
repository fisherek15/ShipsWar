public class Main {

    public static void main(String[] args) {

        ServerMulti serverMulti = new ServerMulti(7778, 10);
        serverMulti.run();
    }
}
