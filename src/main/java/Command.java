public enum Command {

    SET_USERNAME("SET_USERNAME"),
    TEXT("TEXT"),
    SHOW_OPTIONS("SHOW_OPTIONS"),
    GET_USERS("GET_USERS"),
    WRONG_COMMAND("WRONG_COMMAND"),
    EXIT("EXIT");

    private String command;

    Command(String command){
        this.command = command;
    }

    public String getCommand(){
        return command;
    }

    public static String getCommandList(){
        StringBuilder result = new StringBuilder();
        result.append("----------------------------------------------------------").append("\r\n");
        result.append("Available commands:\r\n");
        result.append("* SET_USERNAME - allows set a username. It's necessary use it immediately after connecting to the server. Example of use: <<SET_USERNAME>>your_username").append("\r\n");
        result.append("* TEXT - allows send a message to another user if available. Example of use: <<TEXT:receiver_username>>message contents").append("\r\n");
        result.append("* GET_USERS - shows list of all available users. Example of use: <<GET_USERS>>").append("\r\n");
        result.append("* SHOW_OPTIONS - shows list of all commands. Example of use: <<SHOW_OPTIONS>>").append("\r\n");
        result.append("!!! Be sure to enclose the command in double angle brackets, eg <<COMMAND>>").append("\r\n");
        result.append("----------------------------------------------------------").append("\r\n");
        return result.toString();
    }

}
