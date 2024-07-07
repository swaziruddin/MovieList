import '../App.css';
import React from 'react';

/**
 * Generic component for inputs
 * So far, only supports inputs of type text, can be extended to other input types as needs arise
 * 
 * @param {*} param0 
 * @returns 
 */
function Input({inputId, type, label, placeholder, onChangeHandler}){

    return (
        <div className="inline-block-child">
            {label}&nbsp;   {/* TODO: can be extended to support various label positioning as well as styles */}
            <input data-testid={inputId} id={inputId} type={type} placeholder={placeholder} 
                onChange={(e)=>{onChangeHandler(e.target.value)}}></input>
        </div>
    );
}

export default Input;