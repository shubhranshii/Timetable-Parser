function applyTheme() {
            const theme = document.getElementById('theme').value;
            const table = document.getElementById('timetable');
            table.className = 'table table-bordered ' + theme;
        }

document.addEventListener('DOMContentLoaded', function() {
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