package woopy.com.juanmckenzie.caymanall.filters.models;

/**
 * @author cubycode
 * @since 12/08/2018
 * All rights reserved
 */
public enum SortBy {

    RECENT("Recent"),
    LOWEST_PRICE("Lowest Price"),
    HIGHEST_PRICE("Highest Price"),
    NEW("New"),
    USED("Used"),
    MOST_LIKED("Most Liked");

    private String value;

    SortBy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SortBy getForValue(String value) {
        for (int i = 0; i < SortBy.values().length; i++) {
            if (SortBy.values()[i].getValue().equals(value)) {
                return SortBy.values()[i];
            }
        }
        return null;
    }
}
