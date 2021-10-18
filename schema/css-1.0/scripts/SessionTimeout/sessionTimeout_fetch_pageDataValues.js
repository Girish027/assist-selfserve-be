function translate(values) { 
    values = JSON.parse(values);
    var uiData = { 
        key: values.key,
        expression: values.expression, 
        timeout: values.timeOut
    }; 
    return JSON.stringify(uiData); 
}