function translate(values, entity, context) {
    try {
        context  = JSON.parse(context);
        var date = new Date();
        var dateString = ("0" + date.getDate()).slice(-2) + ("0" + (date.getMonth() + 1)).slice(-2) + ("" + date.getYear()).slice(-2)
        var timeString = ("0" + date.getHours()).slice(-2) + "" + date.getMinutes() + "" + date.getSeconds();
        var accountId = "BulkUpload" + "_" + dateString + "_" + timeString;
        var uiData = { "accountId": accountId };
    } catch (e) {}
    return JSON.stringify(uiData);
}
