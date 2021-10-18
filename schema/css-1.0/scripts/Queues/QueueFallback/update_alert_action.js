function translate(pageData, entityId, clientId, accountId, env, prefetch) {
  try {
    pageData = JSON.parse(pageData).updateQueues_p0;
    var uiData = pageData.tabsField[2].queuefallback;
    var str = "";
    if (uiData.queueType == "LIVE_CHAT") {
      str = uiData.addcond[0].syncRuleBuilder || "SLA15>90";
    } else {
      str = uiData.addcond[0].asyncRuleBuilder || "SLA60>90";
    }
    var fallbackQueues = [];
    var operators = { ">": "GT", "==": "EQ", "<": "LT" };
    var oper = [">", "<", "=="];
    var fieldName = { SLA15: "SLA(15)", SLA60: "SLA(60)" };
    var operString = "";
    var length = str.length - 1;
    var index;
    oper.forEach(function (item) {
      if (str.search(item) != -1) {
        index = str.indexOf(item);
        operString = item;
      }
    });
    var word1 = str.slice(1, index);
    var word2 = str.slice(index + (operString.length), length);
    var data = [{
      "attribute": fieldName[word1],
      "type": "STRING",
      "operator": operators[operString],
      "value": word2
    }];
    if (uiData.queueType == "LIVE_CHAT") {
      if (uiData.addcond[0].fallbackQueuesLive) {
        fallbackQueues.push(uiData.addcond[0].fallbackQueuesLive);
      }
    } else {
      if (uiData.addcond[0].fallbackQueuesMsg) {
        fallbackQueues.push(uiData.addcond[0].fallbackQueuesMsg)
      }
    }
    var apiPayload = [
      {
        "action": "add",
        "data": {
          "alertId": "",
          "alertName": uiData.name || "alertName",
          "alertDescription": uiData.description || "alertDesc",
          "executionWindow": "*/15****",
          "restorePercentage": 0,
          "featureName": "QUEUE_FALLBACK",
          "entityType": "QUEUE",
          "alertType": "TRIGGER",
          "alertAction": "NOTIFY",
          "alertActionData": {
            "actionEntity": "QUEUE",
            "values": fallbackQueues || []
          },
          "isAlertEnabled": uiData.setQueueFallbackEnabled || false,
          "version": 0,
          "triggerConditions": {
            "match": "ALL",
            "conditions": data
          }
        }
      }
    ];
    if (uiData.alertId) {
      apiPayload[0].action = "update";
      apiPayload[0].data.alertId = uiData.alertId;
    }
    else {
      delete apiPayload[0].data.alertId;
    }
  }
  catch (e) { }
  return JSON.stringify(apiPayload);
}