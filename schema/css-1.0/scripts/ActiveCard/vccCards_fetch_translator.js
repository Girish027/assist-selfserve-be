function translate(values) {
    values = JSON.parse(values);
    var card = { enum: [""], enumNames: ["Select Card"] };
    values.forEach(function (item) {
        card.enum.push(item.artifactName);
        card.enumNames.push(item.artifactName);
    }); return JSON.stringify(card);
}
