package woopy.com.juanmckenzie.caymanall.Objects;


import java.util.ArrayList;
import java.util.List;

public class Categories {


    private String category;
    private String ImageUrl;

    private List<SubCategories> subCategories = new ArrayList<>();

    public List<SubCategories> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategories> subCategories) {
        this.subCategories = subCategories;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}

