document.getElementById('searchButton').addEventListener('click', function() {
    const searchInputContainer = document.getElementById('searchInputContainer');
    const isHidden = searchInputContainer.classList.contains('hidden');

    if (isHidden) {
        searchInputContainer.classList.remove('hidden');
        searchInputContainer.classList.add('animate-slide-down');
    } else {
        searchInputContainer.classList.add('hidden');
        searchInputContainer.classList.remove('animate-slide-down');
    }
});