import '../App.css';
import React from 'react';

import Button from '../Button/Button';
import Input from '../Input/Input';
import MovieResults from '../MovieResults/MovieResults';
import SearchError from '../SearchError/SearchError';

/**
 * Component that allows the user to search for movies based on title
 * 
 * @returns 
 */
function MovieSearch() {
    //  search labels
    const SEARCH_BUTTON_LABEL = "Search";
    const SEARCH_INPUT_LABEL = "Search for a movie";
    const SEARCH_INPUT_PLACEHOLDER = "Search by movie title";
    //  pagination label
    const NEXT_BUTTON_LABEL = "Next";
    //  TODO: move token out of env files and into server side code before deploying 
    const OMDB_URL = "https://www.omdbapi.com/?apikey="+process.env.REACT_APP_NOT_SECRET_TOKEN+"&s=";
    const [searchTerm, setSearchTerm] = React.useState(''); //  String representing the search term
    const [movies, setMovies] = React.useState({}); //  movies object
    const [page, setPage] = React.useState(1);  //  page number used to page through search results

    /**
     * Method that calls fetch on the omdb api to return a list of movies given the search term
     * 
     * @param {Integer} pageTemp 
     */
    async function executeSearch(pageTemp){
        //  TODO: sanitize search term
        try {
            let response;
            if (typeof pageTemp === "number"){ 
                response = await fetch(OMDB_URL+searchTerm+"&page="+pageTemp);
            } else {    //  request coming from search button
                response = await fetch(OMDB_URL+searchTerm+"&page="+1);
                setPage(1); //  TODO: should be able to use page
            }
            setMovies(await response.json());
        }
        catch(error){
            //  TODO: handle the error correctly
        }
    }

    /**
     * Set the search term based on the input
     * 
     * @param {*} searchInput 
     */
    function handleSearchInputOnChange(searchInput){
        setSearchTerm(searchInput);
    };

    /**
     * Handle the user clicking next on pagination
     */
    async function pageNext(){
        let pageTemp = page+1;
        setPage(pageTemp);
        //setPage((prevPage) => prevPage + 1);  //  TODO: this should work!
        await executeSearch(pageTemp);
        //  set the focus on the input box
        document.getElementById("search").focus();
    };

    /**
     * Display either the search results or error from search
     * 
     * @returns 
     */
    function mainDisplay(){
        if (movies){
            if (movies.Response === "False"){
                return(<div><SearchError id="error" errorMessage={movies.Error} /></div>);
            } else if (movies.Response === "True"){
                return (<div><MovieResults id="results" movies={movies} page={page}/></div>);
            }
        }
    };

    /**
     * 
     * @returns 
     *  UI for pagination component
     *  TODO: look into react pagination
     */
    function pagination(){
        //  TODO: will have to keep adding count based on page
        //  will have to make Next not clickable
        //  TODO: the last clause movies.Search.length === 10 is a hack for now
        if (movies.Response === "True" && ((movies.totalResults > movies.Search.length) && (movies.Search.length === 10))){
            return (
                <div className="pagination">
                    <Button buttonId="nextButton" buttonLabel={NEXT_BUTTON_LABEL} onClickHandler={pageNext}></Button>
                </div>
            );
        }
    };

    /**
     * Display the main page that allows users to search movies based on title
     * Supports rudimentary pagination
     */
    return (
        <div>
            <div className="rowElement topPadding">
                <div className="greySurround">
                    <Input inputId="search" type="text" label={SEARCH_INPUT_LABEL} placeholder={SEARCH_INPUT_PLACEHOLDER} 
                        onChangeHandler={handleSearchInputOnChange}></Input>
                    <Button buttonId="searchButton" buttonLabel={SEARCH_BUTTON_LABEL}  onClickHandler={executeSearch}></Button>
                </div>
            </div>
            <div className="rowElement">
                {mainDisplay()}
                {pagination()}            
            </div>
        </div>
    );
}

export default MovieSearch;