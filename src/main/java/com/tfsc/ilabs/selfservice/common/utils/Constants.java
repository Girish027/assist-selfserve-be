package com.tfsc.ilabs.selfservice.common.utils;

import com.tfsc.ilabs.selfservice.common.models.PublishType;
import com.tfsc.ilabs.selfservice.workflow.models.WorkflowInstanceStatus;

import java.util.*;

public class Constants {

    public static final String DEFAULT_ERROR_MESSAGE = "error occurred while processing request";
    public static final String METHOD_ARGUMENT_TYPE_MISMATCH_ERROR_MESSAGE = "argument type mismatch";
    public static final String MESSAGE_NOT_READABLE_ERROR_MESSAGE = "argument not readable";
    public static final String ILLEGAL_ARGUMENT_ERROR_MESSAGE = "illegal argument found";
    public static final String DEFAULT_USER = "default.user";

    public static final String ENTITY = "entity";
    public static final String CLIENT_ID = "clientId";
    public static final String ACCOUNT_ID = "accountId";
    public static final String COMPONENT_CLIENT_ID = "componentClientId";
    public static final String COMPONENT_ACCOUNT_ID = "componentAccountId";
    public static final String COMPONENT_LIST = "component_list";
    public static final String PRODUCT_NAME = "productName";
    public static final String BODY = "body";
    public static final String OKTA_URL_CLIENTID = "client_id";
    public static final String OKTA_URL_RESPONSETYPE = "response_type";
    public static final String OKTA_REDIRECT_URI = "redirect_uri";
    public static final String OKTA_GRANT_TYPE = "grant_type";
    public static final String OKTA_URL_CLIENT_SECRET="client_secret";
    public static final String OKTA_CODE = "code";
    public static final String OKTA_SCOPE = "scope";
    public static final String OKTA_STATE = "state";
    public static final String OKTA_ID_TOKEN_HINT = "id_token_hint";
    public static final String OKTA_LOGOUT_REDIRECT_URI = "post_logout_redirect_uri";
    public static final String OKTA_AUTHORIZATION_CODE = "authorization_code";
    public static final String SESSION_COOKIE_NAME = "access_token";
    public static final String SELF_SEVRE = "/assist";
    public static final String DOC_LINK = "docLink";
    public static final String SUPPORT_LINK= "supportLink";
    public static final String TOOLROOT = "toolRoot";
    public static final String TOOLNAME = "toolName";
    public static final String TOOLTITLE = "toolTitle";
    public static final String TOOLTYPE = "toolType";
    public static final String COOKIE_SECURE_FLAG = "cookies.secureFlag";
    public static final String COOKIE_HTTPONLY_FLAG = "cookies.httponlyFlag";
    
    public static final String ENV = "env";
    public static final String SERVICE_BASE_URL ="serviceBaseUrl" ;
    public static final String PARENT_BASE_URL = "parentBaseUrl";
    public static final String CSS_OKTA_ENABLED = "cssOktaEnabled";
    public static final String CONTENT_SERVER_BASEPATH = "contentserver.baseurl";
    public static final String CONTENT_SERVER_CSS_CONTENT_PATH = "contentserver.css_content_base_path";
    public static final String OKTA_AUTHORIZATION_CODE_CALLBACK_URI="/console/authorization-code/callback";
    public static final String OKTA_LOGIN_SCOPE = "openid profile email clients groups";
    public static final String OKTA_LOGOUT_URI="/console/logout";
    public static final String FETCH_JS_FILES_FROM_CONTENT_SERVER_URI ="/js/*";


