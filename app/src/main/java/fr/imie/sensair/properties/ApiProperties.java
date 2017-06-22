package fr.imie.sensair.properties;

/**
 * Created by mirouf on 20/06/17.
 */

public class ApiProperties {
    private static ApiProperties mInstance= null;

    public String addressServer = "http://10.3.3.47/IotApi/web/app.php";
    public String apiPath = "/api";
    public String oauthPath = "/oauth/v2/token";

    protected ApiProperties() {}

    public static synchronized ApiProperties getInstance() {
        if (null == mInstance) {
            mInstance = new ApiProperties();
        }

        return mInstance;
    }
}
