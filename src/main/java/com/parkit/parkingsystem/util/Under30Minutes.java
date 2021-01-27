package com.parkit.parkingsystem.util;

public class Under30Minutes {

    public boolean isUnder30Minutes(float deltaTimes){
        if (deltaTimes < 30){
            return true;
        }
        return false;
    }
}
