import '../App.css';
import React from 'react';

/**
 * Renders list of movies returned from api
 * 
 * @param {*} param0 
 * @returns 
 */
function MovieResults({id, movies, page}) {
    const NO_MOVIE_POSTER_AVAIL = "N/A";

    /**
     * Renders movie post if present
     * 
     * @param {*} moviePosterPath 
     * @param {*} movieTitle 
     * @returns 
     */
    function renderMoviePoster(moviePosterPath, movieTitle){
        if (moviePosterPath !== NO_MOVIE_POSTER_AVAIL){
            return (
                <div>
                    <img src={moviePosterPath} alt={movieTitle} className="moviePoster"/>
                </div>
            );
        }
    };

    /**
     * Renders page heading that shows which page of how many results are displayed
     * 
     */
    function renderSearchResultsTitle(){
        if (movies && movies.Search){
            return (
                <div className="searchResultTitle">Page {page} of {movies.totalResults} results</div>
            );
        }
    };

    return(
        <div data-testid={id}>
            {renderSearchResultsTitle()}
            {movies && movies.Search && movies.Search.map(movie=>{
                return(
                    <div key={movie.imdbID} className="searchResult"> 
                        {renderMoviePoster(movie.Poster, movie.Title)}
                        {movie.Title}
                        <br></br>
                        Year of Release: {movie.Year}
                    </div>
                )
            })}
        </div>
    );
}

export default MovieResults;