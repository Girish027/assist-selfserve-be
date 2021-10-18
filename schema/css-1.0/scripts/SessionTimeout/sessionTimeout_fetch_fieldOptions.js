function translate(values) {
    values = JSON.parse(values);
    var items = values.data.items;
    var properties = {
        list: []
    };
    try {
        items.forEach(function(item) {
            var enumNamesValues = {
                fieldName: item.varName,
                operators: ["equal", "not_equal"],
                fieldType: "text"
            };
            properties.list.push(enumNamesValues);
        })
    } catch (e) {}
    return JSON.stringify(properties);
}