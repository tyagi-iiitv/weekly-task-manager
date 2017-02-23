package com.tyagi.anjul.weeklytaskmanager;

import java.io.Serializable;

/**
 * Created by root on 29/7/15.
 */
public class Task implements Serializable {
    private String name;
    private int hours;
    private int mins;
    private int remaining_hours;
    private int remaining_mins;
    private boolean to_delete;
    private boolean mark_as_done;
    private boolean running_status;
    private long last_updated;

    public Task(String name, int hours, int mins){
        this.name = name;
        this.hours = hours;
        this.mins = mins;
        to_delete = false;
        remaining_hours = hours;
        remaining_mins = mins;
        mark_as_done = false;
        running_status = false;
        last_updated = System.currentTimeMillis();
    }

    public String getName(){
        return name;
    }

    public Integer getHours(){
        return hours;
    }

    public Integer getMins(){
        return mins;
    }

    public Boolean get_delete_status(){
        return to_delete;
    }

    public void set_delete_status(Boolean value){
        to_delete = value;
    }

    public void set_remaining_hours(int value){
        remaining_hours = value;
    }

    public void set_remaining_mins(int value){
        remaining_mins = value;
    }

    public Integer get_remaining_hours(){
        return remaining_hours;
    }

    public Integer get_remaining_mins(){
        return remaining_mins;
    }

    public Boolean get_running_status(){
        return running_status;
    }

    public void set_running_status(Boolean value){
        running_status = value;
    }

    public void set_last_updated(long value){
        last_updated = value;
    }

    public Long get_last_updated(){
        return last_updated;
    }
}
