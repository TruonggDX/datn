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
const menuItems = document.querySelectorAll('#main-nav .menu-item');

menuItems.forEach(item => {
    let timeout;

    item.addEventListener('mouseenter', () => {
        clearTimeout(timeout);
        item.querySelector('.submenu').style.display = 'block';
    });

    item.addEventListener('mouseleave', () => {
        timeout = setTimeout(() => {
            item.querySelector('.submenu').style.display = 'none';
        }, 100); // Thay đổi thời gian trễ nếu cần
    });
});