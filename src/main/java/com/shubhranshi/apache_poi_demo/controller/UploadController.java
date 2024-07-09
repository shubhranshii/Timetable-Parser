package com.shubhranshi.apache_poi_demo.controller;

import com.shubhranshi.apache_poi_demo.service.TimetableParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UploadController {

    @Autowired
    private TimetableParser timetableParser;

    private Map<String, Map<String, List<String>>> timetable;

    @GetMapping("home")
    public String home() {
        return "home";
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("batch") String batch, Model model) {
        Map<String, Map<String, List<String>>> filteredTimetable;
        try {
            timetable = timetableParser.parseTimetable(file);
            filteredTimetable = timetableParser.filterTimetableByBatch(timetable, batch);
            StringBuilder fileContent = new StringBuilder();
            for (Map.Entry<String, Map<String, List<String>>> dayEntry : filteredTimetable.entrySet()) {
                fileContent.append(dayEntry.getKey()).append(":<br>");
                for (Map.Entry<String, List<String>> timeSlotEntry : dayEntry.getValue().entrySet()) {
                    fileContent.append(timeSlotEntry.getKey()).append(": ")
                            .append(String.join(", ", timeSlotEntry.getValue())).append("<br>");
                }
            }

            model.addAttribute("message", "File uploaded successfully!");
            model.addAttribute("fileContent", fileContent.toString());
            System.out.println(filteredTimetable);
        } catch (IOException e) {
            e.printStackTrace();
             // Return to upload page in case of error
        }
        return "upload";
    }
}
