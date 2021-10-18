function translate(values) {
    values = JSON.parse(values);
    var uiData = values.items;
    if (Array.isArray(uiData.enumValues) && uiData.enumValues.length == 1 && uiData.enumValues[0] == '') {
        uiData.enumValues = []
    }
    return JSON.stringify(uiData);
}