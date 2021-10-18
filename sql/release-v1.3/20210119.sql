use self_serve;

UPDATE `page_template` SET `definition` = '{ \"schema\": { \"type\": \"object\", \"properties\": { \"banner\": { \"type\": \"string\" }, \"configs\": { \"type\": \"object\", \"title\": \"\", \"properties\": { \"prevPwd\": { \"type\": \"number\", \"title\": \"Previous Password\", \"description\": \"Number of previously used passwords that are not allowed to be set again\", \"enum\": [ 5, 6, 7, 8, 9, 10 ], \"enumNames\": [ \"Last 5\", \"Last 6\", \"Last 7\", \"Last 8\", \"Last 9\", \"Last 10\" ], \"default\": 5 }, \"pwdLength\": { \"type\": \"number\", \"title\": \"Password Length\", \"description\": \"Minimum number of characters needed in the password\", \"enum\": [ 8, 9, 10, 11, 12, 13, 14, 15, 16 ], \"enumNames\": [ \"8\", \"9\", \"10\", \"11\", \"12\", \"13\", \"14\", \"15\", \"16\" ], \"default\": 8 }, \"pwdExpiry\": { \"type\": \"number\", \"title\": \"Password Expiry\", \"description\": \"Number of days after which password has to be changed\", \"enum\": [ 30, 45, 60, 75, 90 ], \"enumNames\": [ \"30\", \"45\", \"60\", \"75\", \"90\" ], \"default\": 30 }, \"pwdAttempts\": { \"type\": \"number\", \"title\": \"Password Reset Attempts\", \"description\": \"Maximum login attempts allowed with wrong password before account gets locked\", \"enum\": [ 3, 4, 5, 6 ], \"enumNames\": [ \"3\", \"4\", \"5\", \"6\" ], \"default\": 3 } } }, \"note\": { \"type\": \"string\", \"title\": \"*Configurations Value (Editable only for RegEx, Expiry, Length and History Policies.)\", \"default\": \"\" } } }, \"uiSchema\": { \"banner\": { \"ui:field\": \"infobanner\", \"ui:options\": { \"size\": 10, \"bannerTitle\": \"PASSWORD_POLICIES_INFOBANNER_TITLE\", \"bannerDescription\": \"PASSWORD_POLICIES_INFOTEXT\" } }, \"configs\": { \"ui:options\": { \"size\": 8, \"disableSeparator\": true }, \"prevPwd\": { \"ui:field\": \"select\", \"ui:options\": { \"fieldSize\": [ 12, 4 ], \"size\": 12, \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginRight\": \"10px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"pwdLength\": { \"ui:field\": \"select\", \"ui:options\": { \"fieldSize\": [ 12, 4 ], \"size\": 12, \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginRight\": \"10px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"pwdExpiry\": { \"ui:field\": \"select\", \"ui:options\": { \"fieldSize\": [ 12, 4 ], \"size\": 12, \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginRight\": \"10px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"pwdAttempts\": { \"ui:field\": \"select\", \"ui:options\": { \"fieldSize\": [ 12, 4 ], \"size\": 12, \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginRight\": \"10px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } } }, \"note\": { \"ui:field\": \"plaintext\", \"ui:options\": { \"fieldSize\": [ 10, 10 ], \"styles\": { \"label\": { \"padding\": \"0px\", \"fontWeight\": \"normal\" } } } } }, \"viewUiSchema\": { \"banner\": { \"ui:field\": \"infobanner\", \"ui:options\": { \"size\": 10, \"bannerTitle\": \"PASSWORD_POLICIES_INFOBANNER_TITLE\", \"bannerDescription\": \"PASSWORD_POLICIES_INFOTEXT\" } }, \"configs\": { \"ui:options\": { \"size\": 8, \"disableSeparator\": true }, \"prevPwd\": { \"ui:field\": \"select\", \"ui:options\": { \"isDisabled\": true, \"fieldSize\": [ 12, 4 ], \"size\": 12, \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginRight\": \"10px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"pwdExpiry\": { \"ui:field\": \"select\", \"ui:options\": { \"isDisabled\": true, \"inputType\": \"number\", \"fieldSize\": [ 12, 4 ], \"size\": 12, \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginRight\": \"10px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"pwdLength\": { \"ui:field\": \"select\", \"ui:options\": { \"isDisabled\": true, \"fieldSize\": [ 12, 4 ], \"size\": 12, \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginRight\": \"10px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"pwdAttempts\": { \"ui:field\": \"select\", \"ui:options\": { \"isDisabled\": true, \"fieldSize\": [ 12, 4 ], \"size\": 12, \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginRight\": \"10px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } } }, \"note\": { \"ui:field\": \"plaintext\", \"ui:options\": { \"fieldSize\": [ 10, 10 ], \"styles\": { \"container\": {}, \"label\": { \"padding\": \"0px\", \"fontWeight\": \"normal\" }, \"field\": {} } } } }, \"fetch\": { \"page\": \"passwordPolicy_p0\" } }'
WHERE (`id` = 'passwordPolicy_p0');