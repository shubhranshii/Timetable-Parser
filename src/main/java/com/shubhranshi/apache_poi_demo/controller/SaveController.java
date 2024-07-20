package com.shubhranshi.apache_poi_demo.controller;

import com.shubhranshi.apache_poi_demo.service.TimetableParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SaveController {

    private final TimetableParser timetableParser;

    public SaveController(TimetableParser timetableParser) {
        this.timetableParser = timetableParser;
    }

    @PostMapping("/save-timetable")
    public ResponseEntity<byte[]> saveTimetable(@RequestBody Map<String, Map<String, String>> timetableData) {
        try {
            Map<String, Map<String, List<String>>> formattedTimetable = new LinkedHashMap<>();

            for (Map.Entry<String, Map<String, String>> dayEntry : timetableData.entrySet()) {
                formattedTimetable.put(dayEntry.getKey(), new LinkedHashMap<>());
                for (Map.Entry<String, String> slotEntry : dayEntry.getValue().entrySet()) {
                    formattedTimetable.get(dayEntry.getKey()).put(slotEntry.getKey(), Arrays.asList(slotEntry.getValue().split(", ")));
                }
            }

            byte[] excelBytes = timetableParser.saveTimetableToExcel(formattedTimetable);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "timetable.xlsx");

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
