package com.scania.sdip.exceptions;

/**
 * The SdusErrorCode class holds the error codes used in the SDIP platform. The codes are generic
 * for all services within the platform. The class also provides methods for retrieving the
 * specified Http error code and error message, the SDIP error code, the log message and the exit
 * code.
 */
public enum SdipErrorCode implements ErrorCode {

  //4xx_codes
  // 400
  INVALID_SPARQLENDPOINT_DATA(400, 4001,
      "The value for the field \"sparqlEndpoint\" not a valid URL, change the value for this field to be on the form \"http://scania.com/neptune/sparql\\\".",
      "The value for the field \"sparqlEndpoint\" is not a valid URL, sparqlEndpoint was: {0}"),
  INVALID_ID_DATA(400, 4002,
      "The value for the field \"id\" not a valid id.",
      "The value for the field \"id\"\" is not valid. "),
  INVALID_CONTEXT_DATA(400, 4003,
      "The value for the field \"context\" is not a valid context, the context needs to be described in JSON-LD(https://json-ld.org/spec/latest/json-ld/).",
      "The value for the field \"context\" is not a valid context, context was: {0}"),
  INVALID_ONTOLOGY_DATA(400, 4004,
      "The value for the field \"ontology\" is not a valid ontology, the ontology needs to be in OWL(https://www.w3.org/TR/owl2-syntax/) and described in RDF/XML(https://www.w3.org/TR/rdf-syntax-grammar/), Turtle(https://www.w3.org/TR/turtle/) or JSON-LD(https://json-ld.org/spec/latest/json-ld/), you can use protege(https://protegewiki.stanford.edu/wiki/Main_Page) to build an ontology.",
      "The value for the field \"ontology\" is not a valid ontology, ontology was: {0}"),
  INVALID_SHACL_DATA(400, 4005,
      "The value for the field \"shacl\" is not a valid shacl file, the shacl needs to be SHACL(https://www.w3.org/TR/shacl/) and described in RDF/XML(https://www.w3.org/TR/rdf-syntax-grammar/), Turtle(https://www.w3.org/TR/turtle/) or JSON-LD(https://json-ld.org/spec/latest/json-ld/).",
      "The value for the field \"shacl\" is not a valid shacl-file, shacl was: {0}."),
  JSON_NOT_MATCHING_JSON_SCHEMA(400, 4006,
      "The provided JSON does not match the form accepted by the server. JSON should be on the form {0}.",
      "The provided json contains the wrong keys, JSON should be on the form {0}, json was {1}"),
  DOCUMENT_NOT_JSON(400, 4007, "The provided document is not JSON(https://www.json.org/), {0}",
      "The provided document is not JSON, error: {0}, document:\\n {1}"),
  MALFORMED_JWT_TOKEN(400, 4008, "The provided token is not a valid JWT token.",
      "The provided token is not a valid JWT token. Token was: {0}"),
  SEARCH_STRING_EMPTY_ERROR(400, 4009,
      "The provided value for field 'searchString' seems to be empty, it must contain a value.",
      "The provided value for field 'searchString' seems to be empty, it must contain a value, searchString was: {0}"),
  PROHIBITED_SPARQL_QUERY(400, 40010,
      "The provided SPARQL query does not seem to be a supported type, supported query types are CONSTRUCT, SELECT, ASK and DESCRIBE.",
      "The provided SPARQL query does not seem to be a supported type, supported query types are CONSTRUCT, SELECT, ASK and DESCRIBE, query was: {0}"),
  UNABLE_TO_CONVERT_FREETEXT_TO_SPARQL(400, 40011,
      "The value provided for field 'searchString' is invalid.",
      "The value provided for field 'searchString' is not possible to convert into SPARQL. searchString was: {0}, SPARQL: {1}"),
  EMPTY_DOCUMENT(400, 40012,
      "The provided json document was empty. It must contain at least one element.",
      "The provided json document was empty. It must contain at least one element."),
  INVALID_OBJECTURI_DATA(400, 40013,
      "The field \"objecturi\" must be a complete URL, it should be on the form \"http://scania.com/some/path/here\"",
      "The value for field \"objecturi\" must be a complete URL, objecturi was: {0}"),
  INVALID_SUBJECTURI_DATA(400, 40014,
      "The field \"subjecturi\" must be a complete URL, it should be on the form \"http://scania.com/some/path/here\"",
      "The value for field \"subjecturi\" must be a complete URL, subjecturi was: {0}"),
  BAD_QUERY_ENCODING(400, 40015,
      "The query provided could not be converted to an URL encoded format.",
      "The query provided could not be converted to an URL encoded format. Query was: {0}"),
  UNABLE_TO_PARSE_JSON_LD(400, 40016,
      "Was unable to parse the JSON-LD which was created by merging the input with the context. Merged JSON-LD: {0}",
      "Was unable to parse the JSON-LD which was created by merging the input with the context. Merged JSON-LD: {0}, original JSON: {1}"),
  SHACL_VALIDATION_FAILED(400, 40017,
      "SHACL validation failed when trying to store triples, SHACL report was: {1}",
      "SHACL validation failed when trying to store triples, triples was: {0}, SHACL report was: {1}"),
  NO_SHACL_TARGET_CLASS(400, 40018,
      "Shacl Shape {0} is invalid. TargetNode or TargetClass property is mandatory in Shacl Shape.",
      "Shacl Shape {0} is invalid. TargetNode or TargetClass property is mandatory in Shacl Shape."),
  NO_SHACL_TYPE(400, 40019,
      "Shacl Shape {0} is invalid. RDF Type property is mandatory in Shacl Shape",
      "Shacl Shape {0} is invalid. RDF Type property is mandatory in Shacl Shape"),
  INVALID_DEFAULTDOMAIN_ID(400, 40020,
      "The defaultDomains domainId {0} does not exist. please configure a domain.",
      "The defaultDomains domainId {0} does not exist. please configure a domain."),
  INVALID_JSON_CONTENT(400, 40021,
      "Validation failed. JSON does not contain required fields. {0}  Structure should be '{\"@graph\": [{\"@id\", ... }, {\"@id\", ... }, ...]}'",
      "Validation failed. JSON does not contain required fields. {0}, JSON: {1}"),
  INVALID_TRIPLE_JSON(400, 40022,
      "Validation failed. Invalid json structure. Structure should be '{\"@graph\": [{\"@id\", ... }, {\"@id\", ... }, ...]}'",
      "Validation failed. Invalid json structure. Structure should be '{\"@graph\": [{\"@id\", ... }, {\"@id\", ... }, ...]}', JSON: {0}"),
  INVALID_DATABASE_CONNECTION_PARAMETERS_FOR_STATELESS_REQUEST(400, 40023,
      "The connection details for the database is not correct. Validation errors: {0}",
      "The connection details for the database is not correct. Validation errors: {0}"),
  EMPTY_VALID_AND_INVALID_MODELS(400, 40024,
      "Nothing to persist to RDF Store.",
      "Invalid and valid Models are empty. Nothing to persist. Returning status 400."),
  INVALID_FIELD(400, 40025,
      "The value for fields {0} was missing or set to an empty value. Try again with a value set.",
      "The value for field was missing or set to an empty value. Validation failures was: {1}"),
  NO_BODY(400, 40026, "No body was provided. Body is required.",
      "No body was provided. Body is required."), //
  INCORRECT_MAPPER_JSON(400, 40027,
      "The provided JSON is not a correct mapper service JSON. JSON should be on the form: {0}",
      "The provided JSON is not a correct mapper service JSON. JSON should be on the form: {0}, JSON was: {1}"),
  UNKNOWN_IO_ERROR_WHEN_MAPPING(400, 40028,
      "The mapper service got an unrecognized IO Error while parsing input json.",
      "The mapper service got an unrecognized IO Error while parsing input json."),
  INVALID_BASEIRI_DATA(400, 40029,
      "The field \"basseiri\" must be a complete URL, it should be on the form \"http://scania.com/some/path/here\"",
      "The value for field \"objecturi\" must be a complete URL, objecturi was: {0}"),
  DOMAIN_NOT_CONFIGURED(400, 40030,
      "No domain configuration exists for the domain ID: {0} that was provided in the request.",
      "The service is not configured with a domain that has domain ID: {0}."),
  INVALID_SERVICEID_DATA(400, 40031,
      "No service exist with specified serviceId. {0}",
      "No service exist with specified serviceId. {0}"),
  REJECTED_SERVICE_CONFIGURATION(400, 40032,
      "The Configuration's defaultdomain or domains have been deleted and not yet replaced, please update the service configuration with available domains.",
      "The Configuration's defaultdomain or domains have been deleted and not yet replaced, please update the service configuration with available domains."),
  INVALID_DOMAIN_ID_REGEX(400, 40033,
      "The specified domainId {0} is not valid. a valid domainId complies with regex ^[a-z0-9_]+$",
      "The specified domainId {0} is not valid. a valid domainId complies with regex ^[a-z0-9_]+$"),
  INVALID_SERVICTYPE(400, 40034,
      "The specified servicetype {0} is not a valid servicetype. These are the valid servicetypes: {1} .",
      "The specified servicetype {0} is not a valid servicetype. These are the valid servicetypes: {1} ."),
  MAXIMUN_DOMAINS_CONFIGURED(400, 40035,
      "you cant configure more than 20 domains.",
      "you cant configure more than 20 domains."),
  INVALID_DEFAULTDOMAIN_INPUT(400, 40036,
      "The specified ServiceType does not allow defaultDomain. please leave it null.",
      "The specified ServiceType does not allow defaultDomain. please leave it null."),
  INVALID_DOMAINTYPE_DATA(400, 40037,
      "The value for the field: domainType not a valid.Valid input: INTERNAL or EXTERNAL",
      "The value for the field: domainType is not valid."),
  INPUT_NOT_COMPLETE(400, 40038,
      "The input query does not have complete syntax. please verify syntax.",
      "The input query does not have complete syntax. please verify syntax."),
  DOCUMENT_NOT_JSON2(400, 40039, "The provided document is not JSON(https://www.json.org/), {0}",
      "The provided document is not JSON, error: {0}"),
  NO_SERVICE_EXIST(400, 40040,
      "No Service was found in SDCS. Please make sure the wanted service is running and configured properly",
      "No Service was found in SDCS.  Please make sure the wanted service is running and configured properly"),
  NO_SERVICE_CONNECTION_EXIST(400, 40041,
      "The Service {0} does not have any serviceSpecific attributes. please serviceSpecific with empty string \"\".",
      "The Service {0} does not have any serviceSpecific attributes. please leave empty."),
  SERVICE_DONT_ALLOW_EXTERNAL(400, 40042,
      "Domain:{0} is configured with EXTERNAL domainType. Current Service cant write to a external domain. please reconfigure to INTERNAL or use other domain",
      "Domain:{0} is configured with EXTERNAL domainType. Current Service cant write to a external domain. please reconfigure to INTERNAL or use other domain"),
  TOO_MANY_ARGUMENTS(400, 40043,
      "Too many arguments has been found {0}, maximum number of serviceSpecific arguments for the service: {1}, example {2}",
      "Too many arguments has been found {0}, maximum number of serviceSpecific arguments for the service: {1}, example {2}"),
  MISSING_SERVICESPECIFIC_ARGUMENTS(400, 40044,
      "Missing arguments for service: {0}, valid arguments: {1} ",
      "Missing arguments for service: {0}, valid arguments: {1} "),
  DOMAIN_UPDATE_NOT_ALLOWED_SDTS(400, 40045,
      "SDTS service configuration : {0} does not allow domains with DomainType: EXTERNAL , please change the domainType or remove the domain from the SDTS service configuration.",
      "SDTS service configuration : {0} does not allow domains with DomainType: EXTERNAL , please change the domainType or remove the domain from the SDTS service configuration."),
  INVALID_DOMAINID(400, 40046,
      "The specified domainId {0} does not exist.use /listdomains to see the configured domains. Please choose an existing domain.",
      "The specified domainId {0} does not exist. Please choose an existing domain."),
  VALID_AND_INVALID_MODELS_EXIST(400, 40047,
      "SHACL validation failed when trying to store triples. Nothing is persisted to the RDF Store. Here is a validation report: {1}",
      "Invalid and valid Models exist. Returning status 400. Triples was: {0}, SHACL report was: {1}"),
  NOTHING_TO_PERSIST_TO_RDF_STORE(400, 40048,
      "Nothing to persist to RDF Store.",
      "Persisting without SHACL validation failed. Nothing to persist. Returning status 400."),
  NO_DOMAINCONFIGURATIONS_EXIST(404, 40049,
      "No Domain Configurations exist in SDCS. Please configure a domain. /domain (POST)",
      "No Domain Configurations exist in SDCS. Please configure a domain. /domain (POST)"),
  APPLICATION_UNCONFIGURED(400, 40050,
      "This service is not yet configured, you should configure it by calling POST /configure.",
      "This service is not yet configured, the user should configure it by calling POST /configure before calling endpoint again."),
  APPLICATION_UNCONFIGURED_WITH_SERVICEID(400, 40051,
      "No configuration with the serviceId: {0} , the user should configure a configuration with matching serviceId.",
      "No configuration with the serviceId: {0} , the user should configure a configuration with matching serviceId."),
  INVALID_DOMAINS(400, 40052,
      "The specified string of domains is not valid, should follow this regex pattern: ^(([a-z0-9_]+)(?:,|$))+$ ex. domain1,domain2,domain3",
      "The specified string of domains is not valid, should follow this regex pattern: ^(([a-z0-9_]+)(?:,|$))+$ ex. domain1,domain2,domain3"),
  DOMAIN_NOT_ANSWERING(400, 40053,
      "One or more configured domain endpoint did not answer correctly.please verify query and make sure the domains have the correct sparql endpoint or that the endpoint is reachable: {0}",
      "One or more configured domain endpoint did not answer correctly.please verify query and make sure the domains have the correct sparql endpoint or that the endpoint is reachable: {0}"),
  SPARQL_QUERY_NOT_ALLOWED(400, 40054,
      "The given SPARQL query is not allowed: {0}",
      "The given SPARQL query is not allowed: {0}"),
  JSON_MAPPING_EXCEPTION(400, 40055,
      "Json mapping exception: {0}",
      "Json mapping exception: {0}"),
  INVALID_TRIPLESTORETYPE_DATA(400, 40056,
      "The value for the field: triplestoretype not a valid.Valid input: GRAPHDB, NEPTUNE, BLAZEGRAPH or GENERIC",
      "The value for the field: triplestoretype is not valid."),
  INVALID_TRIPLES_DATA(400, 40057,
      "No triples file was provided. The file is required to load the triples",
      "No triples file was provided."),
  INVALID_TRIPLESTORE_ID(400, 40058,
      "The provided TripleStoreType \"{0}\" is not recognised as a supported triplestore, supported triplestores: {1}.",
      "The provided TripleStoreType \"{0}\" is not recognised as a supported triplestore, supported triplestores: {1}."),
  INVALID_UNSUPPORTED_FORMAT(400, 40059,
      "The provided file is not a supported format. supported formats are rdfxml (.rdf), turtle (.ttl) or ntriples (.nt).",
      "The provided file is not a supported format. supported formats are rdfxml (.rdf), turtle (.ttl) or ntriples (.nt)."),
  INVALID_NEPTUNE_BULK_ENVIRONMENT(400, 40060,
      "One or more of the environment mandatory for neptune bulk load is not set: {0}.",
      "One or more of the environment mandatory for neptune bulk load is not set {0}."),
  ERROR_BLAZEGRAPH_NULLGRAPH(400, 40061,
      "Blazegraph need a set graph to use bulkload.",
      "Blazegraph need a set graph to use bulkload."),
  UNKOWN_SERVICE_SPECIFIC(400, 40062,
      "{0} is not a valid serviceSpecific parameter for {1}. These are the valid arguments for this service {2}",
      "{0} is not a valid serviceSpecific parameter for {1}. These are the valid arguments for this service {2}"),
  PROHIBITED_SPARQL_UPDATE_QUERY(400, 40063,
      "The provided SPARQL UPDATE does not seem to be a supported type, supported query types are INSERT, DELETE, DELETE/INSERT, CREATE, COPY, MOVE or ADD.",
      "The provided SPARQL UPDATE does not seem to be a supported type, supported query types are INSERT, DELETE, DELETE/INSERT, CREATE, COPY, MOVE or ADD. query was: {0}"),
  MALFORMED_URL(400, 40064,
      "The url: {0} is malformed please contact support if issue persist {1}.",
      "The url: {0} is malformed."),
  ASK_SPARQL_ERROR(400, 40065,
      "ASK sparql invalid: {0} .",
      "ASK sparql invalid: {0} ."),
  SPARQL_ENDPOINT_EMPTY(400, 40066,
      "Provided sparql endpoint is empty or invalid, please provide valid sparql endpoint. {0}",
      "Provided sparql endpoint is empty or invalid, please provide valid sparql endpoint. {0}"),
  SCRIPT_INVALID(400, 40067,
      "Provided script is malformed, please provide the valid script. {0}",
      "Provided script is malformed, please provide the valid script. {0}"),
  SCRIPT_PROPERTY_MISSING(400, 40068,
      "Property required for transformation is missing, please provide the valid script. {0}",
      "Property required for transformation is missing, please provide the valid script. {0}"),
  HTTP_RESPONSE_NOK(400, 40069,
      "Received unexpected response code: {0}, from server: {1} .",
      "Received unexpected response code: {0}, from server: {1} ."),
  SCRIPT_OUTPUT_ERROR(400, 40070,
      "Error occured while converting script output into Collection object, please provide the valid script. {0}",
      "Error occured while converting script output into Collection object, please provide the valid script. {0}"),
  SOAP_RESPONSE_WSDL_NOK(400, 40071,
      "Received unexpected response code when parsing wsdlfile: {0}, from server: {1} .",
      "Received unexpected response code when parsing wsdlfile: {0}, from server: {1} ."),
  INVALID_JSONARRAY_RESPONSE(400, 40072,
      "Received response is empty or invalid JSONArray. Please provide valid JSONArray. {0} ",
      "Received response is empty or invalid JSONArray. Please provide valid JSONArray. {0} "),
  FAILED_TO_PARSE_JSONARRAY(400, 40073,
      "Property required to parse JSONArray into Model object is missing. Please provide valid JSONArray. {0}",
      "Property required to parse JSONArray into Model object is missing. Please provide valid JSONArray. {0} "),
  MALFORMED_SPARQL_QUERY(400, 40074,
      "The provided SPARQL query is not valid. Please provide valid SPARQL query. {0}",
      "The provided SPARQL query is not valid. Please provide valid SPARQL query. {0} "),
  INVALID_EXECUTIONID(400, 40075,
      "Provided execution id - {0} is not invalid. Try again with a valid id.",
      "Provided execution id - {0} is not invalid. Try again with a valid id."),
  SIGNATURE_JWT_TOKEN_ERROR(400, 40076, "Signature validation failed for the  JWT token.",
          "Signature validation failed for the  JWT token. Sign in with correct key : {0}"),
  INVALID_AUTH_TOKEN_ERROR(400, 40077,
          "Provided Authorization Token is invalid to fetch On-behalf Of(OBO) token for Stardog connection. Pass the valid SDOS JWT token. {0}",
          "Provided Authorization Token is invalid to fetch On-behalf Of(OBO) token for Stardog connection. Pass the valid SDOS JWT token. {0}"),
  MISSING_OBO_TOKEN_ERROR(400, 40078,
          "On-behalf Of(OBO) token required for stardog connection is missing. Please check whether user has access to Stardog with proper roles assigned or provide valid JWT token to fetch OBO token",
          "On-behalf Of(OBO) token required for stardog connection is missing. Please check whether user has access to Stardog with proper roles assigned or provide valid JWT token to fetch OBO token"),
  // 404
  PAGE_NOT_FOUND(404, 4041,
      "The url {0} does not exist in the platform. To see what urls are available go to {1}",
      "The url {0} does not exist in the platform. The user has been told to go to the swagger ui."),
  CONSISTENCY_CHECK_CANT_REACH_SERVICE(404, 4042,
      "Unable to reach service {0} which should be available at URL {1}.",
      "Unable to reach service {0} which should be available at URL {1}."),
  CONFIGURATION_NOT_FOUND(404, 4043, "No configuration with id {0} found.",
      "No configuration with id {0} found."),
  TOKEN_NOT_FOUND(404, 4044, "No HttpHeader Authorization token was found.",
      "No HttpHeader Authorization token was found."),
  // 406
  NOT_ACCEPTABLE(406, 4061,
      "The server is unable to serve a request with the media type required by the client. Supported media-types are: {0}.",
      "The server is unable to serve a request with the media type required by the client. Supported media-types are: {0}."),
  // 415
  UNSUPPORTED_MEDIA_TYPE(415, 4151,
      "The value for HTTP header Content-Type is not a valid media type for this endpoint. Supported media-types are: {0}.",
      "The value for HTTP header Content-Type is not a valid media type for this endpoint. Supported media-types are: {0}."),


