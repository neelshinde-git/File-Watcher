package com.neel.fileoperations;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


public class FileOperations {

    public static final String TIMESTAMP =  getDateTime("datetime");
    public static final String DATE = getDateTime("date");
    public static final String TIME = getDateTime("time");

    public static String getDateTime(String parameter){
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter dateformat;
        switch(parameter){
            case "datetime":
                 dateformat = DateTimeFormatter.ofPattern("ddMMyyyyhhmmss");
                break;
            case "time":
                dateformat = DateTimeFormatter.ofPattern("hhmmss");
                break;
            case "date":
                dateformat = DateTimeFormatter.ofPattern("ddMMyyyy");
                break;
            default:
                dateformat = DateTimeFormatter.ofPattern("ddMMyyyyhhmmss");
                break;
        }
        return dateformat.format(dateTime);
    }

    public static void renameFile(Criteria criteria){
        File[] input = getAllFiles(criteria);

        if(input.length==1 && !criteria.getDestination().equals("") && input[0].exists()){
            String DATE_TIME=DATE+"_"+TIME;
            String outFile;
            if(criteria.getDestination().contains("%DATE_TIME")) {
                outFile = criteria.getDestination().replace("%DATE_TIME",DATE_TIME);
            } else if (criteria.getDestination().contains("%TIME")){
                outFile = criteria.getDestination().replace("%TIME",TIME);
            }else if (criteria.getDestination().contains("%DATE")){
                outFile = criteria.getDestination().replace("%DATE",DATE);
            } else {
                outFile = criteria.getDestination();
            }
            File output = new File (input[0].getParent()+"\\"+outFile);
            if(output.exists()){
                Working.writeLogs("ERROR - Rename failed - File " + output.getAbsolutePath() + " already exists");
            }else{
                Working.writeLogs("File " + input[0].getAbsolutePath() + " renamed to " + output.getAbsolutePath() + " " + input[0].renameTo(output));
            }
        } else{
            Working.writeLogs("ERROR - Renamed failed for  " + input[0].getAbsolutePath() + "multiple input files OR no input file OR new name not provided");
        }
    }

    public static void copyFile(Criteria criteria) throws IOException {
        File[] listOfFiles = getAllFiles(criteria);
        if (listOfFiles!=null){
            if (!(criteria.getDestination().equals(null)||criteria.getDestination().equals(""))){
                for(int i = 0; i<listOfFiles.length;i++){
                    Path input = Paths.get(listOfFiles[i].getAbsolutePath());
                    Path output = Paths.get(criteria.getDestination()+"\\"+listOfFiles[i].getName());
                    try {
                        Files.copy(input, output, REPLACE_EXISTING);
                        Working.writeLogs("Copying " + listOfFiles[i].getAbsolutePath() + " to " + criteria.getDestination());
                    }catch (IOException e){
                        Working.writeLogs("ERROR -  Copying file : " + e.toString());
                    }
                }
            }else {
                Working.writeLogs("ERROR - Destination not specified for Copying " + criteria.getFilename());
            }
        } else {
            Working.writeLogs("ERROR -  No such files found : " + criteria.getFilename());
        }
    }

    public static void moveFile(Criteria criteria){
        File[] input = getAllFiles(criteria);
        if(input!=null && !criteria.getDestination().equals("") && input.length>0){
            for(int i=0;i<input.length;i++){
                try{
                    Path movement = Files.move(Paths.get(input[i].getAbsolutePath()),Paths.get(criteria.getDestination()+"\\"+input[i].getName()));
                    if(movement!=null) Working.writeLogs("File " + input[i].getAbsolutePath() + " moved to " + criteria.getDestination());
                    else Working.writeLogs("ERROR - Fine movement for " + input[i].getAbsolutePath() + " to " +  criteria.getDestination() + " failed.");
                }catch (Exception e){
                    Working.writeLogs("ERROR - File movement for " + input[i].getAbsolutePath() + " failed due to : " + e.toString());
                }
            }
        } else {
            Working.writeLogs("ERROR - File " + criteria.getFilename() + " OR " + criteria.getDestination() + " not found, check Config or actual paths.");
        }
    }

    public static File[] getAllFiles(Criteria criteria){
        File f = new File(criteria.getOnlyFilePath());
        FilenameFilter filter = (dir, name) -> {
                return name.matches(criteria.getOnlyFileName());
        };
        return f.listFiles(filter);
        }

    public static void concatFiles(Criteria criteria) throws IOException {
        File[] input = getAllFiles(criteria);
        FileInputStream inputStream= null;
        String DATE_TIME=DATE+"_"+TIME;
        String linebreak= System.lineSeparator();
        String outFile ;
        if(criteria.getDestination().contains("%DATE_TIME")) {
            outFile = criteria.getDestination().replace("%DATE_TIME",DATE_TIME);
        } else if (criteria.getDestination().contains("%TIME")){
            outFile = criteria.getDestination().replace("%TIME",TIME);
        }else if (criteria.getDestination().contains("%DATE")){
            outFile = criteria.getDestination().replace("%DATE",DATE);
        } else {
            outFile = criteria.getDestination();
        }
        if(input!=null){
             FileWriter output = new FileWriter(outFile,true);
            for(int i=0; i < input.length; i++){
                try(Scanner br = new Scanner(input[i])){
                    Working.writeLogs("Concatenating file " + input[i].getAbsolutePath() + " to " + outFile);
                    while (br.hasNextLine()) {
                        output.write(br.nextLine()+System.lineSeparator());
                    }


                } catch (IOException e){
                    Working.writeLogs("ERROR - Concat unsuccessful for : " + input[i].getAbsolutePath() + " because :" + e.toString());
                    output.close();
                    break;
                }
                if(i==input.length-1){
                    for(int a=0; a < input.length; a++) {
                        Working.writeLogs("Deleting file : " + input[a].getAbsolutePath() + " " + input[a].delete());
                    }
                }
            }
            output.close();
        }
    }
}
