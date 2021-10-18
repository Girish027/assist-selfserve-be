function translate(values) {
    values = JSON.parse(values);
    var items = values.items;
    items.commandName = items.commandName.replace('/f ', '');
    items.enabled = items.enabled.toString();
    return JSON.stringify(items);
}
