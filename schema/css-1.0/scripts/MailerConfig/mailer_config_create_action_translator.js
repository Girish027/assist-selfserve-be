function translate(pageData, entityId, clientId, accountId, env) {
    pageData = JSON.parse(pageData).createMailer_p0;
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
    apiData.key = pageData.mailConfigAccordionTabs[0].key;
    apiData.configKey = pageData.mailConfigAccordionTabs[0].configKey;
    apiData.debugEnabled = pageData.mailConfigAccordionTabs[0].debugEnable === "true";
    apiData.fromEmail = pageData.mailConfigAccordionTabs[0].fromEmailId;
    apiData.mailSubject = pageData.mailConfigAccordionTabs[0].mailSubject;
    apiData.fromPassword = pageData.mailConfigAccordionTabs[0].fromPassword;
    apiData.clientId = "nemo-client-" + clientId;
    apiData.accountId = accountId + "-account-default";
    apiData.mailOpeningLines = "";
    apiData.mailSalutation = "";
    return JSON.stringify(apiData);
}