function applyTheme() {
            const theme = document.getElementById('theme').value;
            const table = document.getElementById('timetable');
            table.className = 'table table-bordered ' + theme;
        }

document.addEventListener('DOMContentLoaded', function() {

    function saveTimetable() {
        const table = document.getElementById('timetable');
        const timetableData = {};

        // Extract table headers (time slots)
        const headers = Array.from(table.querySelectorAll('thead th')).slice(1).map(th => th.textContent.trim());

        // Extract table data
        table.querySelectorAll('tbody tr').forEach(row => {
            const day = row.querySelector('td').textContent.trim();
            timetableData[day] = {};

            Array.from(row.querySelectorAll('td')).slice(1).forEach((cell, index) => {
                timetableData[day][headers[index]] = cell.textContent.trim();
            });
        });

        // Send data to server
        fetch('/save-timetable', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(timetableData),
        })
        .then(response => {
            if (response.ok) {
                return response.blob();
            }
            throw new Error('Network response was not ok.');
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.style.display = 'none';
            a.href = url;
            a.download = 'timetable.xlsx';
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            alert('Timetable saved successfully! Check your downloads folder.');
        })
        .catch((error) => {
            console.error('Error:', error);
            alert('Error saving timetable');
        });
    }

    document.getElementById('saveButton').addEventListener('click', saveTimetable);

    const saveButton = document.getElementById('saveButton');
        if (saveButton) {
            saveButton.addEventListener('click', saveTimetable);
        } else {
            console.error('Save button not found in the DOM');
        }

    const table = document.getElementById('timetable');
    const editableCells = table.querySelectorAll('.editable-cell');

    editableCells.forEach(cell => {
        cell.setAttribute('contenteditable', 'true');

        // Optional: Add event listeners for better user experience
        cell.addEventListener('focus', function() {
            if (this.textContent.trim() === '___') {
                this.textContent = '';
            }
        });

        cell.addEventListener('blur', function() {
            if (this.textContent.trim() === '') {
                this.textContent = '___';
            }
        });
    });
});

