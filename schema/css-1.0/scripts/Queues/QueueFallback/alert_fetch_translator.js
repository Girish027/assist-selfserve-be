function translate(apiData, entity, context, auxResponse) {
  try {
    var val = JSON.parse(apiData);
    var uiData;
    var auxResp = JSON.parse(auxResponse);
    if (val.length > 0) {
      var alert = val[0];
      var condition = alert.triggerConditions.conditions;
      var operators = { GT: ">", EQ: "==", LT: "<" };
      var ruleBuilderdata = "";
      var syncRuleBuilderData = "(SLA15>90)";
      var asyncRuleBuilderData = "(SLA60>90)";
      if (condition.length) {
        var oper = condition[0].operator;
        var value = parseInt(condition[0].value);
        ruleBuilderdata = "(" + condition[0].attribute.replace(/[{()}]/g, "") + operators[oper] + value + ")";
      }
      if (auxResp === 'LIVE_CHAT') {
        syncRuleBuilderData = ruleBuilderdata;
      } else {
        asyncRuleBuilderData = ruleBuilderdata
      }
      uiData = {
        "tabsField": [
          {},
          {},
          {
            "queuefallback": {
              "alertId": alert.alertId || "",
              "setQueueFallbackEnabled": alert.isAlertEnabled || false,
              "name": alert.alertName || "",
              "description": alert.alertDescription || "",
              "addcond": [
                {
                  "syncRuleBuilder": syncRuleBuilderData,
                  "asyncRuleBuilder": asyncRuleBuilderData,
                  "fallbackQueuesLive": alert.alertActionData.values ? alert.alertActionData.values[0] : "",
                  "fallbackQueuesMsg": alert.alertActionData.values ? alert.alertActionData.values[0] : ""
                }
              ]
            }
          }
        ]
      };
    } else {
      uiData = {
        "tabsField": [
          {},
          {},
          {
            "queuefallback": {
              "alertId": "",
              "setQueueFallbackEnabled": false,
              "name": "",
              "description": "",
              "addcond": [
                {
                  "syncRuleBuilder": "(SLA15>90)",
                  "asyncRuleBuilder": "(SLA60>90)",
                  "fallbackQueuesLive": "",
                  "fallbackQueuesMsg": ""
                }
              ]
            }
          }
        ]
      };
    }
  }
  catch (e) { }
  return JSON.stringify(uiData);
}