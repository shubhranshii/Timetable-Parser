package com.shubhranshi.apache_poi_demo.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TimetableFilterService {

    public Map<String, Map<String, List<String>>> filterTimetableByBatch(Map<String, Map<String, List<String>>> timetable, String batch) {
        Map<String, Map<String, List<String>>> filteredTimetable = new HashMap<>();

        for (Map.Entry<String, Map<String, List<String>>> dayEntry : timetable.entrySet()) {
            String day = dayEntry.getKey();
            Map<String, List<String>> timeslots = dayEntry.getValue();
            Map<String, List<String>> filteredTimeslots = new HashMap<>();

            for (Map.Entry<String, List<String>> timeslotEntry : timeslots.entrySet()) {
                String timeslot = timeslotEntry.getKey();
                List<String> subjects = timeslotEntry.getValue();
                List<String> filteredSubjects = new ArrayList<>();

                for (String subject : subjects) {
                    if (subject.contains(batch)) {
                        filteredSubjects.add(subject);
                    }
                }

                if (!filteredSubjects.isEmpty()) {
                    filteredTimeslots.put(timeslot, filteredSubjects);
                }
            }

            if (!filteredTimeslots.isEmpty()) {
                filteredTimetable.put(day, filteredTimeslots);
            }
        }

        return filteredTimetable;
    }
}
