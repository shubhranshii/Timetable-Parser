package com.shubhranshi.apache_poi_demo.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
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

            while (rows.hasNext()) {
                Row row = rows.next();
                Iterator<Cell> cells = row.iterator();

                List<String> cellValues = new ArrayList<>();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    switch (cell.getCellType()) {
                        case STRING:
                            cellValues.add(cell.getStringCellValue().trim());
                            break;
                        case NUMERIC:
                            cellValues.add(String.valueOf(cell.getNumericCellValue()));
                            break;
                        default:
                            cellValues.add("UNKNOWN");
                    }
                }

                if (rowNum == 0) {
                    // edit the row number according to your excel file
                    timeSlots.addAll(cellValues.subList(1, cellValues.size()));
                    rowNum++;
                    continue;
                }

                // Check if the first cell is a day
                String firstCellValue = cellValues.get(0).trim();
                if (isDayOfWeek(firstCellValue)) {
                    currentDay = firstCellValue;
                    timetable.putIfAbsent(currentDay, new LinkedHashMap<>());
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
            case "TUES":
            case "WED":
            case "THUR":
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

    public byte[] saveTimetableToExcel(Map<String, Map<String, List<String>>> timetable) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Timetable");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Day / Time Slot");

        // Get all time slots
        Set<String> allTimeSlots = new LinkedHashSet<>();
        for (Map<String, List<String>> daySchedule : timetable.values()) {
            allTimeSlots.addAll(daySchedule.keySet());
        }

        // Add time slots to header
        int columnIndex = 1;
        for (String timeSlot : allTimeSlots) {
            headerRow.createCell(columnIndex++).setCellValue(timeSlot);
        }

        // Add data rows
        int rowIndex = 1;
        for (Map.Entry<String, Map<String, List<String>>> dayEntry : timetable.entrySet()) {
            Row dataRow = sheet.createRow(rowIndex++);
            dataRow.createCell(0).setCellValue(dayEntry.getKey());

            columnIndex = 1;
            for (String timeSlot : allTimeSlots) {
                Cell cell = dataRow.createCell(columnIndex++);
                List<String> classes = dayEntry.getValue().get(timeSlot);
                if (classes != null && !classes.isEmpty()) {
                    cell.setCellValue(String.join(", ", classes));
                }
            }
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        return bos.toByteArray();
    }
}
