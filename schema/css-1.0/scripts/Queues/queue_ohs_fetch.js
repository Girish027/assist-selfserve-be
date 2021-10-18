function translate(apiData) {
    var val = JSON.parse(JSON.parse(apiData).data.entity);
    var ea = val.entityAttributes;
    var ebd = val.entityBaseData;
    var uiData = {
        "tabsField": [
            {
                "queueList": ebd.entityId,
                "queueName": ebd.entityDisplayName,
                "queueId": ebd.entityId,
                "queueDesc": ea.queueDesc,
                "queueTags": []
            },
            {
                "assistConfig": {
                    "queueType": ea.queueType
                }
            },
            {
                "queuefallback": {
                    "setQueueFallback": "Set the fallback queues for " + ebd.entityDisplayName,
                    "queueType": ea.queueType
                }
            }
        ]
    };
    if (ea.tags && ea.tags.split(",") !== "") { 
        uiData.tabsField[0].queueTags = ea.tags.split(","); 
    }
    return JSON.stringify(uiData);
}