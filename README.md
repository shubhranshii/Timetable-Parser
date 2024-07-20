# Timetable-Parser

This service allows users to upload a university timetable Excel file, filter the timetable based on batch and electives, and view the timetable in an editable format on a web interface. Users can also change the theme, manually edit the timetable by clicking on cells, and save the edited timetable as an Excel file.

## Features
1. **Upload Timetable**: Upload an Excel file containing the university timetable.
2. **Filter Timetable**: Filter the timetable based on batch.
3. **Editable Timetable**: View and edit the timetable directly in the web interface.
4. **Theme Selection**: Change the theme of the timetable display.
5. **Save as Excel**: Save the edited timetable as an Excel file.

## Setup Instructions

### Prerequisites
- Java 8 or higher
- Maven
- Spring Boot

### Steps to Setup

1. **Clone the Repository**
    ```bash
    git clone https://github.com/shubhranshii/Timetable-Parser.git
    cd timetable-Parser
    ```

2. **Build the Project**
    ```bash
    mvn clean install
    ```

3. **Run the Application**
    ```bash
    mvn spring-boot:run
    ```

4. **Access the Application**
   Open your browser and go to `http://localhost:8080/upload`.

### Note on Customization

Based on the Excel file format you are using, you might need to edit the code in the `TimetableParser` class. Specifically, you might need to adjust the row number (`rowNum ==`) to match the row where the time slots are located in your Excel file.

## Usage

### Upload Timetable

1. **Select the Excel file**: Click on the "Choose File" button to upload your timetable file.
2. **Upload**: Click on the "Upload" button to process the file.

### Filter Timetable

After uploading the timetable, you can filter it based on batch.

### Editable Timetable

You can manually edit the timetable by clicking on any cell. Changes will be automatically saved in the displayed timetable.

### Change Theme

Select a theme from the dropdown to change the appearance of the timetable.

### Save as Excel

After editing, you can save the timetable as an Excel file by clicking on the "Save Timetable" button.

## Example Timetable Structure

Your Excel file should have the following structure:

| Day / Time Slot | 9 - 9.50 AM | 10 - 10.50 AM | 11 - 11.50 AM | 12 NOON - 12.50 PM | 1 - 1.50 PM | 2 - 2.50 PM | 3 - 3.50 PM | 4 - 4.50 PM |
|-----------------|-------------|---------------|---------------|--------------------|-------------|-------------|-------------|-------------|
| MON             | ...         | ...           | ...           | ...                | ...         | ...         | ...         | ...         |
| TUES            | ...         | ...           | ...           | ...                | ...         | ...         | ...         | ...         |
| WED             | ...         | ...           | ...           | ...                | ...         | ...         | ...         | ...         |
| THUR            | ...         | ...           | ...           | ...                | ...         | ...         | ...         | ...         |
| FRI             | ...         | ...           | ...           | ...                | ...         | ...         | ...         | ...         |
| SAT             | ...         | ...           | ...           | ...                | ...         | ...         | ...         | ...         |
| SUN             | ...         | ...           | ...           | ...                | ...         | ...         | ...         | ...         |

Ensure that the day names and time slots match the structure expected by the parser.

## Contributing

Feel free to open issues or submit pull requests if you find any bugs or have suggestions for new features.

---
