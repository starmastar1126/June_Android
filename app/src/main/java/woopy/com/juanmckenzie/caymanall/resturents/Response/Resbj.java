package woopy.com.juanmckenzie.caymanall.resturents.Response;

import java.util.ArrayList;

public class Resbj {

    ArrayList<Object> html_attributions = new ArrayList<Object>();
    Result ResultObject;
    private String status;


    // Getter Methods

    public Result getResult() {
        return ResultObject;
    }

    public String getStatus() {
        return status;
    }

    // Setter Methods

    public void setResult(Result resultObject) {
        this.ResultObject = resultObject;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public class Result {
        ArrayList<Object> address_components = new ArrayList<Object>();
        private String adr_address;
        private String formatted_address;
        private String formatted_phone_number;
        Geometry GeometryObject;
        private String icon;
        private String id;
        private String international_phone_number;
        private String name;
        Opening_hours Opening_hoursObject;
        ArrayList<Object> photos = new ArrayList<Object>();
        private String place_id;
        Plus_code Plus_codeObject;
        private float rating;
        private String reference;
        ArrayList<Object> reviews = new ArrayList<Object>();
        private String scope;
        ArrayList<Object> types = new ArrayList<Object>();
        private String url;
        private float user_ratings_total;
        private float utc_offset;
        private String vicinity;

        public ArrayList<Object> getAddress_components() {
            return address_components;
        }

        public void setAddress_components(ArrayList<Object> address_components) {
            this.address_components = address_components;
        }

        public Geometry getGeometryObject() {
            return GeometryObject;
        }

        public void setGeometryObject(Geometry geometryObject) {
            GeometryObject = geometryObject;
        }

        public Opening_hours getOpening_hoursObject() {
            return Opening_hoursObject;
        }

        public void setOpening_hoursObject(Opening_hours opening_hoursObject) {
            Opening_hoursObject = opening_hoursObject;
        }

        public ArrayList<Object> getPhotos() {
            return photos;
        }

        public void setPhotos(ArrayList<Object> photos) {
            this.photos = photos;
        }

        public Plus_code getPlus_codeObject() {
            return Plus_codeObject;
        }

        public void setPlus_codeObject(Plus_code plus_codeObject) {
            Plus_codeObject = plus_codeObject;
        }

        public ArrayList<Object> getReviews() {
            return reviews;
        }

        public void setReviews(ArrayList<Object> reviews) {
            this.reviews = reviews;
        }

        public ArrayList<Object> getTypes() {
            return types;
        }

        public void setTypes(ArrayList<Object> types) {
            this.types = types;
        }
        // Getter Methods

        public String getAdr_address() {
            return adr_address;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public String getFormatted_phone_number() {
            return formatted_phone_number;
        }

        public Geometry getGeometry() {
            return GeometryObject;
        }

        public String getIcon() {
            return icon;
        }

        public String getId() {
            return id;
        }

        public String getInternational_phone_number() {
            return international_phone_number;
        }

        public String getName() {
            return name;
        }

        public Opening_hours getOpening_hours() {
            return Opening_hoursObject;
        }

        public String getPlace_id() {
            return place_id;
        }

        public Plus_code getPlus_code() {
            return Plus_codeObject;
        }

        public float getRating() {
            return rating;
        }

        public String getReference() {
            return reference;
        }

        public String getScope() {
            return scope;
        }

        public String getUrl() {
            return url;
        }

        public float getUser_ratings_total() {
            return user_ratings_total;
        }

        public float getUtc_offset() {
            return utc_offset;
        }

        public String getVicinity() {
            return vicinity;
        }

        // Setter Methods

        public void setAdr_address(String adr_address) {
            this.adr_address = adr_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public void setFormatted_phone_number(String formatted_phone_number) {
            this.formatted_phone_number = formatted_phone_number;
        }

        public void setGeometry(Geometry geometryObject) {
            this.GeometryObject = geometryObject;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setInternational_phone_number(String international_phone_number) {
            this.international_phone_number = international_phone_number;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setOpening_hours(Opening_hours opening_hoursObject) {
            this.Opening_hoursObject = opening_hoursObject;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public void setPlus_code(Plus_code plus_codeObject) {
            this.Plus_codeObject = plus_codeObject;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setUser_ratings_total(float user_ratings_total) {
            this.user_ratings_total = user_ratings_total;
        }

        public void setUtc_offset(float utc_offset) {
            this.utc_offset = utc_offset;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }
    }

    public class Plus_code {
        private String compound_code;
        private String global_code;


        // Getter Methods

        public String getCompound_code() {
            return compound_code;
        }

        public String getGlobal_code() {
            return global_code;
        }

        // Setter Methods

        public void setCompound_code(String compound_code) {
            this.compound_code = compound_code;
        }

        public void setGlobal_code(String global_code) {
            this.global_code = global_code;
        }
    }

    public class Opening_hours {
        private boolean open_now;
        ArrayList<Object> periods = new ArrayList<Object>();
        ArrayList<Object> weekday_text = new ArrayList<Object>();


        // Getter Methods

        public boolean getOpen_now() {
            return open_now;
        }

        // Setter Methods

        public void setOpen_now(boolean open_now) {
            this.open_now = open_now;
        }
    }

    public class Geometry {
        Location LocationObject;
        Viewport ViewportObject;


        // Getter Methods

        public Location getLocation() {
            return LocationObject;
        }

        public Viewport getViewport() {
            return ViewportObject;
        }

        // Setter Methods

        public void setLocation(Location locationObject) {
            this.LocationObject = locationObject;
        }

        public void setViewport(Viewport viewportObject) {
            this.ViewportObject = viewportObject;
        }
    }

    public class Viewport {
        Northeast NortheastObject;
        Southwest SouthwestObject;


        // Getter Methods

        public Northeast getNortheast() {
            return NortheastObject;
        }

        public Southwest getSouthwest() {
            return SouthwestObject;
        }

        // Setter Methods

        public void setNortheast(Northeast northeastObject) {
            this.NortheastObject = northeastObject;
        }

        public void setSouthwest(Southwest southwestObject) {
            this.SouthwestObject = southwestObject;
        }
    }

    public class Southwest {
        private float lat;
        private float lng;


        // Getter Methods

        public float getLat() {
            return lat;
        }

        public float getLng() {
            return lng;
        }

        // Setter Methods

        public void setLat(float lat) {
            this.lat = lat;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }
    }

    public class Northeast {
        private float lat;
        private float lng;


        // Getter Methods

        public float getLat() {
            return lat;
        }

        public float getLng() {
            return lng;
        }

        // Setter Methods

        public void setLat(float lat) {
            this.lat = lat;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }
    }

    public class Location {
        private float lat;
        private float lng;


        // Getter Methods

        public float getLat() {
            return lat;
        }

        public float getLng() {
            return lng;
        }

        // Setter Methods

        public void setLat(float lat) {
            this.lat = lat;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }
    }
}