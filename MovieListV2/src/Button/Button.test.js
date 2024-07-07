import { render, screen } from '@testing-library/react';
import Button from './Button';

describe('Test button component', () => {
    afterEach(() => {
        jest.clearAllMocks();
    });
    
    it('renders button', () => {
        const buttonLabel = "Button Label";
        const buttonId = "buttonId";

        render(<Button buttonId={buttonId} buttonLabel={buttonLabel}/>);
        const buttonElement = screen.queryByTestId(buttonId);
        expect(buttonElement).toBeInTheDocument();
        expect(buttonElement.textContent).toBe(buttonLabel);
    });

    it('tests button on click handler', () => {
            const buttonId = "buttonId";
            const mockOnClick = jest.fn();
        
            render(<Button buttonId={buttonId} onClickHandler={mockOnClick}/>);
            const buttonElement = screen.queryByTestId(buttonId);
            buttonElement.click();
            expect(mockOnClick).toHaveBeenCalledTimes(1);
    });

});