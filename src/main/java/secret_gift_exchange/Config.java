package secret_gift_exchange;

public class Config {

    // app config
    public static int MAX_RETRY = 1000;

    // root path
    public static final String ROOT_PATH = "secret-gift-exchange";

    // sub path
    public static final String BUILD_EXCHANGE_PAIRS = "build-exchange-pairs";

    // email config
    public static String EMAIL_HOST = "localhost";
    public static String EMAIL_FROM = "abc@gmail.com";
    public static String EMAIL_SUBJECT = "Secret Gift Game";
    public static String EMAIL_TEMPLATE = "Hi %s, you have selected to send a gift to %s.";
}
