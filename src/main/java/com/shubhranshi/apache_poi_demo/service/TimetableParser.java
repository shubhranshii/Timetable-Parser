package com.shubhranshi.apache_poi_demo.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class TimetableParser {

    @Autowired
    TimetableFilterService timetableFilterService;

    public Map<String, Map<String, List<String>>> parseTimetable(MultipartFile file) throws IOException {
        Map<String, Map<String, List<String>>> timetable = new LinkedHashMap<>();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook;
            if (file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(is);
            } else if (file.getOriginalFilename().toLowerCase().endsWith(".xls")) {
                workbook = new HSSFWorkbook(is);
            } else {
                throw new IllegalArgumentException("Invalid file format. Please upload an Excel file.");
            }
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List<String> timeSlots = new ArrayList<>();
            String currentDay = null;
            int rowNum = 0;
            boolean truncate = false;

            while (rows.hasNext()) {
                Row row = rows.next();
                Iterator<Cell> cells = row.iterator();

                List<String> cellValues = new ArrayList<>();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    switch (cell.getCellType()) {
                        case STRING:
                            if (String.valueOf(cell.getStringCellValue()).equalsIgnoreCase("IMPORTANT INFORMATION")) {
                                truncate = true;
                                break;
                            }
                            cellValues.add(cell.getStringCellValue().trim());
                            break;
                        case NUMERIC:
                            cellValues.add(String.valueOf(cell.getNumericCellValue()));
                            break;
                        default:
                            cellValues.add("UNKNOWN");
                    }
                }

                if (truncate) {
                    break; // Stop processing rows if "IMPORTANT INFORMATION" is encountered
                }

                if (rowNum == 1) {
                    // The second row contains the time slots
                    timeSlots.addAll(cellValues.subList(1, cellValues.size()));
                    rowNum++;
                    continue;
                }

                // Check if the first cell is a day
                String firstCellValue = cellValues.get(0).trim();
                if (isDayOfWeek(firstCellValue)) {
                    currentDay = firstCellValue;
                    timetable.putIfAbsent(currentDay, new LinkedHashMap<>());
                    rowNum++;
                    // Fill in the time slots for the first row of the current day
                    for (int i = 0; i < timeSlots.size(); i++) {
                        String timeSlot = timeSlots.get(i);
                        timetable.get(currentDay).putIfAbsent(timeSlot, new ArrayList<>());
                        timetable.get(currentDay).get(timeSlot).add(cellValues.size() > i + 1 ? cellValues.get(i + 1) : "UNKNOWN");
                    }
                } else if (currentDay != null) {
                    // Handle rows where the day is not explicitly mentioned (marked with "_")
                    for (int i = 0; i < timeSlots.size(); i++) {
                        String timeSlot = timeSlots.get(i);
                        timetable.get(currentDay).putIfAbsent(timeSlot, new ArrayList<>());
                        timetable.get(currentDay).get(timeSlot).add(cellValues.size() > i + 1 ? cellValues.get(i + 1) : "UNKNOWN");
                    }
                }

                rowNum++;
            }
        }

        return timetable;
    }

    private boolean isDayOfWeek(String value) {
        switch (value.toUpperCase()) {
            case "MON":
            case "TUE":
            case "WED":
            case "THU":
            case "FRI":
            case "SAT":
            case "SUN":
                return true;
            default:
                return false;
        }
    }

    public Map<String, Map<String, List<String>>> filterTimetableByBatch(Map<String, Map<String, List<String>>> timetable, String batch) {
        return timetableFilterService.filterTimetableByBatch(timetable, batch);
    }
}