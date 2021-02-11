
package com.example.sportingcenterandroidapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseEvento {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("dataFine")
    @Expose
    private String dataFine;
    @SerializedName("inizio")
    @Expose
    private String inizio;
    @SerializedName("activityId")
    @Expose
    private String activityId;
    @SerializedName("number")
    @Expose
    private Object number;

    public ResponseEvento(String title, Integer number, String data){
        this.title=title;
        this.number = number;
        this.inizio = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataFine() {
        return dataFine;
    }

    public void setDataFine(String dataFine) {
        this.dataFine = dataFine;
    }

    public String getInizio() {
        return inizio;
    }

    public void setInizio(String inizio) {
        this.inizio = inizio;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Object getNumber() {
        return number;
    }

    public void setNumber(Object number) {
        this.number = number;
    }

}
