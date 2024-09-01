package sanity.nil.webchat.application.consts;

public enum MemberRoleType {
    ADMIN("0,1,2,3,4,5,6"),
    MODERATOR("1,2,3,4,5,6"),
    MEMBER("4,5,6");

    private String functions;

    MemberRoleType(String functions) {
        this.functions = functions;
    }

    public String getFunctions() {
        return functions;
    }
}
