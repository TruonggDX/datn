$(document).ready(function() {
    $(window).scroll(function() {
        if ($(this).scrollTop() > 20) {
            $('#back-to-top').fadeIn();
        } else {
            $('#back-to-top').fadeOut();
        }
    });
    $('#back-to-top').click(function() {
        $('html, body').animate({scrollTop: 0}, 800);
        return false;
    });
});

document.querySelectorAll('.menu li').forEach(function(menuItem) {
    menuItem.addEventListener('mouseover', function() {
        const submenu = this.querySelector('.submenu-container');
        if (submenu) {
            submenu.style.display = 'block';
            const menuItemRect = this.getBoundingClientRect();
            const submenuRect = submenu.getBoundingClientRect();

            // Calculate the top position for submenu
            const topOffset = menuItemRect.bottom + window.scrollY;
            submenu.style.top = `${topOffset}px`;

            // Center submenu horizontally
            submenu.style.left = `50%`;
            submenu.style.transform = `translateX(-50%)`;
        }
    });

    menuItem.addEventListener('mouseout', function() {
        const submenu = this.querySelector('.submenu-container');
        if (submenu) {
            submenu.style.display = 'none';
        }
    });
});