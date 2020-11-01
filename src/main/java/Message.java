public class Message {

    private String command;
    private String message;
    private String username;

    public Message(String data) {
        decodeMessage(data);
    }

    public String getCommand() {
        return command;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    private void decodeMessage(String data){
        String header;
        if(data != null){
            header = data.substring(data.indexOf("<<"), data.lastIndexOf(">>"));
            if(header.length() > 0) {
                if (!data.endsWith(">>")) {
                    this.message = data.substring(data.lastIndexOf(">>"));
                }
                if (header.contains(":")) {
                    String headerParts[] = header.split(":");
                    this.command = headerParts[0];
                    this.username = headerParts[1];
                } else {
                    this.command = header;
                }
            }
        } else {
            this.command = Command.WRONG_COMMAND.getCommand();
        }

    }
}
