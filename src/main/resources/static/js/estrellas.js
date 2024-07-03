document.querySelectorAll('.rating').forEach(rating => {
    const stars = rating.querySelectorAll('.star');
    const averageDisplay = rating.nextElementSibling.querySelector('.average-value');
    let totalRatings = 0;
    let numRatings = 0;

    stars.forEach(function(star, index) {
        star.addEventListener('click', function () {
            let ratingValue = stars.length - index;
            totalRatings += ratingValue;
            numRatings += 1;

            // Update stars display
            for (let i = 0; i <= index; i++) {
                stars[i].classList.add('checked');
            }
            for (let i = index + 1; i < stars.length; i++) {
                stars[i].classList.remove('checked');
            }

            // Calculate and display average rating
            const averageRating = totalRatings / numRatings;
            averageDisplay.textContent = averageRating.toFixed(1);
        });
    });
});