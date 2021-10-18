function translate(values) {
    values = JSON.parse(values);
    var items = values.data.items;
    var entities = [];
    try {
        items.forEach(function (item) {
            if (item.commandName.indexOf('/f') !== -1) { entities.push({ name: item.key, label: item.commandName.replace('/f ', '') }); }
        })
    } catch (e) { } return JSON.stringify(entities);
}
