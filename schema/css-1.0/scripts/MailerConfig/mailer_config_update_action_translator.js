function translate(pageData, entityId, clientId, accountId, env, prefetch, liveEntityIdMap) {
    pageData = JSON.parse(pageData).updateMailer_p0;
    var templateConfigObj = {};
    var apiData = {};
    for (var i = 0; i < pageData.tabsField[0].baseConfigurationAccordionTabs.length; i++) {
        for (var key in pageData.tabsField[0].baseConfigurationAccordionTabs[i]) {
            templateConfigObj[key] = pageData.tabsField[0].baseConfigurationAccordionTabs[i][key];
        }
    }
    var templateConfig = {};
    templateConfig.header = templateConfigObj.extendedSubject;
    templateConfig.LogoURL = templateConfigObj.companyLogo;
    templateConfig.greetingMessage = templateConfigObj.greetings;
    templateConfig.signOffMessage = templateConfigObj.signOffMsg;
    templateConfig.signatureMessage = templateConfigObj.signatureText;
    templateConfig.headerMessage = templateConfigObj.message;
    templateConfig.c2CMessage = templateConfigObj.clickToChatMsg;
    templateConfig.c2CButtonURL = templateConfigObj.buttonUrl;
    templateConfig.c2CButtonText = templateConfigObj.buttonLabel;
    templateConfig.logoThemeBtnCol = templateConfigObj.logoBackground;
    templateConfig.logoPosition = templateConfigObj.logoPosition;
    templateConfig.themeName = templateConfigObj.themeSelector.theme;
    templateConfig.themeBgCol = templateConfigObj.themeSelector.bgColor;
    templateConfig.themeLinkCol = templateConfigObj.themeSelector.linkColor;
    templateConfig.themeBtnCol = templateConfigObj.themeSelector.buttonColor;
    templateConfig.appendName = templateConfigObj.showCustomerName;
    templateConfig.appendClickToChat = templateConfigObj.showClick;
    templateConfig.appendSignOff = templateConfigObj.showSignOff;
    templateConfig.appendSignature = templateConfigObj.showSignature;
    templateConfig.contextDetailsText = "null";
    var footer = templateConfigObj.footer;
    var labels = ["One", "Two", "Three", "Four", "Five"];
    for (var i = 0; i < labels.length; i++) {
        var label = labels[i];
        if (footer[i] !== undefined) {
            templateConfig["footer" + label] = footer[i].showFooterLink;
            templateConfig["footerLink" + label + "Text"] = footer[i].label;
            templateConfig["footerLink" + label + "URL"] = footer[i].linkUrl;
        } else {
            templateConfig["footer" + label] = false;
            templateConfig["footerLink" + label + "Text"] = "";
            templateConfig["footerLink" + label + "URL"] = "";
        }
    }
    templateConfig.footerMessage = templateConfigObj.footerMessage;
    templateConfig.appendAgentDetails = false;
    templateConfig.appendContextDetails = false;
    apiData.templateConfig = templateConfig;
    apiData.isSSL = pageData.mailConfigAccordionTabs[0].isSsl === "true";
    apiData.bccId = pageData.mailConfigAccordionTabs[0].bccEmail;
    apiData.fromName = pageData.mailConfigAccordionTabs[0].fromName;
    apiData.smtpPort = pageData.mailConfigAccordionTabs[0].smtpPort;
    apiData.smtpHost = pageData.mailConfigAccordionTabs[0].smtpHost;
    apiData.configKey = pageData.mailConfigAccordionTabs[0].configKey;
    apiData.debugEnabled = pageData.mailConfigAccordionTabs[0].debugEnable === "true";
    apiData.fromEmail = pageData.mailConfigAccordionTabs[0].fromEmailId;
    apiData.mailSubject = pageData.mailConfigAccordionTabs[0].mailSubject;
    apiData.fromPassword = pageData.mailConfigAccordionTabs[0].fromPassword;
    apiData.clientId = "nemo-client-" + clientId;
    apiData.accountId = accountId + "-account-default";
    apiData.mailOpeningLines = "";
    apiData.mailSalutation = "";
    if (env === "LIVE") {
        var key = getLiveKey(prefetch, liveEntityIdMap, {
            label: apiData.configKey
        }, "updateMailer_p0.key");
        if (key !== "") {
            apiData.key = key;
        } else {
            apiData.key = pageData.mailConfigAccordionTabs[0].key;
        }

    } else {
        apiData.key = pageData.mailConfigAccordionTabs[0].key;
    }
    return JSON.stringify(apiData);
}

function getLiveKey(prefetch, liveEntityIdMap, testEntity, liveEntityIdKey) {
    if (liveEntityIdMap !== null && liveEntityIdMap !== undefined) {
        var liveEntityId = JSON.parse(liveEntityIdMap);
        if (Object.keys(liveEntityId).length !== 0) {
            return liveEntityId[liveEntityIdKey];
        } else {
            return getLiveKeyFromPrefetch(prefetch, testEntity);
        }
    } else {
        return getLiveKeyFromPrefetch(prefetch, testEntity);
    }
}

function getLiveKeyFromPrefetch(prefetch, testEntity) {
    var listLiveData = JSON.parse(prefetch).listLiveData;
    var key = "";
    for (var i = 0; i < listLiveData.length; i++) {
        if (testEntity.secondaryLabel !== undefined) {
            if (listLiveData[i].label === testEntity.label && listLiveData[i].secondaryLabel === testEntity.secondaryLabel) {
                key = listLiveData[i].name;
                break;
            }
        } else {
            if (listLiveData[i].label === testEntity.label) {
                key = listLiveData[i].name;
                break;
            }
        }
    }
    return key;
}
