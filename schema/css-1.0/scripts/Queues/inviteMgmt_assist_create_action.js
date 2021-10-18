function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap,componentClientId, componentAccountId) {
    var uiData = JSON.parse(pageData).createQueues_p0.tabsField;
    var uiDataB = uiData[0];
    var uiDataA = uiData[1].assistConfig;
    var apiData = [{
        queueName: uiDataB.queueName,
        queueDescription: uiDataB.queueDesc,
        acceptanceRate: uiDataA.inviteMgmt.invites.toString(),
        safetyfactor: uiDataA.inviteMgmt.safetyfactor,
        expiryInterval: uiDataA.inviteMgmt.expiryInterval,
        caModelType: uiDataA.inviteMgmt.caModelType,
        key: uiDataA.inviteMgmt.key !== "" ? uiDataA.inviteMgmt.key : componentAccountId+"-queue-"+uiDataB.queueName
    }];
    return JSON.stringify(apiData);
}