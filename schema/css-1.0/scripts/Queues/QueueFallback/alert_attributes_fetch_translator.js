function translate(values) {
  try {
    values = JSON.parse(values);
    var properties = {
      list: []
    };
    var operator = { GT: "greater", EQ: "equal", LT: "less" };
    var attributeType = { INTEGER: "number", STRING: "text" };
    var placeholder = "Values entered will be taken in percentage";
    values.forEach(function (item) {
      if (item.properties.queueType && item.properties.queueType == "SYNC") {
        var value = [];
        item.operatorsAllowed.forEach(function (op) {
          value.push(operator[op])
        });
        var key = item.attributeName.replace(/[{()}]/g, "");
        var alertAttributeType = attributeType[item.alertAttributeValueType];
        var data = '(' + key + '>' + 90 + ')';
        var enumNamesValues = {
          key: key,
          fieldName: item.attributeName,
          operators: value,
          fieldType: alertAttributeType,
          placeholder: placeholder,
          formData: data
        };
        properties.list.push(enumNamesValues);
      }
    })
  }
  catch (e) { }
  return JSON.stringify(properties);
}
