// public/index.js (votre fichier client-side JavaScript)

document.addEventListener('DOMContentLoaded', () => {
  const trendingMoviesContainer = document.getElementById('trendingMovies');
  const url = "https://api.themoviedb.org/3/trending/movie/day?language=en-US&api_key=4d56c14d98d344e437797c271b694f09";
  let trendingPage = 1; // Numéro de la page pour charger plus de films en tendance
  
  function fetchTrendingMovies() {
      fetch(`${url}&page=${trendingPage}`)
          .then(response => response.json())
          .then(data => {
              if (data.results.length > 0) {
                  displayTrendingMovies(data.results);
                  trendingPage++;
              }
          });
  }

  function displayTrendingMovies(movies) {
      movies.forEach(movie => {
          const movieElement = document.createElement('div');
          movieElement.innerHTML = `
              <img src="https://image.tmdb.org/t/p/w500/${movie.poster_path}" alt="${movie.title}">
              <h3>${movie.title}</h3>
              <p>Date de sortie: ${movie.release_date}</p>
              <a href="./movie.html?id=${movie.id}">En savoir plus</a>
          `;
          trendingMoviesContainer.appendChild(movieElement);
      });
  }

  function loadMoreIfAtBottom() {
      const { scrollTop, scrollHeight, clientHeight } = document.documentElement;

      if (scrollTop + clientHeight >= scrollHeight - 10) {
          fetchTrendingMovies();
      }
  }

  // Ajout de l'événement scroll pour déclencher le chargement infini
  window.addEventListener('scroll', loadMoreIfAtBottom);

  // Appel initial pour afficher les films en tendance lors du chargement de la page
  fetchTrendingMovies();
});

function loadMoreTrendingMovies() {
  // Incrémentez le numéro de page et appelez la fonction fetchTrendingMovies() à nouveau
  trendingPage++;
  fetchTrendingMovies();
}
