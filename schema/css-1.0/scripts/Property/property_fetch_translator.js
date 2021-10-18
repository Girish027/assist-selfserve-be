function translate(values) {
    values = JSON.parse(values);
    values.scope = {};
    if (values.scopeType === 'queue') {
        values.scope.queueScope = values.scopeId;
    } else {
        values.scope.accountId = values.scopeId;
    }
    values.scope.scopeType = values.scopeType;
    values.propertyType = values.varGroup;
    values.order = values.order;
    values.editableFlag = values.editableFlag.toString();
    values.mandatoryFlag = values.mandatoryFlag.toString();
    values.maskableFlag = values.maskableFlag.toString();
    if (values.validatorId !== undefined) {
        values.propertyType = values.validatorId;
    }
    else { values.propertyType = ''; };
    return JSON.stringify(values)
}