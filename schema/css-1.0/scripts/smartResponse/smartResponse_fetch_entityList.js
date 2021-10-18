function translate(values) {
    try {
        values = JSON.parse(values);
        var entities = [];
        if (values.data !== undefined) {
            var items = values.data.items;
            items.forEach(function(item) {
                entities.push({
                    name: item.key,
                    label: item.smartresponseText
                });
            });
        }
    } catch (e) {}
    return JSON.stringify(entities);
}