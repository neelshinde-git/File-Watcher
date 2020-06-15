package com.neel.fileoperations;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Working {
    public static ArrayList<Criteria> allCriteria = new ArrayList<>();
    private static String logFilePath;

    public static void ReadConfigFile(String configFile) {
        /*
        First two lines of config should be as below:
        Config file path
        Filename|Action|Path|DateRange1|DateRange2
        */
        try (Scanner scanner = new Scanner(new FileReader(configFile))) {
            logFilePath = scanner.nextLine();
            scanner.nextLine();
            scanner.useDelimiter("\\|");
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                String[] tokens = str.split("\\|");
                if(tokens[0].equals("-")) tokens[0]="";
                if(tokens[1].equals("-")) tokens[1]="";
                if(tokens[2].equals("-")) tokens[2]="";
                if(tokens[3].equals("-")) tokens[3]="";
                if(tokens[4].equals("-")) tokens[4]="";
                allCriteria.add(new Criteria(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]));
            }
            writeLogs("Config file read successfully.");

        } catch (Exception e) {
            writeLogs("ERROR - Config file read failed : " + e.toString());
        }
    }

    public static void printConfiguration(){
        for (int i = 0; i < allCriteria.size(); i++) {
            System.out.println("File: " + allCriteria.get(i).getFilename() +
                            " Action: " + allCriteria.get(i).getAction()+
                            " Destination: " + allCriteria.get(i).getDestination()+
                    " Time Range between: " + allCriteria.get(i).getDateRange1() + " and " + allCriteria.get(i).getDateRange2()
                    );
        }
    }

    public static void writeLogs(String message) {
        String logFilename = logFilePath + "\\" + "File_Watcher_" + FileOperations.getDateTime("date") + ".txt";
        //System.out.println(logFilename);
        try (FileWriter logFile = new FileWriter(logFilename, true)) {
            String timestamp = FileOperations.getDateTime("datetime");
            logFile.write("\n" + timestamp + " : " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void processCriteria() throws IOException {
        for (Criteria criteria : allCriteria){
            if(criteria.getAction().equals("COPY")){
                FileOperations.copyFile(criteria);
            } else if (criteria.getAction().equals("CONCAT")){
                FileOperations.concatFiles(criteria);
            }else if(criteria.getAction().equals("MOVE")){
                FileOperations.moveFile(criteria);
            }else if(criteria.getAction().equals("RENAME")){
                FileOperations.renameFile(criteria);
            }
        }
    }
}
