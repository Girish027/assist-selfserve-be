function translate(values) {
    values = JSON.parse(values);
    var items = values.data.items;
    var entities = [];
    items.forEach(function (item) {
        entities.push({ name: item.key, label: item.piiMaskName });
    });
    return JSON.stringify(entities);
}