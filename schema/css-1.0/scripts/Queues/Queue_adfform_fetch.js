function translate(values, entity) {
    var uiData = {
        "tabsField": [{}, {
            "assistConfig": {}
        }]
    };
    var apiData = {
        "dispostionFormContent": ""
    };
    try {
        apiData = JSON.parse(values).data;
        uiData.tabsField[1].assistConfig.dispositionForm = apiData.dispostionFormContent;
    } catch (e) {
        uiData.tabsField[1].assistConfig.dispositionForm = "";
    }
    return JSON.stringify(uiData);
}