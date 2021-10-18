function translate(values) {
    values = JSON.parse(values)[0];
    if (values.scopeType === 'queue') {
        values.queueScope = values.scopeId;
    } else {
        values.accountScope = values.accountId;
    }
    values.enabled = values.enabled.toString();
    return JSON.stringify(values)
}