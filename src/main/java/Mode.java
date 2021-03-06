public enum Mode {

    BUSY("BUSY"),
    READY("READY"),
    PLAYING("PLAYING");

    private final String mode;

    Mode(String mode){
        this.mode = mode;
    }

    @Override
    public String toString() {
        return mode;
    }
}
