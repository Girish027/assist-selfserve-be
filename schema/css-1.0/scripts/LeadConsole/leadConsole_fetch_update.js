function translate(values) { 
    values = JSON.parse(values)[0];
    var uiData = {
        key: values.key, 
        time:{ 
            hours: values.hours, 
            minutes: values.minutes, 
            meridian: values.meridiem 
        },
        applicationValue: values.applicationValue
    }; 
    return JSON.stringify(uiData); 
}