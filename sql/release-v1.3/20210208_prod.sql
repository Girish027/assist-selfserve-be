use self_serve;

UPDATE `page_template` SET `definition` = '{ \"schema\": { \"definitions\": { \"card\": { \"type\": \"string\", \"title\": \"Select Card\", \"enum\": [ \"\" ], \"enumNames\": [ \"Select Card\" ] } }, \"type\": \"object\", \"properties\": { \"banner\": { \"type\": \"string\" }, \"key\": { \"type\": \"string\", \"title\": \"\", \"default\": \"\" }, \"card\": { \"title\": \"ACTIVECARD_SELECTCARD\", \"$ref\": \"#/definitions/card\" }, \"preview\": { \"type\": \"string\", \"title\": \"Preview\" }, \"commandName\": { \"type\": \"string\", \"title\": \"CARDS_TABLE_HEADER_NAME\" }, \"commandDescription\": { \"type\": \"string\", \"title\": \"HOME_PAGE_TABLE_HEAD_DESC\" }, \"numberOfParamters\": { \"type\": \"string\", \"title\": \"ACTIVECARD_NUMBEROFPARAMS\" }, \"commandActionName\": { \"type\": \"string\", \"title\": \"COMMAND_NAME\" }, \"enabled\": { \"type\": \"string\", \"title\": \"IS_ENABLED\", \"enum\": [ \"true\", \"false\" ], \"enumNames\": [ \"true\", \"false\" ], \"default\": \"true\" } }, \"required\": [ \"commandDescription\", \"numberOfParamters\", \"commandActionName\" ] }, \"uiSchema\": { \"ui:order\":[\"banner\",\"key\",\"card\",\"preview\",\"commandName\",\"commandDescription\",\"numberOfParamters\",\"commandActionName\",\"enabled\"], \"banner\": { \"ui:field\": \"infobanner\", \"ui:options\": { \"size\": 10, \"bannerTitle\": \"\", \"bannerDescription\": \"ACTIVE_CARDS_INFOTEXT_PROD\" } }, \"key\": { \"ui:field\": \"text\", \"ui:options\": { \"inputType\": \"text\", \"hidden\": true, \"fieldSize\": [ 8, 8 ], \"size\": 12, \"styles\": { \"container\": { \"marginBottom\": \"15px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"card\": { \"ui:field\": \"select\", \"ui:options\": { \"fieldSize\": [ 8, 8 ], \"size\": 12, \"styles\": { \"container\": { \"marginBottom\": \"15px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"preview\": { \"ui:field\": \"previewComponent\", \"ui:options\": { \"fieldSize\": [ 8, 8 ], \"previewTitle\":\"Preview - {previewTitleKey}\", \"previewTitleKey\":\"commandName\", \"previewLinkTitle\":\"Click Here \", \"labelText\":\" to Preview\", \"isEditable\": true, \"previewType\":\"image\", \"template\": [ { \"name\": \"cardsApiUrl\", \"value\": \"{vccApiBaseUrl}/crs/v1/clients/{clientId}/accounts/{accountId}/activecards/meta/name/{cardName}?view=test&channel=CHAT\" }, { \"name\": \"editUrl\", \"value\": \"{vccAppUrl}/edit#cards&cardId={cardId}\" } ], \"imageApi\": { \"headers\": { \"template\": [ { \"name\": \"apikey\", \"value\": \"{vccApiKey}\" } ] }, \"imageLink\": \"viewsData.0.previewImage\", \"imageId\":  \"artifactId\" }, \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginTop\": \"-15px\" }, \"label\": { \"marginBottom\": \"8px\", \"marginTop\": \"-5px\" }, \"field\": {} } } }, \"commandName\": { \"ui:field\": \"boundField\", \"ui:options\": { \"inputType\": \"text\", \"isDisabled\": true, \"fieldSize\": [ 8, 8 ], \"size\": 12, \"component\": \"text\", \"boundFieldValuePath\": \"card\", \"styles\": { \"container\": { \"marginBottom\": \"15px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"commandDescription\": { \"ui:field\": \"text\", \"ui:options\": { \"inputType\": \"text\", \"isDisabled\": false, \"fieldSize\": [ 8, 8 ], \"size\": 12, \"styles\": { \"container\": { \"marginBottom\": \"15px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"numberOfParamters\": { \"ui:field\": \"text\", \"ui:options\": { \"inputType\": \"text\", \"isDisabled\": false, \"fieldSize\": [ 12, 12 ], \"size\": 4, \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginRight\": \"10px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"commandActionName\": { \"ui:field\": \"boundField\", \"ui:options\": { \"inputType\": \"plaintext\", \"isDisabled\": true, \"component\": \"text\", \"boundFieldValuePath\": \"card\", \"preProcessorForValue\": [ { \"operation\": \"prefix\", \"value\": \"/f \" } ], \"fieldSize\": [ 12, 12 ], \"size\": 4, \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginLeft\": \"10px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"enabled\": { \"ui:field\": \"select\", \"ui:options\": { \"inputType\": \"text\", \"isDisabled\": false, \"fieldSize\": [ 10, 10 ], \"size\": 5, \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginRight\": \"20px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } } }, \"viewUiSchema\": { \"ui:order\":[\"banner\",\"key\",\"card\",\"preview\",\"commandName\",\"commandDescription\",\"numberOfParamters\",\"commandActionName\",\"enabled\"], \"banner\": { \"ui:field\": \"infobanner\", \"ui:options\": { \"size\": 10, \"bannerTitle\": \"\", \"bannerDescription\": \"ACTIVE_CARDS_INFOTEXT_PROD\" } }, \"key\": { \"ui:field\": \"text\", \"ui:options\": { \"inputType\": \"text\", \"hidden\": true, \"fieldSize\": [ 8, 8 ], \"size\": 12, \"styles\": { \"container\": { \"marginBottom\": \"15px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"card\": { \"ui:field\": \"select\", \"ui:options\": { \"isDisabled\": false, \"fieldSize\": [ 8, 8 ], \"size\": 12, \"styles\": { \"container\": { \"marginBottom\": \"15px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"preview\": { \"ui:field\": \"previewComponent\", \"ui:options\": { \"fieldSize\": [ 8, 8 ], \"previewTitle\":\"Preview - {previewTitleKey}\", \"previewTitleKey\":\"commandName\", \"previewLinkTitle\":\"Click Here \", \"labelText\":\" to Preview\", \"isEditable\": true, \"previewType\":\"image\", \"template\": [ { \"name\": \"cardsApiUrl\", \"value\": \"{vccApiBaseUrl}/crs/v1/clients/{clientId}/accounts/{accountId}/activecards/meta/name/{cardName}?view=test&channel=CHAT\" }, { \"name\": \"editUrl\", \"value\": \"{vccAppUrl}/edit#cards&cardId={cardId}\" } ], \"imageApi\": { \"headers\": { \"template\": [ { \"name\": \"apikey\", \"value\": \"{vccApiKey}\" } ] }, \"imageLink\": \"viewsData.0.previewImage\", \"imageId\":  \"artifactId\" }, \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginTop\": \"-15px\" }, \"label\": { \"marginBottom\": \"8px\", \"marginTop\": \"-5px\" }, \"field\": {} } } }, \"commandName\": { \"ui:field\": \"boundField\", \"ui:options\": { \"inputType\": \"text\", \"isDisabled\": false, \"component\": \"text\", \"boundFieldValuePath\": \"card\", \"fieldSize\": [ 8, 8 ], \"size\": 12, \"styles\": { \"container\": { \"marginBottom\": \"15px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"commandDescription\": { \"ui:field\": \"text\", \"ui:options\": { \"inputType\": \"text\", \"isDisabled\": false, \"fieldSize\": [ 8, 8 ], \"size\": 12, \"styles\": { \"container\": { \"marginBottom\": \"15px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"numberOfParamters\": { \"ui:field\": \"text\", \"ui:options\": { \"inputType\": \"text\", \"isDisabled\": false, \"fieldSize\": [ 12, 12 ], \"size\": 4, \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginRight\": \"10px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"commandActionName\": { \"ui:field\": \"boundField\", \"ui:options\": { \"inputType\": \"plaintext\", \"isDisabled\": true, \"fieldSize\": [ 12, 12 ], \"size\": 4, \"component\": \"text\", \"boundFieldValuePath\": \"card\", \"preProcessorForValue\": [ { \"operation\": \"prefix\", \"value\": \"/f \" } ], \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginLeft\": \"10px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } }, \"enabled\": { \"ui:field\": \"select\", \"ui:options\": { \"inputType\": \"text\", \"isDisabled\": false, \"fieldSize\": [ 10, 10 ], \"size\": 5, \"styles\": { \"container\": { \"marginBottom\": \"15px\", \"marginRight\": \"10px\" }, \"label\": { \"marginBottom\": \"8px\" }, \"field\": {} } } } }, \"fetch\": { \"list\": { \"listActiveCardsFromVCC\": \"definitions.card\" }, \"page\": \"createActiveCards_p0\" }, \"validation\": { \"enabled\": true, \"type\": \"onchange\" } }'
WHERE (`id` = 'createActiveCards_p0');
