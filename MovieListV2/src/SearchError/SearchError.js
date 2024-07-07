import '../App.css';
import React from 'react';

/**
 * Component to display erros from server while executing searches
 * Could be generalized to an error component 
 * 
 * @param {*} param0 
 * @returns 
 */
function SearchError({id, errorMessage}){
    return (
        <div data-testid={id}>
            Error: {errorMessage}
        </div>
    );
}

export default SearchError;