package com.example.meteors;

public class SpacePlanetItem
{
    private int imageResource;
    private String spaceNameTextView;
    private String costTextView;

    public SpacePlanetItem(int imageResource, String spaceNameTextView, String costTextView)
    {
        this.imageResource = imageResource;
        this.spaceNameTextView = spaceNameTextView;
        this.costTextView = costTextView;
    }


    public int getImageResource()
    {
        return imageResource;
    }

    public String getSpaceNameTextView()
    {
        return spaceNameTextView;
    }

    public String getCostTextView()
    {
        return costTextView;
    }

}
