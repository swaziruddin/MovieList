import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Input from './Input';

describe('Test input component', () => {
    afterEach(() => {
        jest.clearAllMocks();
    });
    
    it('renders input for text', () => {
        const inputLabel = "Input Label";
        const inputType = "text";
        const inputId = "inputId";

        render(<Input inputId={inputId} type={inputType} label={inputLabel}/>);
        const inputElement = screen.queryByTestId(inputId);
        expect(inputElement).toBeInTheDocument();
        const labelElement = screen.getByText(inputLabel);
        expect(labelElement).toBeInTheDocument();
    });

    it('tests change handler for text input type', async() => {
        const inputType = "text";
        const inputId = "inputId";
        const mockOnChange = jest.fn();
        
        render(<Input inputId={inputId} type={inputType} onChangeHandler={mockOnChange}/>);
        const inputElement = screen.queryByTestId(inputId);
        userEvent.type(inputElement, "s");
        await expect(mockOnChange).toHaveBeenCalledTimes(1);
    });
});