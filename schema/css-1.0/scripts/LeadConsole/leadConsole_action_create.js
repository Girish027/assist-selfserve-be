function translate(pageData, entityId, clientId, accountId, env) {
     pageData = JSON.parse(pageData).createLeadConsole_p0;
     var apiData = {};
     if (pageData.propertyType = "Reset Time") {
         apiData.propertyType = "RESET_TIME";
     }
     else {
         apiData.propertyType = pageData.propertyType;
     }
     apiData.key = pageData.key;
     apiData.hours = pageData.time.hours;
     apiData.minutes = pageData.time.minutes;
     apiData.meridiem = pageData.time.meridian;
     return JSON.stringify(apiData);
 }