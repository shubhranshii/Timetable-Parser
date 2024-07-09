package com.shubhranshi.apache_poi_demo.service;

import com.shubhranshi.apache_poi_demo.model.Subject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SubjectService {

    private Map<String, Subject> subjects;

    public SubjectService() {
        this.subjects = new HashMap<>();
        initializeSubjects();
    }

    private void initializeSubjects() {
        // Hardcode the subject codes and names
        subjects.put("18B11CI121", new Subject("18B11CI121", "Fundamentals of Computers & Programming - II"));
        subjects.put("18B15CI121", new Subject("18B15CI121", "Fundamentals of Computers & Programming Lab- II"));
        subjects.put("15B11CI211", new Subject("15B11CI211", "Software Development Fundamentals-II"));
        subjects.put("15B17CI271", new Subject("15B17CI271", "Software Development Fundamentals Lab-II"));
        subjects.put("15B11EC111", new Subject("15B11EC111", "Electrical Science-I"));
        subjects.put("15B17EC171", new Subject("15B17EC171", "Electrical Science Lab-I"));
        subjects.put("18B15GE111", new Subject("18B15GE111", "Engineering Drawing & Design"));
        subjects.put("18B15GE112", new Subject("18B15GE112", "Workshop"));
        subjects.put("15B11MA211", new Subject("15B11MA211", "MATHEMATICS-II"));
        subjects.put("15B11MA212", new Subject("15B11MA212", "BASIC MATHEMATICS-II"));
        subjects.put("22B12HS111", new Subject("22B12HS111", "LIFE SKILLS AND EFFECTIVE COMMUNICATION"));
        subjects.put("18B15BT111", new Subject("18B15BT111", "Basic Bioscience Lab"));
        subjects.put("15B11PH211", new Subject("15B11PH211", "Physics-2"));
        subjects.put("15B17PH271", new Subject("15B17PH271", "Physics Lab-2"));
        subjects.put("15B11PH212", new Subject("15B11PH212", "Biophysical Techniques"));
        // Add more subjects as needed
    }

    public Map<String, Subject> getSubjects() {
        return subjects;
    }
}
