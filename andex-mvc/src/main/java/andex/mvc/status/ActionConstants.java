package andex.mvc.status;

public enum  ActionConstants {
    ENABLE("enable"),
    DISABLE("disable"),
    VISIBILITY("visibility"),
    TEXT_STRING("text.string"),
    TEXT_RES("text.res"),
    HINT_STRING("hint.string"),
    HINT_RES("hint.res"),
    BACKGROUND_COLOR("background.color"),
    BACKGROUND_COLOR_INT("background.color.int"),
    BACKGROUND_RES("background.res")
    ;

    private String str;

    ActionConstants(String str) {
        this.str = str;
    }
}
