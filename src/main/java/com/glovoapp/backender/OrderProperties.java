package com.glovoapp.backender;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "backender.orders")
public class OrderProperties {

    /**
     * Keyword using for filter orders for glovers with box
     */
    private String[] keywords = {"pizza", "cake", "flamingo"};

    /**
     * Bicycle distance limit. Orders more then this value won't shows to the glover on a bicycle
     */
    private double bicycleLimit = 5.0;

    /**
     * Sorting param in km. Orders will be sorted by this value (one by one every each distance step)
     */
    private double distanceStep = 0.5;

    /**
     * Sorting priority parameters (string words separated by comma)
     */
    private String[] sortFields = {"DISTANCE", "VIP", "FOOD"};


    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public double getBicycleLimit() {
        return bicycleLimit;
    }

    public void setBicycleLimit(double bicycleLimit) {
        this.bicycleLimit = bicycleLimit;
    }

    public double getDistanceStep() {
        return distanceStep;
    }

    public void setDistanceStep(double distanceStep) {
        this.distanceStep = distanceStep;
    }

    public String[] getSortFields() {
        return sortFields;
    }

    public void setSortFields(String[] sortFields) {
        this.sortFields = sortFields;
    }
}
