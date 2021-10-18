function translate(values) {
    try {
        values = JSON.parse(values);
        var entities = { existingCards: [], ids: [] };
        if (values) {
            var items = values.data.items;
            items.forEach(function (item) {
                if (item.commandName.indexOf('/f') !== -1) {
                    entities.existingCards.push(item.commandName.replace('/f ', ''));
                    entities.ids.push(item.key);
                }
            })
        }
    }
    catch (e) { } return JSON.stringify(entities);
}