  //5xx codes
  // 500
  UNKNOWN_REASON_ERROR(500, 5001,
      "The service was unable to fulfill the request for an unknown reason {0}, contact support on mail {1} if the issue persists.",
      "The service was unable to fulfill the request for an unknown reason {0}, contact support on mail {1} if the issue persists."),
  RDFSTORE_COMMUNICATION_ERROR(500, 5002,
      "The rdf store endpoint was unable to carry out the request, the rdf store endpoint answered with message \"{0}\", ensure that the provided data is correct, contact support on mail {2} if the issue persists",
      "The rdf store endpoint at {1} was unable to carry out the request, it responded with message \"{0}\", the user has been told to contact support."),
  RDF_STORE_RESPONSE_NOT_JSON_LD(500, 5003,
      "The rdf store responded with an unrecognized data format, contact support on mail {0} if the issue persists.",
      "The rdf store responded with an unrecognized data format, the user has been told to contact support."),
  IO_ERROR(500, 5004,
      "The service was unable to fulfill the request due to an IO error, contact support on mail {0} if the issue persists.",
      "The service was unable to fulfill the request due to an IO error, see stacktrace for more information."),
  NO_CONTEXT_PRESENT(500, 5005,
      "No context was present, context is required as part of a configuration. The server lost it for an unknown reason.",
      "No context was present, context is required as part of a configuration. The server lost it for an unknown reason."),
  ERROR_DURING_SHACL_VALIDATION(500, 5006,
      "An unknown error occurred in the SHACL validator. Contact support on mail {0} if the issue persists.",
      "An unknown error occurred in the SHACL validator. Contact support on mail, the user has been told to contact support."),
  UNKOWN_ERROR_CRUD_DATABASE(500, 5007,
      "The service was unable to fulfill the request to the database for an unknown reason, {0}, contact support on mail {1} if the issue persists",
      "The service was unable to fulfill the request to the database for an unknown reason, {0} .The user has been told to contact support."),
  S3BUCKET_COMMUNICATION_ERROR(500, 5008,
      "The AWS S3 bucket was unable to carry out the request, answered with message \"{0}\", contact support on mail {1} if the issue persists",
      "The AWS S3 bucket was unable to carry out the request, it responded with message \"{0}\", the user has been told to contact support."),
  CONFIGURATION_ERROR(500, 5009,
      "Something went wrong when retrieving configuration details. Configuration is incomplete and invalid, this should never happen. contact support on mail {0}.",
      "Something went wrong when retrieving configuration details. Configuration is incomplete and invalid, this should never happen."),
  INVALID_SPARQL_QUERY(500, 50010,
      "The platform created an invalid SPARQL-query. Contact support on mail {2} if the issue persists.",
      "The platform created an invalid SPARQL-query, the user has been told to contact support. Query: {0}. Query error: {1}"),
  HTTP_COMMUNICATION_ERROR(500, 50011,
      "The http endpoint was unable to carry out the request, the endpoint answered with message \"{0}\", contact support on mail {2} if the issue persists",
      "The http endpoint was unable to carry out the request, the endpoint answered with message \"{0}\", the user has been told to contact support."),
  SOAP_ERROR(500, 50012,
      "The soap creation failed, answered with message \"{0}\", contact support on mail {1} if the issue persists",
      "The soap creation failed, answered with message \"{0}\", the user has been told to contact support."),
  UNKNOWN_PARSING_ERROR(500, 50013,
      "An unknown Error occured while parsing JSONArray into Model object with the following error message: {0} ",
      "An unknown Error occured while parsing JSONArray into Model object with the following error message: {0} "),

