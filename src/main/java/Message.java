import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

    private String command; //todo consider changing the property type to Command
    private String message;
    private String username;

    public Message(String data) {
        this.message = "";
        decodeMessage(data);
    }

    public String getCommand() {
        return command.toUpperCase();
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    private void decodeMessage(String data){
        Pattern compiledPattern = Pattern.compile("<<.+>>.*");
        Matcher matcher = compiledPattern.matcher(data);

        if(matcher.matches()){
            String header = data.substring(data.indexOf("<<")+2, data.lastIndexOf(">>"));
                if (!data.endsWith(">>")) {
                    this.message = data.substring(data.lastIndexOf(">>")+2);
                }
                if (header.contains(":")) {
                    String[] headerParts = header.split(":");
                    this.command = headerParts[0];
                    this.username = headerParts[1];
                } else {
                    this.command = header;
                }
        } else {
            this.command = Command.WRONG_COMMAND.toString();
        }

    }
}
