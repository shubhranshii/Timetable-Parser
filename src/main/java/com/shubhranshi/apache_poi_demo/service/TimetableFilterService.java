package com.shubhranshi.apache_poi_demo.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TimetableFilterService {

    private static final Pattern BATCH_RANGE_PATTERN = Pattern.compile("([A-Z]{1}[0-9]+)-([A-Z]{1}[0-9]+)");
    private static final Pattern LB_BATCH_PATTERN = Pattern.compile("LB+");

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
                    if (isBatchInSubject(batch, subject)) {
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

    private boolean isBatchInSubject(String batch, String subject) {
        // Check for exact match
        if (subject.contains(batch)) {
            return true;
        }

        // Check for batch ranges
        Matcher matcher = BATCH_RANGE_PATTERN.matcher(subject);
        while (matcher.find()) {
            String startBatch = matcher.group(1);
            String endBatch = matcher.group(2);

            if (isBatchInRange(batch, startBatch, endBatch)) {
                return true;
            }
        }

        // Check for LB# pattern if the batch starts with B
        if (batch.startsWith("B")) {
            Matcher lbMatcher = LB_BATCH_PATTERN.matcher(subject);
            if (lbMatcher.find()) {
                return true;
            }
        }

        return false;
    }

    private boolean isBatchInRange(String batch, String startBatch, String endBatch) {
        // Extract letters and numbers from batch, startBatch, and endBatch
        String batchLetter = batch.substring(0, 1);
        int batchNumber = Integer.parseInt(batch.substring(1));
        String startBatchLetter = startBatch.substring(0, 1);
        int startBatchNumber = Integer.parseInt(startBatch.substring(1));
        String endBatchLetter = endBatch.substring(0, 1);
        int endBatchNumber = Integer.parseInt(endBatch.substring(1));

        // Ensure the letters are the same and batch number falls within the range
        return batchLetter.equals(startBatchLetter) &&
                batchLetter.equals(endBatchLetter) &&
                batchNumber >= startBatchNumber && batchNumber <= endBatchNumber;
    }
}
