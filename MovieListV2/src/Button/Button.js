import '../App.css';
import React from 'react';

/**
 * Generic button component for app
 * 
 * @param {*} param0 
 * @returns 
 */
function Button({buttonId, buttonLabel, onClickHandler}){

    return (
        <div className="inline-block-child">
            <button data-testid={buttonId} id={buttonId} onClick={onClickHandler}>{buttonLabel}</button>
        </div>
    );
}

export default Button;