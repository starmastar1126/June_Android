package woopy.com.juanmckenzie.caymanall.resturents;


import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by iamcs on 2017-05-04.
 */

public class PlaceDetailContract {

    //Content Authority
    public static final String CONTENT_AUTHORITY = "me.chandansharma.aroundme";
    //Base Uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //content path
    public static final String PATH_PLACE = "place";

    private PlaceDetailContract() {
    }

    public static final class PlaceDetailEntry {

        //Content Uri
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PLACE);

        //All the constant that are used to store the place details in table
        public static final String PLACE_TABLE_NAME = "place_detail";

        public static final String _ID = "_id";
        public static final String COLUMN_PLACE_ID = "place_id";
        public static final String COLUMN_PLACE_LATITUDE = "place_latitude";
        public static final String COLUMN_PLACE_LONGITUDE = "place_longitude";
        public static final String COLUMN_PLACE_NAME = "place_name";
        public static final String COLUMN_PLACE_OPENING_HOUR_STATUS = "place_opening_hour_status";
        public static final String COLUMN_PLACE_RATING = "place_rating";
        public static final String COLUMN_PLACE_ADDRESS = "place_address";
        public static final String COLUMN_PLACE_PHONE_NUMBER = "place_phone_number";
        public static final String COLUMN_PLACE_WEBSITE = "place_website";
        public static final String COLUMN_PLACE_SHARE_LINK = "place_share_link";

        /**
         //Table Constant for Place User Rating
         public static final String PLACE_RATING_TABLE_NAME = "rating_detail";
         public static final String COLUMN_PLACE_RATING_AUTHOR_NAME = "place_rating_author_name";
         public static final String COLUMN_PLACE_RATING_AUTHOR_PROFILE_PICTURE_URL =
         "place_rating_author_profile_picture_url";
         public static final String COLUMN_PLACE_RATING_AUTHOR_RATING = "place_author_rating";
         public static final String COLUMN_PLACE_RATING_RELATIVE_TIME_DESCRIPTION = "" +
         "place_rating_relative_time_description";
         public static final String COLUMN_PLACE_RATING_AUTHOR_REVIEW_TEXT = "" +
         "place_rating_author_review_text";
         **/
        //The MIME type of the Place
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE;
        //The MIME type of the Single Place
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE;
    }
}