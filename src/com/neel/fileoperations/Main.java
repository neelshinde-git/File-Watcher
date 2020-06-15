package com.neel.fileoperations;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Working.ReadConfigFile("C:\\Java_project_work\\Configuration.csv");
        //Working.printConfiguration();
        Working.processCriteria();
    }
}