    public static final String PLACE_HOLDER_PREFIX = "${";
    public static final String PLACE_HOLDER_SUFFIX = "}";
    public static final String OBJECT_TRANSLATOR_METHOD = "translate";
    public static final String DATA = "data";
    public static final String MESSAGE = "message";
    // validation
    public static final String INPUT_KEYS = "validate.keys";
    // bulk upload
    public static final String GCP_UPLOAD_DIR = "self-serve";
    public static final String STORAGE_TYPE = "bulk.storage";
    public static final String GCP_PROJECT_ID = "gcp.project.id";
    public static final String GCP_BUCKET_NAME = "gcp.bucket.name";
    public static final String CREDENTIALS_FILE_PATH = "credentials.file.path";
    public static final String FILE_TYPES = "file.file-types";
    public static final String FILE_MIME_TYPES = "file.file-mimeTypes";
    public static final String LOCAL_STORAGE = "local";
    public static final String GCP_STORAGE = "gcp";
    // for Okta
    public static final String TOKEN = "token";
    public static final String TOKEN_TYPE_HINT = "token_type_hint";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String ACTIVE = "active";
    public static final String TRUE = "true";
    public static final String SUB = "sub";
    public static final String SSO_DISABLE = "sso.disable";
    public static final String OKTA_BASEURL = "okta.baseUrl";
    public static final String OKTA_AUTHORIZATION_SERVICE_ID = "okta.authorization.service.id";
    public static final String OKTA_CLIENT_ID = "okta.client.id";
    public static final String OKTA_CLIENT_SECRET = "okta.client.secret";
    public static final String OKTA_API_KEY = "okta.apiKey";
    public static final String OKTA_CONNECTION_TIMEOUT = "okta.connection.timeout";
    public static final String OKTA_CONNECTION_TIMEOUT_MULTIPLY = "okta.connection.mulitply";
    public static final String PROXY = "config.cob.proxy";
    public static final String PROXY_HOST = "config.proxy.host";
    public static final String PROXY_PORT = "config.proxy.port";
    public static final String PROXY_PROTOCOL = "config.proxy.protocol";
    public static final String UMS_BASE_URL = "ums.baseUrl";
    public static final String AUTHORIZATION_ENABLE = "role_permission.enable";
    public static final String UMS_ENABLE = "ums.enable";
    public static final String THREAD_COUNT = "thread.concurrency";
    public static final String WORKFLOW_INSTANCE_LOCK_TIMEOUT = "workflow_instance.timeout";
    public static final String BASE_URI = "/api";
    public static final String PAGEID = "pageId";
    public static final String ENTITIES = "entities";
    public static final Integer PARENT_ID = -1;
    public static final String USER_EVENT_LOG = "userevent-log";
    public static final String DELIM_UNDERSCORE = "_";
    public static final String CACHE_NAME = "auxEntityMetaData";
    public static final String BULK_STORAGE = "bulk.storage";
    public static final String LOCAL_FILE_UPLOAD_PATH = "file.upload-dir";
    // URI Constants
    public static final String GET_CODE = "/{code}";
    public static final String RELOAD_CONFIG = "/reloadConfig";
    public static final String GET_FETCH_DATA_URI = "/client/{clientId}/account/{accountId}/env/{env}/entity/{entity}/type/{type}/fetch/{fetchFor}";
    public static final String GET_FETCH_ENTITY_URI = "/client/{clientId}/account/{accountId}/publishType/{publishType}/fetch/entity";
    public static final String GET_FETCH_FORMDATA_URI = "/client/{clientId}/account/{accountId}/pageTemplate/{pageTemplateId}/formData";
    public static final String GET_FETCH_URI = "/client/{clientId}/account/{accountId}/publishType/{publishType}/fetch/data";
    public static final String GET_DOWNLOAD_DATA_URI = "/client/{clientId}/account/{accountId}/env/{env}/entity/{entity}/type/{type}/download/{fetchFor}";
    public static final String EXECUTE_ACTION_URI = "/client/{clientId}/account/{accountId}/env/{env}/entity/{entity}/action/{actionId}";
    public static final String INVALIDATE_SESSION_URI = "/session/logout";
    public static final String EXPIRE_ENTITY_CACHE_URI = "/entity-cache/expire";
    public static final String EXPIRE_API_CACHE_URI = "/api-cache/expire";
    //used only for SecurityConfig
    public static final String API_KEY_VALIDATION_ENTITY_CACHE_URI = "/api/entity-cache/expire";
    public static final String API_KEY_VALIDATION_API_CACHE_URI = "/api/api-cache/expire";

