function translate(values) {
    var entities = [];
    var list = [];
    try {
        values = JSON.parse(values);
        list = values.data.items;
    } catch (e) {
        return e.toString();
    }
    list.forEach(function (item) {
        entities.push({
            name: item.key,
            label: item.userName,
            labelState: item.status
        });
    });
    var metaData = {
        "totalCount": values.data.count
    };
    return JSON.stringify({
        "data": entities,
        "metaData": metaData
    });
}
