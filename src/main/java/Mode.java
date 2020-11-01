public enum Mode {

    BUSY("BUSY"),
    READY("READY");

    private String mode;

    Mode(String mode){
        this.mode = mode;
    }

    public String getMode(){
        return mode;
    }
}
