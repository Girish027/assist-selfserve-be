function translate(values) {
    values = JSON.parse(values)[0];
    var uiData = {};
    uiData.key = values.key;
    var mailConfigAccordionTabs = {};
    mailConfigAccordionTabs.isSsl = values.isSSL.toString();
    mailConfigAccordionTabs.key = values.key;
    mailConfigAccordionTabs.bccEmail = values.bccId;
    mailConfigAccordionTabs.fromName = values.fromName;
    mailConfigAccordionTabs.smtpPort = values.smtpPort;
    mailConfigAccordionTabs.smtpHost = values.smtpHost;
    mailConfigAccordionTabs.configKey = values.configKey;
    mailConfigAccordionTabs.debugEnable = values.debugEnabled.toString();
    mailConfigAccordionTabs.fromEmailId = values.fromEmail;
    mailConfigAccordionTabs.mailSubject = values.mailSubject;
    mailConfigAccordionTabs.fromPassword = values.fromPassword;
    var tabsField = [];
    var baseConfigurationAccordionTabs = [];
    var themeObj = {};
    if (values.templateConfig !== undefined) {
        var templateConfig = values.templateConfig;
        themeObj.themeSelector = {};
        themeObj.themeSelector.theme = templateConfig.themeName;
        themeObj.themeSelector.bgColor = templateConfig.themeBgCol;
        themeObj.themeSelector.linkColor = templateConfig.themeLinkCol;
        themeObj.themeSelector.buttonColor = templateConfig.themeBtnCol;
        baseConfigurationAccordionTabs.push(themeObj);
        var headerObj = {};
        headerObj.extendedSubject = templateConfig.header !== "null" ? templateConfig.header : "";
        headerObj.companyLogo = templateConfig.LogoURL !== "null" ? templateConfig.LogoURL : undefined;
        headerObj.logoPosition = templateConfig.logoPosition !== "null" ? templateConfig.logoPosition : "";
        headerObj.logoBackground = templateConfig.logoThemeBtnCol !== "null" ? templateConfig.logoThemeBtnCol : "";
        headerObj.greetings = templateConfig.greetingMessage !== "null" ? templateConfig.greetingMessage : "";
        headerObj.showCustomerName = templateConfig.appendName !== "null" ? templateConfig.appendName : "";
        headerObj.message = templateConfig.headerMessage !== "null" ? templateConfig.headerMessage : "";
        baseConfigurationAccordionTabs.push(headerObj);
        var c2cObj = {};
        c2cObj.showClick = templateConfig.appendClickToChat;
        c2cObj.clickToChatMsg = templateConfig.c2CMessage !== "null" ? templateConfig.c2CMessage : "";
        c2cObj.buttonLabel = templateConfig.c2CButtonText !== "null" ? templateConfig.c2CButtonText : "";
        c2cObj.buttonUrl = templateConfig.c2CButtonURL !== "null" ? templateConfig.c2CButtonURL : undefined;
        baseConfigurationAccordionTabs.push(c2cObj);
        var footerObj = {};
        footerObj.showSignOff = templateConfig.appendSignOff;
        footerObj.signOffMsg = templateConfig.signOffMessage !== "null" ? templateConfig.signOffMessage : "";
        footerObj.showSignature = templateConfig.appendSignature !== "null" ? templateConfig.appendSignature : "";
        footerObj.signatureText = templateConfig.signatureMessage !== "null" ? templateConfig.signatureMessage : "";
        footerObj.footerMessage = templateConfig.footerMessage !== "null" ? templateConfig.footerMessage : "";
        var footer = [];
        var labels = ["Two", "Three", "Four", "Five"];
        footer.push({
            showFooterLink: templateConfig.footerLinkOne,
            label: templateConfig.footerLinkOneText,
            linkUrl: templateConfig.footerLinkOneURL
        });
        for (var i = 0; i < labels.length; i++) {
            var label = labels[i];
            if (templateConfig["footerLink" + label] === true) {
                footer.push({
                    showFooterLink: templateConfig["footerLink" + label],
                    label: templateConfig["footerLink" + label + "Text"] !== "null"? templateConfig["footerLink" + label + "Text"]: "",
                    linkUrl: templateConfig["footerLink" + label + "URL"] !== "null" ? templateConfig["footerLink" + label + "URL"]: ""
                });
            } else {
                if (templateConfig["footerLink" + label + "Text"] !== "" || templateConfig["footerLink" + label + "URL"] !== "") {
                    footer.push({
                        showFooterLink: templateConfig["footerLink" + label],
                        label: templateConfig["footerLink" + label + "Text"] !== "null"? templateConfig["footerLink" + label + "Text"]: "",
                        linkUrl: templateConfig["footerLink" + label + "URL"] !== "null" ? templateConfig["footerLink" + label + "URL"]: ""
                    });
                }
            }
        }
        footerObj.footer = footer;
        baseConfigurationAccordionTabs.push(footerObj);
    }else{
        baseConfigurationAccordionTabs = [{themeSelector: {}},{},{},{}]
    }
    tabsField.push({"baseConfigurationAccordionTabs": baseConfigurationAccordionTabs});
    tabsField.push({"mailerPreview": {}});
    uiData.mailConfigAccordionTabs = [mailConfigAccordionTabs];
    uiData.tabsField = tabsField;
    return JSON.stringify(uiData);
}



