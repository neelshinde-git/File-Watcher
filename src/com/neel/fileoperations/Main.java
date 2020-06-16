package com.neel.fileoperations;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String configFile = "C:\\Java_project_work\\Configuration.csv";
        Working.ReadConfigFile(configFile);
        //Working.printConfiguration();
        Working.processCriteria();
        Working.deleteOldLogs(7);
    }
}
