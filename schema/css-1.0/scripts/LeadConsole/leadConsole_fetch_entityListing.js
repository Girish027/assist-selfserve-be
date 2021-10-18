function translate(values) {
    values = JSON.parse(values);
    var items = values.data.items;
    var entities = [];
    try {
        items.forEach(function(item) {
            entities.push({
                name: item.key,
                label: item.applicationValue
            });
        })
    } catch (e) {}
    return JSON.stringify(entities);
}