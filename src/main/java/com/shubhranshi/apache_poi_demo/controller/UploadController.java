package com.shubhranshi.apache_poi_demo.controller;

import com.shubhranshi.apache_poi_demo.service.TimetableParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UploadController {

    private final TimetableParser timetableParser;

    public UploadController(TimetableParser timetableParser) {
        this.timetableParser = timetableParser;
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("batch") String batch, Model model) {
        Map<String, Map<String, List<String>>> timetable = null;
        try {
            timetable = timetableParser.parseTimetable(file);
            Map<String, Map<String, List<String>>> filteredTimetable = timetableParser.filterTimetableByBatch(timetable, batch);
            System.out.println(timetable);

            List<String> days = new ArrayList<>(timetable.keySet());
            List<String> timeSlots = new ArrayList<>();

            if (!timetable.isEmpty() && !days.isEmpty() && timetable.containsKey(days.getFirst())) {
                timeSlots.addAll(timetable.get(days.getFirst()).keySet());
            }

            System.out.println(days);
            System.out.println(timeSlots);
            model.addAttribute("message", "File uploaded successfully!");
            //model.addAttribute("fileContent", generateFileContent(timetable));
            model.addAttribute("timetable", filteredTimetable);
            model.addAttribute("days", days);
            model.addAttribute("timeSlots", timeSlots);

            System.out.println(filteredTimetable);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Error uploading file: " + e.getMessage());
        }
        return "upload";
    }

     private String generateFileContent(Map<String, Map<String, List<String>>> filteredTimetable) {
        StringBuilder fileContent = new StringBuilder();
        for (Map.Entry<String, Map<String, List<String>>> dayEntry : filteredTimetable.entrySet()) {
            fileContent.append(dayEntry.getKey()).append(":<br>");
            for (Map.Entry<String, List<String>> timeSlotEntry : dayEntry.getValue().entrySet()) {
                fileContent.append(timeSlotEntry.getKey()).append(": ")
                        .append(String.join(", ", timeSlotEntry.getValue())).append("<br>");
            }
        }
        return fileContent.toString();
    }
}