package com.image.login.tomcat.model;

/**
 * Created by ejaiwng on 3/2/2018.
 */
public class LoginResult extends Result{

    private long timeUsed;
    private double match;
    private int matchThreshold;
    private boolean pass;

    public long getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(long timeUsed) {
        this.timeUsed = timeUsed;
    }

    public double getMatch() {
        return match;
    }

    public void setMatch(double match) {
        this.match = match;
    }

    public int getMatchThreshold() {
        return matchThreshold;
    }

    public void setMatchThreshold(int matchThreshold) {
        this.matchThreshold = matchThreshold;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }


}
