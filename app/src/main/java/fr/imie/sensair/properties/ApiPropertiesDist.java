package fr.imie.sensair.properties;

/**
 * Created by mirouf on 20/06/17.
 */

public class ApiPropertiesDist {
    private static ApiPropertiesDist mInstance= null;

    public String addressServer = "ip";
    public String apiPath = "/api";
    public String oauthPath = "/oauth/v2/token";

    protected ApiPropertiesDist() {}

    public static synchronized ApiPropertiesDist getInstance() {
        if (null == mInstance) {
            mInstance = new ApiPropertiesDist();
        }

        return mInstance;
    }
}
