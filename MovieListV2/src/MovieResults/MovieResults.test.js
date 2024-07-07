import { render, screen } from '@testing-library/react';
import MovieResults from './MovieResults';

describe('Test movie results component', () => {    
    it('renders results correctly', () => {
        const id = "cmpId";
        const page = 1;
        const title1 = "Title1";
        const year1 = "2000";
        const imdbId1 = "1";
        const image1 = "srcLink1"
        const title2 = "Title2";
        const year2 = "2002";
        const imdbId2 = "2";
        const image2 = "N/A"
        const movies = {Search: [{Title: title1, Year: year1, imdbID: imdbId1, Poster: image1}, 
            {Title: title2, Year: year2, imdbID: imdbId2, Poster: image2}], 
            totalResults: 2, Response: "True"};
        
        //  render the component
        render(<MovieResults id={id} movies={movies} page={page}/>);
        const resultsElement = screen.queryByTestId(id);
        expect(resultsElement).toBeInTheDocument();
        //  expect page number to be in the doc
        const currentPage = resultsElement.querySelector(".searchResultTitle");
        expect(currentPage).toHaveTextContent(page);
        //  expect two results to be in the doc
        const searchResults = resultsElement.querySelectorAll(".searchResult");
        expect(searchResults).toHaveLength(2);
        //  expect the first result to show an image tag and the correct title and year
        const firstSearchResult = searchResults[0];
        expect(firstSearchResult.querySelector(".moviePoster")).toBeTruthy();
        expect(firstSearchResult).toHaveTextContent(title1);
        expect(firstSearchResult).toHaveTextContent(year1);
        //  expect the second result to show NO image tag and the correct title and year
        const secondSearchResult = searchResults[1];
        expect(secondSearchResult.querySelector(".moviePoster")).toBeFalsy();
        expect(secondSearchResult).toHaveTextContent(title2);
        expect(secondSearchResult).toHaveTextContent(year2);
    });

    it('renders results correctly with no movies', () => {
        const id = "cmpId";
        const page = 1;
        const movies = {};
        
        //  render the component
        render(<MovieResults id={id} movies={movies} page={page}/>);
        const resultsElement = screen.queryByTestId(id);
        expect(resultsElement).toBeInTheDocument();
        //  expect page number to not be in the doc
        const currentPage = resultsElement.querySelector(".searchResultTitle");
        expect(currentPage).toBeFalsy();
        //  expect no results to be in the doc
        const searchResults = resultsElement.querySelectorAll(".searchResult");
        expect(searchResults).toHaveLength(0);
    });
});