package woopy.com.juanmckenzie.caymanall.resturents.Modal;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by iamcs on 2017-05-02.
 */

public class PlaceUserRating implements Parcelable{


    /**
     * All Reference Variable
     */
    private String mAuthorName;
    private String mAuthorProfilePictureUrl;
    private Double mAuthorPlaceRating;
    private String mPlaceRatingRelativeTimeDescription;
    private String mAuthorReviewText;

    /**
     *
     * @param mAuthorName name of the author
     * @param mAuthorProfilePictureUrl author profile picture
     * @param mPlaceRating rating given by author for current place
     * @param mPlaceRatingRelativeTimeDescription how old rating is
     * @param mAuthorReviewText rating description
     */

    public PlaceUserRating(String mAuthorName, String mAuthorProfilePictureUrl,
                           Double mPlaceRating, String mPlaceRatingRelativeTimeDescription,
                           String mAuthorReviewText){
        this.mAuthorName = mAuthorName;
        this.mAuthorProfilePictureUrl = mAuthorProfilePictureUrl;
        this.mAuthorPlaceRating = mPlaceRating;
        this.mPlaceRatingRelativeTimeDescription = mPlaceRatingRelativeTimeDescription;
        this.mAuthorReviewText = mAuthorReviewText;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String authorName) {
        mAuthorName = authorName;
    }

    public String getAuthorProfilePictureUrl() {
        return mAuthorProfilePictureUrl;
    }

    public void setAuthorProfilePictureUrl(String authorProfilePictureUrl) {
        mAuthorProfilePictureUrl = authorProfilePictureUrl;
    }

    public Double getAuthorPlaceRating() {
        return mAuthorPlaceRating;
    }

    public void setAuthorPlaceRating(Double authorPlaceRating) {
        mAuthorPlaceRating = authorPlaceRating;
    }

    public String getPlaceRatingRelativeTimeDescription() {
        return mPlaceRatingRelativeTimeDescription;
    }

    public void setPlaceRatingRelativeTimeDescription(String placeRatingRelativeTimeDescription) {
        mPlaceRatingRelativeTimeDescription = placeRatingRelativeTimeDescription;
    }

    public String getAuthorReviewText() {
        return mAuthorReviewText;
    }

    public void setAuthorReviewText(String authorReviewText) {
        mAuthorReviewText = authorReviewText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<PlaceUserRating> CREATOR = new Parcelable
            .Creator<PlaceUserRating>() {

        @Override
        public PlaceUserRating createFromParcel(Parcel source) {
            return new PlaceUserRating(source);
        }

        @Override
        public PlaceUserRating[] newArray(int size) {
            return new PlaceUserRating[size];
        }
    };

    /**
     * Storing the PlaceUserRating data to Parcel object
     **/
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthorName);
        dest.writeString(mAuthorProfilePictureUrl);
        dest.writeDouble(mAuthorPlaceRating);
        dest.writeString(mPlaceRatingRelativeTimeDescription);
        dest.writeString(mAuthorReviewText);
    }

    /**
     * Retrieving PlaceUserRating data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private PlaceUserRating(Parcel in){
        this.mAuthorName = in.readString();
        this.mAuthorProfilePictureUrl = in.readString();
        this.mAuthorPlaceRating = in.readDouble();
        this.mPlaceRatingRelativeTimeDescription = in.readString();
        this.mAuthorReviewText = in.readString();
    }
}