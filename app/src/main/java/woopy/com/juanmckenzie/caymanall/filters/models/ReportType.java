package woopy.com.juanmckenzie.caymanall.filters.models;

/**
 * @author cubycode
 * @since 12/08/2018
 * All rights reserved
 */
public enum ReportType {

    AD("Ad"),
    USER("User"),
    NEWS("News"),
    EVENTS("Events");

    private String value;

    ReportType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