  // 503
  RDFSTORE_UNREACHEBLE(503, 5031,
      "The service was unable to communicate with the rdf store endpoint, contact support on mail {1} if the issue persists.",
      "The service was unable to communicate with the rdf store endpoint {0}, the user has been told to contact support."),
  UNABLE_TO_COMMUNICATE_WITH_DATABASE(503, 5032,
      "Unable to communicate with the configuration persistence layer. Database answered: \"{0}\". Contact support on mail {1} if the issue persists.",
      "Unable to communicate with the configuration persistence layer. Database answered: \"{0}\". The user has been told to contact support."),

  //sdip codes
  BOTH_TOKEN_AND_URL_OR_NEITHER(101,
      "Both -t/--token and -u/--url needs to specified or neither, but never only one of them."),
  FAILED_TO_PARSE_ARGUMENTS(102,
      "Failed to parse arguments from command line due to the following error: {0}"),
  ARGUMENT_NOT_A_VALID_URL(103,
      "The argument for the config host -ch/--configHost is not a valid URL. was: {0}"),
  INVALID_AUTHENTICATION_URL(104,
      "The argument for authentication -u/--url is not a valid URL. The authentication url argument was: {0}."),
  CONFIGURATIONHOST_INFORMATION(105,
      "ConfigurationHost information : {0}."),
  UNSUPPORTED_DATABASE(202,
      "The database string provided is not a supported database, supported databases are mongodb and dynamodb. Run 'java -jar sdcs.jar --help' for more information."),
  UNABLE_TO_CREATE_DYNAMODB_TABLE(203,
      "Failed to create dynamodb table with name: {0}. Got status code: {1}. Check permissions, security groups and other AWS settings and try again."),
  UNABLE_TO_SET_BACKUP_MODE_DYNAMODB(204,
      "Unable to set backup mode to continuous backups for the table {0}. Got status code: {1}. Check permissions, security groups and other AWS settings and try again. "),
  INVALID_DYNAMODB_DATABASE_URL(205,
      "The argument for -db/--database {0} can not be translated into a valid dynamodb url.Observer, example dynamodb://localhost:8000/"),
  RESPONSE_NOT_JSON_DOCUMENT(310,
      "The provided configuration from endpoint {0} does not seem to be json. Got the following error message when parsing: {1}");


  private final int httpStatus;
  private final int sdipCode;
  private String logMessage;
  private String httpMessage;

  private SdipErrorCode(int httpStatus, int sdipCode, String httpMessage, String logMessage) {
    this.httpStatus = httpStatus;
    this.sdipCode = sdipCode;
    this.httpMessage = httpMessage;
    this.logMessage = logMessage;
  }

  private SdipErrorCode(int sdipCode, String logMessage) {
    httpStatus = -1;
    this.sdipCode = sdipCode;
    httpMessage = "ERROR";
    this.logMessage = logMessage;
  }

  @Override
  public int getHttpErrorCode() {
    return httpStatus;
  }

  @Override
  public int getSdipErrorCode() {
    return sdipCode;
  }

  @Override
  public String getLogMessage() {
    return logMessage;
  }

  @Override
  public String getHttpMessage() {
    return httpMessage;
  }

  @Override
  public int getExitCode() {
    return sdipCode;
  }

  @Override
  public void setHttpMessage(String message) {
    httpMessage = message;
  }

  @Override
  public void setLogMessage(String message) {
    logMessage = message;
  }
}
