document.addEventListener('DOMContentLoaded', (event) => {
    const stars = document.querySelectorAll('.star');
    let rating = 0;

    stars.forEach(star => {
        star.addEventListener('click', () => {
            rating = star.getAttribute('data-value');
            stars.forEach(s => {
                s.classList.remove('selected');
                if (s.getAttribute('data-value') <= rating) {
                    s.classList.add('selected');
                }
            });
            console.log(`Calificación: ${rating}`);
        });
    });
});