function translate(pageData, entityId, clientId, accountId, env) {
    var uiData = JSON.parse(pageData).updateQueues_p0.tabsField;
    var uiDataB = uiData[0];
    var uiDataA = uiData[1].assistConfig;
    var apiData = [{
            queueName: uiDataB.queueName,
            queueDescription: uiDataB.queueDesc,
            acceptanceRate: uiDataA.inviteMgmt.invites.toString(),
            safetyfactor: uiDataA.inviteMgmt.safetyfactor,
            expiryInterval: uiDataA.inviteMgmt.expiryInterval,
            caModelType: uiDataA.inviteMgmt.caModelType,
            key: uiDataA.inviteMgmt.key
        }
    ];
    return JSON.stringify(apiData);
}
