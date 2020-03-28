package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.*;

public class Main {

    public static void main(String[] args) {

        StoredDataObjects storedDataObjects = new StoredDataObjects();
        storedDataObjects.readDataObjects(args[0]);

        ArrayList<Lecture> allLectures = storedDataObjects.allLectures;




    }
}
