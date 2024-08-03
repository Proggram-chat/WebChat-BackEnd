package sanity.nil.webchat.application.consts;

public enum ChatAction {
    GET_NEW_MESSAGES("getMessages"),
    SEND_NEW_MESSAGE("sendMessage");

    private final String value;

    public String getValue() {
        return value;
    }

    ChatAction(String value) {
        this.value = value;
    }
}
