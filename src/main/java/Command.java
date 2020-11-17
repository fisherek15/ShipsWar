public enum Command {
    //todo consider splitting the enum to ServerCommand and ClientCommand
    SET_USERNAME("SET_USERNAME"),
    TEXT("TEXT"),
    SHOW_OPTIONS("SHOW_OPTIONS"),
    GET_USERS("GET_USERS"),
    WRONG_COMMAND("WRONG_COMMAND"),
    GET_USERNAME("GET_USERNAME"),
    SET_USER_MODE("SET_USER_MODE"),
    SET_SHIP("SET_SHIP"),
    REMOVE_SHIP("REMOVE_SHIP"),
    START("START"),
    START_YES("START_YES"),
    START_NO("START_NO"),
    SHOT("SHOT"),
    SHOT_ANSWER("SHOT_ANSWER"),
    EXIT("EXIT");

    private final String command;

    Command(String command){
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }

    public static String getCommandList(){
        return "----------------------------------------------------------\r\n" +
        "Available commands:\r\n" +
        "* SET_USERNAME - allows set a username. It's necessary use it immediately after connecting to the server. Example of use: <<SET_USERNAME>>your_username\r\n" +
        "* TEXT - allows send a message to another user if available. Example of use: <<TEXT:receiver_username>>message contents\r\n" +
        "* GET_USERS - shows list of all available users. Example of use: <<GET_USERS>>\r\n" +
        "* GET_USERNAME - shows name of connected user. Example of use: <<GET_USERNAME>>\r\n" +
        "* SHOW_OPTIONS - shows list of all commands. Example of use: <<SHOW_OPTIONS>>\r\n" +
        "* SET_USER_MODE - sets user into BUSY or READY mode. The user will (not) be visible in list of all users. Example of use: <<SET_USER_MODE>>proper_mode. In place of 'proper_mode, insert: 'BUSY' or 'READY'.\r\n" +
        "* SET_SHIP - sets a ship on the board. Example of use: <<SET_SHIP>>A3-A5\r\n" +
        "* REMOVE_SHIP - removes a ship from the board. Example of use: <<REMOVE_SHIP>>A3-A5\r\n" +
        "* START - send a game proposal. Use the command to send the game proposal after sets all ships. Example of use: <<START:receiver_username>>\r\n" +
        "* START_YES - accepts the game proposal. Use the command to accept the game proposal after sets all ships. Example of use: <<START_YES:receiver_username>>\r\n" +
        "* START_NO - refuse the game proposal. Use the command to refuse the game proposal. Example of use: <<START_NO:receiver_username>>\r\n" +
        "* SHOT - sends shot position to opponent. Example of use: <<SHOT:receiver_username>>B3\r\r" +
        "!!! Be sure to enclose the command in double angle brackets, eg <<COMMAND>>\r\n" +
        "----------------------------------------------------------\r\n";
    }
}

