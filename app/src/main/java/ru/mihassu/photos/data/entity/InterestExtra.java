
package ru.mihassu.photos.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InterestExtra {

    @SerializedName("explore_date")
    @Expose
    private String exploreDate;
    @SerializedName("next_prelude_interval")
    @Expose
    private String nextPreludeInterval;

    public String getExploreDate() {
        return exploreDate;
    }

    public void setExploreDate(String exploreDate) {
        this.exploreDate = exploreDate;
    }

    public String getNextPreludeInterval() {
        return nextPreludeInterval;
    }

    public void setNextPreludeInterval(String nextPreludeInterval) {
        this.nextPreludeInterval = nextPreludeInterval;
    }

}
