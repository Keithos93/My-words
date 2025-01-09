// search.js

document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('searchInput');
    const searchResultsContainer = document.getElementById('searchResults');
    const loadMoreButton = document.getElementById('loadMoreResults');

    let searchPage = 1; // Numéro de la page pour charger plus de résultats de recherche

    function fetchSearchResults() {
        const searchTerm = searchInput.value;
        // Assurez-vous d'utiliser la bonne API ou endpoint pour la recherche
        const searchUrl = `https://api.themoviedb.org/3/search/movie?query=${searchTerm}&page=${searchPage}&api_key=4d56c14d98d344e437797c271b694f09`;

        fetch(searchUrl)
            .then(response => response.json())
            .then(data => displaySearchResults(data.results));
    }

    function displaySearchResults(results) {
        // Effacer les résultats précédents
        searchResultsContainer.innerHTML = '';

        results.forEach(result => {
            const resultElement = document.createElement('div');
            resultElement.innerHTML = `
                <img src="https://image.tmdb.org/t/p/w500/${result.poster_path}" alt="${result.title}">
                <h3>${result.title}</h3>
                <a href="./movie.html?id=${result.id}">En savoir plus</a>
            `;
            searchResultsContainer.appendChild(resultElement);
        });
    }

    function loadMoreSearchResults() {
        searchPage++;
        fetchSearchResults();
    }

    // Ajout d'un gestionnaire d'événement pour la recherche en temps réel
    searchInput.addEventListener('input', fetchSearchResults);

    // Ajout d'un gestionnaire d'événement pour le bouton de chargement
    loadMoreButton.addEventListener('click', loadMoreSearchResults);

    // Appel initial pour afficher les résultats de recherche lors du chargement de la page
    fetchSearchResults();
});
