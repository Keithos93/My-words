
window.addEventListener('DOMContentLoaded', () => {
    console.log("*********** Movie page ***********");


    const movieCard = document.querySelector("#movie-card");
    const moviePosterElement = movieCard.querySelector("#poster");
    const movieTitleElement = movieCard.querySelector("#title")
    const movieOverviewElement = movieCard.querySelector("#overview");
    const moviePopularityElement = movieCard.querySelector("#popularity"); 

    const pseudonymeField = document.querySelector("#pseudonyme");
    const commentField = document.querySelector("#comment");
    const commentBtn = document.querySelector("#comment-btn");
    const commentsSection = document.querySelector("#comments");


    // Objet global
    let parsedMovie, message, pseudnonyme;

    const searchParams = new URLSearchParams(window.location.search);
    let movieId = searchParams.get("id");


    let movie = localStorage.getItem(`movie-${movieId}`);


    console.log(movie);



    if (movie === null) {
        fetch(`https://api.themoviedb.org/3/movie/${movieId}?language=en-US&api_key=4d56c14d98d344e437797c271b694f09`)
        .then(response => response.json())
        .then(data => {
            parsedMovie = data;
            parsedMovie.comments = [];
            localStorage.setItem(`movie-${movieId}`, JSON.stringify(parsedMovie));
            moviePosterElement.src = `https://image.tmdb.org/t/p/w500/${parsedMovie.poster_path}`;
            movieTitleElement.textContent = parsedMovie.title;
            movieOverviewElement.textContent = parsedMovie.overview;
            moviePopularityElement.textContent = parsedMovie.popularity;
        })
        .catch(error => console.log(error));
    } else {
        parsedMovie = JSON.parse(movie);
        moviePosterElement.src = `https://image.tmdb.org/t/p/w500/${parsedMovie.poster_path}`;
        movieTitleElement.textContent = parsedMovie.title;
        movieOverviewElement.textContent = parsedMovie.overview;
        moviePopularityElement.textContent = parsedMovie.popularity;
    }


    if (parsedMovie.comments) {
        commentsSection.innerHTML = ``;
        parsedMovie.comments.forEach(comment => {
            let commentElement = `<div>
                 <p>${comment.message}</p>
                 <p>- Par ${comment.pseudonyme}</p>
            </div>`
            commentsSection.innerHTML += commentElement
        });
    }


    pseudonymeField.addEventListener("input", (event) => {
        pseudnonyme = event.target.value;
        // comment.pseudnonyme = pseudnonyme;
    });


    commentField.addEventListener("input", (event) => {
        message = event.target.value;
        // comment.message = message;
    });

    commentBtn.addEventListener("click", (event) => {
        event.preventDefault();

        // Logique pour enregistrer l'ajout d'un nouveau commentaire dans le local storage
        let comment = {
            "pseudonyme": pseudnonyme,
            "message": message
        };

        parsedMovie.comments.push(comment);
        localStorage.setItem(`movie-${movieId}`, JSON.stringify(parsedMovie));

        commentsSection.innerHTML = ``;

        parsedMovie.comments.forEach(comment => {
            let commentElement = `<div>
                 <p>${comment.message}</p>
                 <p>- Par ${comment.pseudonyme}</p>
            </div>`
            commentsSection.innerHTML += commentElement
        });


    });



})