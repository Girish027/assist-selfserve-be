function translate(pageData) {
    var uiData = JSON.parse(pageData).createBulkAgentsTest_p0;
    var uploadOption = uiData.stepThree.selectType;
    return uploadOption;
}