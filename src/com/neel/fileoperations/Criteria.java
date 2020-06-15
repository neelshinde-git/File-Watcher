package com.neel.fileoperations;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Criteria {
    private String filename;
    private String action;
    private String destination;
    private String dateRange1;
    private String dateRange2;



    public Criteria(String filename, String action, String destination, String dateRange1, String dateRange2) {
        this.filename = filename;
        this.action = action;
        this.destination = destination;
        this.dateRange1 = dateRange1;
        this.dateRange2 = dateRange2;

    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateRange1() {
        return dateRange1;
    }

    public void setDateRange1(String dateRange1) {
        this.dateRange1 = dateRange1;
    }

    public String getDateRange2() {
        return dateRange2;
    }

    public void setDateRange2(String dateRange2) {
        this.dateRange2 = dateRange2;
    }

    public String getOnlyFilePath(){
        Pattern patternPath = Pattern.compile("(.*)\\\\");
        Matcher matcher = patternPath.matcher(filename);
        if(matcher.find()){
            return new String(matcher.group());
        }
        return filename;
    }

    public String getOnlyFileName(){
        Pattern patternPath = Pattern.compile("(.(?!\\\\))+$");
        Matcher matcher = patternPath.matcher(filename);
        if(matcher.find()){
            return matcher.group().replace("\\","");
        }
        return filename;
    }


}