    public static final String GET_PERMISSIONS_URI = "/client/{clientId}/account/{accountId}/permissions";
    public static final String GET_PARENT_NODES = "/{scopeType}/{scopeId}/parent/nodes";
    public static final String GET_WORKFLOWS = "/{scopeType}/{scopeId}/workflows";
    public static final String GET_ALL_NODES = "/{scopeType}/{scopeId}/menu/{menuGroupName}/nodes";
    public static final String CACHE_EVICT_ON_INSTANCE_API = "cache_evict_on_instance_api";
    public static final String CONFIG_KEY_MAX_INSTANCES_DAYS_LIMIT = "max_instances_days_limit";
    public static final String CACHE_EXPIRE_CONFIG = "cache_expire_config";
    public static final String CACHE_APIKEY = "cache_apikey";
    public static final String SKIP_LIVE_PROMOTIONS = "skip_live_promotions";
    public static final String MOCK_SUCCESS_RESPONSE = "mock_success_response";
    public static final String CACHE_CLEAR_MAXTIME = "cache_clear_maxtime"; // in milliseconds
    public static final String EXCLUDE_CACHE_FETCHFOR = "exclude_cache_fetchfor";
    public static final String EXCLUDED_FETCHFOR_LIST = "excluded_fetchfor_list";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_AUTHORIZATION_METHOD_BEARER = "Bearer";
    public static final String ACTION_DISCARD = "Discard";
    public static final String RESPONSE_SUCCESS = "Success";
    public static final List<WorkflowInstanceStatus> DRAFT_STATUS = Arrays.asList(WorkflowInstanceStatus.DRAFT_EDIT, WorkflowInstanceStatus.DRAFT_SAVE, WorkflowInstanceStatus.TEST_PROMOTION_FAILED, WorkflowInstanceStatus.PROMOTING_TO_TEST);
    public static final List<WorkflowInstanceStatus> LIVE_STATUS = Collections.singletonList(WorkflowInstanceStatus.PROMOTED_TO_LIVE);
    public static final List<WorkflowInstanceStatus> TEST_STATUS = Arrays.asList(WorkflowInstanceStatus.PROMOTED_TO_TEST, WorkflowInstanceStatus.LIVE_PROMOTION_FAILED, WorkflowInstanceStatus.PROMOTING_TO_LIVE);
    public static final Map<PublishType, WorkflowInstanceStatus[]> NON_TERMINAL_STATUS = new EnumMap<>(PublishType.class);
    public static final String LOCALE_CODE = "localeCode";
    public static final String USER_ID = "UserID";
    public static final String SESSION_ID = "SessionID";
    public static final String REQUEST_ID = "RequestID";
    public static final String RESOURCE_LOCKED = "Resource Locked! In use by another user";
    public static final String UMS_USER_RESOURCE_PATH = "/v1/users";
    public static final int NUM_THOUSAND = 1000;
    public static final int ONE_YEAR_MILLISECS = 31536000;

    public static final String X_RATE_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
    public static final String X_RATE_LIMIT_RETRY_AFTER_MILLISECS = "X-Rate-Limit-Retry-After-Milliseconds";
    public static final String RATE_LIMIT_TOTAL_TOKENS = "rate_limit.total_tokens";
    public static final String RATE_LIMIT_BUCKET_CAPACITY = "rate_limit.bucket_capacity";
    public static final String RATE_LIMIT_REFRESH_DURATION = "rate_limit.refresh_duration";
    public static final String RATE_LIMIT_INITIAL_TOKENS = "rate_limit.initial_tokens";
    public static final String COOKIE_NAME = "access_token";

