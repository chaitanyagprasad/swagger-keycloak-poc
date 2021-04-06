# swagger-keycloak-poc
The aim of the project is to not grant direct access to swagger-ui but to authenticate the user and based on their role, grant access.

Keycloak is being used for identity management and the configuration works in conjunction with Spring Security.

The idea is to get Keycloak to authenticate the user for us and based on the role, Spring Security will decide whether or not to provide access to Swagger.

For doing this, I have created a separate realm in keycloak called documentation.
