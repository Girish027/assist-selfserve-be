function translate(pageData, entityId, clientId, accountId, env, prefetch) {
  try {
    pageData = JSON.parse(pageData).createQueues_p0;
    var fieldName = { SLA15: "SLA(15)", SLA60: "SLA(60)" };
    var word1 = pageData.tabsField[1].assistConfig.queueType === 'LIVE_CHAT' ? "SLA15" : "SLA60";
    var apiPayload = [
      {
        "action": "add",
        "data": {
          "alertName": "alertName",
          "alertDescription": "alertDesc",
          "executionWindow": "*/15****",
          "restorePercentage": 0,
          "featureName": "QUEUE_FALLBACK",
          "entityType": "QUEUE",
          "alertType": "TRIGGER",
          "alertAction": "NOTIFY",
          "alertActionData": {
            "actionEntity": "QUEUE",
            "values": []
          },
          "isAlertEnabled": false,
          "version": 0,
          "triggerConditions": {
            "match": "ALL",
            "conditions": [
              {
                "attribute": fieldName[word1],
                "type": "STRING",
                "operator": "GT",
                "value": "90"
              }
            ]
          }
        }
      }
    ];
    if (pageData.tabsField[2].queuefallback) {
      var uiData = pageData.tabsField[2].queuefallback;
      var str = "";
      var word2 = "";
      var data = [];
      var fallbackQueues = [];
      var operators = { ">": "GT", "==": "EQ", "<": "LT" };
      var oper = [">", "<", "=="];
      var operString = "";
      var index;
      if (uiData.addcond) {
        if (uiData.queueType == "LIVE_CHAT") {
          str = uiData.addcond[0].syncRuleBuilder;
          fallbackQueues.push(uiData.addcond[0].fallbackQueuesLive)
        } else {
          str = uiData.addcond[0].asyncRuleBuilder;
          fallbackQueues.push(uiData.addcond[0].fallbackQueuesMsg)
        }
      } else {
        str = uiData.queueType == "LIVE_CHAT" ? "(SLA15>90)" : "(SLA60>90)";
      }
      var length = str.length - 1;
      oper.forEach(function (item) {
        if (str.search(item) != -1) {
          index = str.indexOf(item);
          operString = item;
        }
      });
      word1 = str.slice(1, index);
      word2 = str.slice(index + (operString.length), length);
      data = [
        {
          "attribute": fieldName[word1],
          "type": "STRING",
          "operator": operators[operString],
          "value": word2
        }
      ];

      apiPayload[0].data.alertName = uiData.name || "alertName";
      apiPayload[0].data.alertDescription = uiData.description || "alertDesc";
      apiPayload[0].data.isAlertEnabled = uiData.setQueueFallbackEnabled || false;
      apiPayload[0].data.alertActionData.values = fallbackQueues;
      apiPayload[0].data.triggerConditions.conditions = data;
    }
  }
  catch (e) { }
  return JSON.stringify(apiPayload);
}