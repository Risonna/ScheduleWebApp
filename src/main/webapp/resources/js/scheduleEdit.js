function toggleNestedOptions() {
    var nestedOptions = document.getElementById('nestedOptions');
    var computedStyle = window.getComputedStyle(nestedOptions);

    if (computedStyle.display === 'none') {
        nestedOptions.style.display = 'block';
    } else {
        nestedOptions.style.display = 'none';
    }
}