function translate(pageData) {
    var uiData = JSON.parse(pageData).createBulkAgentsLive_p0;
    var uploadOption = uiData.stepTwo.selectType;
    return uploadOption;
}
