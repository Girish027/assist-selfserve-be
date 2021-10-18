function translate(values) { 
    values = JSON.parse(values)[0]; 
    var uiData = { 
        tabsField: [
            {}, 
            { 
                assistConfig: { 
                    inviteMgmt: { 
                        expiryInterval: values.expiryInterval, 
                        invites: Number(values.acceptanceRate),
                        safetyfactor: values.safetyfactor,
                        caModelType: "INVITE_MODEL",                        
                        key: values.key
                    } 
                } 
            }
        ] 
    }; 
    return JSON.stringify(uiData);
}