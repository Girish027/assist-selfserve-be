function translate(values, entity, context, auxResponse) {
    try {
        values = JSON.parse(values);
        var auxResp = JSON.parse(auxResponse);
        var existingCards = auxResp.existingCards;
        var card = { enum: [""], enumNames: ["Select Card"] };
        var idIndex = -1;
        if (entity !== null) {
            auxResp.ids.forEach(function (item, index) {
                if (item === entity) { idIndex = index; }
            });
        };
        if (values) {
            values.forEach(function (item) {
                var count = 0;
                existingCards.forEach(function (data) {
                    if (data === item.artifactName) { count = count + 1; }
                });
                if (count === 0) {
                    card.enum.push(item.artifactName);
                    card.enumNames.push(item.artifactName);
                }
            });
        }
        if (idIndex !== -1) {
            card.enum.push(existingCards[idIndex]);
            card.enumNames.push(existingCards[idIndex]);
        }
    }
    catch (e) { } return JSON.stringify(card);
}