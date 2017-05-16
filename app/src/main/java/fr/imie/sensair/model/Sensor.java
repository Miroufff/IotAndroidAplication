package fr.imie.sensair.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by mirouf on 12/05/17.
 */

@Table(name = "sensor")
public class Sensor extends Model
{
    @Column(name = "displayName")
    private String displayName;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "product")
    private String product;

    @Column(name = "version")
    private int version;

    @Column(name = "enable")
    private boolean enable;

    @Column(name = "User")
    private User user;

    public Sensor() {
        super();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
