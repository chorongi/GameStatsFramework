package edu.cmu.cs.cs214.hw5.core;


import com.google.gdata.data.DateTime;

/**
 * Rating is a class that is used to store time and rating data
 */
public class Rating{
    private final DateTime time;
    private final int rating;

    /**
     * Constructor for rating
     * @param t is time
     * @param r is the player's rank at time t
     */
    public Rating(DateTime t, int r){
        time = t;
        rating = r;
    }

    /**
     * getter for time field
     * @return the time of the rating
     */
    public DateTime getTime(){
        return time;
    }

    /**
     * getter for rating field
     * @return rating of player
     */
    public int getRating(){
        return rating;
    }

    @Override
    public boolean equals(Object o){
        if(! (o instanceof Rating)){
            return false;
        }
        else{
            Rating r2 = (Rating)o;
            return r2.getTime().equals(time) && r2.getRating() == rating;
        }
    }

    @Override
    public int hashCode(){
        return java.util.Objects.hash(time, rating);
    }

}
