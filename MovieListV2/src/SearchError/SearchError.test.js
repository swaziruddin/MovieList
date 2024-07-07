import { render, screen } from '@testing-library/react';
import SearchError from './SearchError';

describe('Test search error component', () => {    
    it('renders error component', () => {
        const id = "cmpId";
        const errorMessage = "some error";

        render(<SearchError id={id} errorMessage={errorMessage}/>);
        const errorElement = screen.queryByTestId(id);
        expect(errorElement).toBeInTheDocument();
        expect(errorElement).toHaveTextContent(errorMessage);
    });
});