    public static final String DB_CONFIG_ISS = "JWT_CLAIM_ISS";
    public static final String DB_CONFIG_EXP = "JWT_CLAIM_EXP";
    public static final String DB_CONFIG_ALG = "JWT_CLAIM_ALG";
    public static final String AUTH_TOKEN_TYPE = "GenerateAuthToken";
    public static final String DB_CONFIG_PRIVATE_KEY_TEST = "JWT_CLAIM_PRIVATE_KEY_TEST";
    public static final String DB_CONFIG_PUBLIC_KEY_TEST = "JWT_CLAIM_PUBLIC_KEY_TEST";
    public static final String DB_CONFIG_PRIVATE_KEY_LIVE = "JWT_CLAIM_PRIVATE_KEY_LIVE";
    public static final String DB_CONFIG_PUBLIC_KEY_LIVE = "JWT_CLAIM_PUBLIC_KEY_LIVE";
    public static final String DB_CONFIG_TOKEN_HEADER_KEY = "JWT_TOKEN_HEADER_KEY";
    public static final String EMAIL_ID = "emailId";
    public static final String ENTITY_RESPONSE_TRANSLATOR_KEY = "entityResponseTranslator";
    public static final String UPLOAD_OPTION = "uploadOption";
    static final String JAVASCRIPT = "nashorn";
    static final String USER_AUDIT_LOG = "useraudit-log";
    static final String ACTION_LIVE = "Live";
    static final String ACTION_TEST = "Test";
    static final String STATUS_FAILED = "Fail";
    static final String NO_ENVIRONMENT_TO_EXECUTE_FROM_THIS_STATE = "No environment to execute from this state";
    static final Map<PublishType, WorkflowInstanceStatus[]> TERMINAL_STATUS = new EnumMap<>(PublishType.class);
    private static final WorkflowInstanceStatus[] TERMINAL_STATUS_DEFAULT = {WorkflowInstanceStatus.DISCARDED, WorkflowInstanceStatus.PROMOTED_TO_LIVE};
    private static final WorkflowInstanceStatus[] TERMINAL_STATUS_TEST = {WorkflowInstanceStatus.DISCARDED, WorkflowInstanceStatus.PROMOTED_TO_TEST};
    private static final WorkflowInstanceStatus[] TERMINAL_STATUS_LIVE = TERMINAL_STATUS_DEFAULT;
    private static final WorkflowInstanceStatus[] NON_TERMINAL_STATUS_DEFAULT = {WorkflowInstanceStatus.DRAFT_SAVE, WorkflowInstanceStatus.DRAFT_EDIT, WorkflowInstanceStatus.TEST_PROMOTION_FAILED, WorkflowInstanceStatus.PROMOTED_TO_TEST, WorkflowInstanceStatus.LIVE_PROMOTION_FAILED};
    private static final WorkflowInstanceStatus[] NON_TERMINAL_STATUS_TEST = {WorkflowInstanceStatus.DRAFT_SAVE, WorkflowInstanceStatus.DRAFT_EDIT, WorkflowInstanceStatus.TEST_PROMOTION_FAILED};
    private static final WorkflowInstanceStatus[] NON_TERMINAL_STATUS_LIVE = {WorkflowInstanceStatus.DRAFT_SAVE, WorkflowInstanceStatus.DRAFT_EDIT, WorkflowInstanceStatus.LIVE_PROMOTION_FAILED};
    public static final String DEFAULT_CLIENT_ID = "all";
    public static final String CONFIG_KEY_MAX_INSTANCES_FOR_REFRESH_MINUTES_LIMIT = "max_instances_refresh_minutes_limit";
    public static final String UPLOAD_ID = "uploadId";
    public static final String HTTP_GET_ERROR_MSG="Got exception while making GET call :";
    public static final String SEARCH_VALUE = "searchValue";
    public static final String PAGE_NUMBER = "pageNumber";
    public static final String PAGE_SIZE = "pageSize";


    static {
        TERMINAL_STATUS.put(PublishType.DEFAULT, TERMINAL_STATUS_DEFAULT);
        TERMINAL_STATUS.put(PublishType.TEST_ONLY, TERMINAL_STATUS_TEST);
        TERMINAL_STATUS.put(PublishType.LIVE_ONLY, TERMINAL_STATUS_LIVE);
    }

    static {
        NON_TERMINAL_STATUS.put(PublishType.DEFAULT, NON_TERMINAL_STATUS_DEFAULT);
        NON_TERMINAL_STATUS.put(PublishType.TEST_ONLY, NON_TERMINAL_STATUS_TEST);
        NON_TERMINAL_STATUS.put(PublishType.LIVE_ONLY, NON_TERMINAL_STATUS_LIVE);
    }

    private Constants() {
        // hiding implicit public constructor
    }
}
