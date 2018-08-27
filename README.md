**Keycloak Token Authentication**

This is a KeyCloak token authentication service using Open ID Connect protocol, OAuth 2.0.


**KeyCloak Standalone Configuration**

- Download the standalone server distribution zip file from KeyCloak website https://www.keycloak.org/downloads.html, currently the latest version is 4.2.1.Final.
- Extract the zip file keycloak-4.2.1.Final to anywhere and change default port 8080 to any other port at C:\Program Files\keycloak-4.2.1.Final\standalone\configuration\standalone.xml at these lines (for example i set the KeyCloak service port to 8180):
  
            <socket-binding name="http" port="${jboss.http.port:8180}"/>
            <socket-binding name="https" port="${jboss.https.port:8443}"/>            
- Run the standalone.bat file at C:\Program Files\keycloak-4.1.0.Final\bin\standalone.bat.
- Open the browser and go to http://localhost:8080/auth.
- Set the admin username and password for the first time.
- Create new realm by click at 'Add realm' button. Give any display name and leave other field to default then save.
- Add new client by go to 'Clients' section and click 'Create' button. Give the 'Client ID' any name then save.
- Add new role by go to 'Roles' section. Let's just create simple role called 'user'. 
- Add new user by go to 'Users' section. Let's put it 'testuser' and turn off the 'Temporary' flag under 'Credentials' tab. Then go to 'Role Mappings' tab and map the role 'user' to 'Assigned Roles' box.

**Token Service Configuration**

- Download the service from http://192.168.101.51:7990/projects/SA/repos/token/browse or clone it using repository http://zaid.shaharil@192.168.101.51:7990/scm/sa/token.git.
- Set configuration at application.properties for these parameters, for example:

        server.port=8082
        
        keycloak.auth-server-url=http://localhost:8180/auth
        keycloak.realm=SpringBoot
        keycloak.public-client=true
        keycloak.resource=normal
        keycloak.principal-attribute=preferred_username
        
        keycloaks.client-password=d5e59078-bbab-46b9-a48d-ff34abbf517c
        keycloaks.client-id=product-app
        keycloaks.admin-user=admin
        keycloaks.admin-password=password
- To start the program, go to your program directory and run:

        mvnw spring-boot:run
- Refer to list of api below. Test directly using Postman.

**List of API**

1. Create new token
    - Called during login
    - http://localhost:8082/createToken
    - Must add these parameters to the header (all mandatory):
        - username
        - password
    - Check the "result" parameter to be true if success, it will return as below _(the access_token and refresh token must be kept and sent every time make request to microservice)_
    
            {
                "user_id": null,
                "name": null,
                "preferred_username": null,
                "email": null,
                "roles": null,
                "status_code": 200,
                "status_phrase": "OK",
                "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLRHB5b3pxRUg2aGV0bWtQZkhqV0hMZGlpbE9jR3FGcEVSaHR5SEtoWXprIn0.eyJqdGkiOiI1Mjk1ZDU5NC1mOTYxLTQ0NWItYThmMS01MDJmYTA5N2MyMzIiLCJleHAiOjE1MzQyMzI3NjYsIm5iZiI6MCwiaWF0IjoxNTM0MjMyNDY2LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxODAvYXV0aC9yZWFsbXMvU3ByaW5nQm9vdCIsImF1ZCI6InByb2R1Y3QtYXBwIiwic3ViIjoiY2MyZmY2MTMtMDcyNS00YWEzLWFlYjQtYjkzNGQ4MDk1ZDUyIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoicHJvZHVjdC1hcHAiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiJlODVlYzJjYy01MjZiLTQzNzctOTlhMS1jNmYyOTI0YTM4OWUiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsInZpZXctcmVhbG0iLCJtYW5hZ2UtaWRlbnRpdHktcHJvdmlkZXJzIiwiaW1wZXJzb25hdGlvbiIsInJlYWxtLWFkbWluIiwiY3JlYXRlLWNsaWVudCIsIm1hbmFnZS11c2VycyIsInF1ZXJ5LXJlYWxtcyIsInZpZXctYXV0aG9yaXphdGlvbiIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS11c2VycyIsIm1hbmFnZS1ldmVudHMiLCJtYW5hZ2UtcmVhbG0iLCJ2aWV3LWV2ZW50cyIsInZpZXctdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJtYW5hZ2UtYXV0aG9yaXphdGlvbiIsIm1hbmFnZS1jbGllbnRzIiwicXVlcnktZ3JvdXBzIl19LCJwcm9kdWN0LWFwcCI6eyJyb2xlcyI6WyJ1bWFfcHJvdGVjdGlvbiJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIHJvbGVfbGlzdCBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwidXNlcl9pZCI6ImNjMmZmNjEzLTA3MjUtNGFhMy1hZWI0LWI5MzRkODA5NWQ1MiIsIm5hbWUiOiJNdWhhbW1hZCBaYWlkIFNoYWhhcmlsIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidGVzdHVzZXIiLCJnaXZlbl9uYW1lIjoiTXVoYW1tYWQgWmFpZCIsImZhbWlseV9uYW1lIjoiU2hhaGFyaWwiLCJlbWFpbCI6ImhlbXBwb2tAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsidW1hX2F1dGhvcml6YXRpb24iLCJ1c2VyIiwib2ZmbGluZV9hY2Nlc3MiXX0.ZwBeYrqs-SqsSEsW6_V4hQ3pHHypecxYJ3f9h3ZYoPoMtlTLm15bAs5AFEiE9V9nf9W61823UNCFcRHHy1R4wACRtskxu9JXoGX6rPubnML1VjyDIEBBL8Nvo_rkKg94RvASswKT8gD5qPua-hUFZzF1mdiyx9UVV6j_eZhR7PoRauKIyXPvN5VVFMjfOfvFNMxeFcq5KQCYAt08KdeA1jKhe94ufs15dHck_jeEwvz5m9LCKBNC3i7AYPZv_ZBMJWR23LDPPF-VKSkSWBAazFR5a9zIRNwDMlpx0UaZ2PgC5cAiIOyhu2O3b-V02cG2WCN_QvY9Ljqqz032h_Dycg",
                "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLRHB5b3pxRUg2aGV0bWtQZkhqV0hMZGlpbE9jR3FGcEVSaHR5SEtoWXprIn0.eyJqdGkiOiIwZTI3ZmI1Ni0xMzFkLTRlY2UtOTJiMi0xOTgzNThjM2UyZTUiLCJleHAiOjE1MzQyMzQyNjYsIm5iZiI6MCwiaWF0IjoxNTM0MjMyNDY2LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxODAvYXV0aC9yZWFsbXMvU3ByaW5nQm9vdCIsImF1ZCI6InByb2R1Y3QtYXBwIiwic3ViIjoiY2MyZmY2MTMtMDcyNS00YWEzLWFlYjQtYjkzNGQ4MDk1ZDUyIiwidHlwIjoiUmVmcmVzaCIsImF6cCI6InByb2R1Y3QtYXBwIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiZTg1ZWMyY2MtNTI2Yi00Mzc3LTk5YTEtYzZmMjkyNGEzODllIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsInZpZXctcmVhbG0iLCJtYW5hZ2UtaWRlbnRpdHktcHJvdmlkZXJzIiwiaW1wZXJzb25hdGlvbiIsInJlYWxtLWFkbWluIiwiY3JlYXRlLWNsaWVudCIsIm1hbmFnZS11c2VycyIsInF1ZXJ5LXJlYWxtcyIsInZpZXctYXV0aG9yaXphdGlvbiIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS11c2VycyIsIm1hbmFnZS1ldmVudHMiLCJtYW5hZ2UtcmVhbG0iLCJ2aWV3LWV2ZW50cyIsInZpZXctdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJtYW5hZ2UtYXV0aG9yaXphdGlvbiIsIm1hbmFnZS1jbGllbnRzIiwicXVlcnktZ3JvdXBzIl19LCJwcm9kdWN0LWFwcCI6eyJyb2xlcyI6WyJ1bWFfcHJvdGVjdGlvbiJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIHJvbGVfbGlzdCBlbWFpbCJ9.f-ezNs8nLfM4yA4gH25oPTYu6_QbtuQGZJKh8bpG8e7sq6PimnyjKA30WBzXYALRhlE0NHJnLGY_HntEDDgItZUrVRGx5sbx8RJZeBVPxFqd_zwGm4C2RARcZhhpS4oJDOVt26z7WMGBYGzaiMWmOYi08NGm3fWkXI6GUK8R5sq-yDuDsiBE3afe4mpSPuD-CmYuTIYwB0eim-7GfXfllch4gsUxtIRD09nYkgFNOJpbgJVaGAKK_sGuxvppqpTnvnCDAuNCQOCekZOoVW81g-9b53wyzuu8QDEeKLkiov-RTHIEd_cwjR4j81I841YWS_hSrLuEOtywUVOFhYTACQ",
                "result": true
            }
    - If failed because of invalid username, wrong password, invalid client ID or client secret, it will return like this:
    
            {
                "user_id": null,
                "name": null,
                "preferred_username": null,
                "email": null,
                "roles": null,
                "status_code": 401,
                "status_phrase": "Unauthorized",
                "access_token": null,
                "refresh_token": null,
                "result": false
            } 

2. Check token validity
    - Should be called / intercepted every time accessing to API or microservices
    - http://localhost:8082/checkToken
        - Set the access token in the Autherization Type 'OAuth 2.0' 
        - Add header parameter key=refresh_token, value=<refresh_token>
    - If success, it will return:
    
            {
                "user_id": "cc2ff613-0725-4aa3-aeb4-b934d8095d52",
                "name": "Muhammad Zaid Shaharil",
                "preferred_username": "testuser",
                "email": "hemppok@gmail.com",
                "roles": [
                    "uma_authorization",
                    "user",
                    "offline_access"
                ],
                "status_code": 200,
                "status_phrase": "OK",
                "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLRHB5b3pxRUg2aGV0bWtQZkhqV0hMZGlpbE9jR3FGcEVSaHR5SEtoWXprIn0.eyJqdGkiOiIzMmQ0NGU3MC0zZTg0LTRmN2QtYTM0Yi00MDYzZDhkZTczY2IiLCJleHAiOjE1MzQxNDAxMzYsIm5iZiI6MCwiaWF0IjoxNTM0MTM5ODM2LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxODAvYXV0aC9yZWFsbXMvU3ByaW5nQm9vdCIsImF1ZCI6InByb2R1Y3QtYXBwIiwic3ViIjoiY2MyZmY2MTMtMDcyNS00YWEzLWFlYjQtYjkzNGQ4MDk1ZDUyIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoicHJvZHVjdC1hcHAiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiI1ZDhiMzdlZS1jOTk1LTRjOWEtYTQ1MS0wYzYzM2Y1ZmQxZGIiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsInZpZXctcmVhbG0iLCJtYW5hZ2UtaWRlbnRpdHktcHJvdmlkZXJzIiwiaW1wZXJzb25hdGlvbiIsInJlYWxtLWFkbWluIiwiY3JlYXRlLWNsaWVudCIsIm1hbmFnZS11c2VycyIsInF1ZXJ5LXJlYWxtcyIsInZpZXctYXV0aG9yaXphdGlvbiIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS11c2VycyIsIm1hbmFnZS1ldmVudHMiLCJtYW5hZ2UtcmVhbG0iLCJ2aWV3LWV2ZW50cyIsInZpZXctdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJtYW5hZ2UtYXV0aG9yaXphdGlvbiIsIm1hbmFnZS1jbGllbnRzIiwicXVlcnktZ3JvdXBzIl19LCJwcm9kdWN0LWFwcCI6eyJyb2xlcyI6WyJ1bWFfcHJvdGVjdGlvbiJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIHJvbGVfbGlzdCBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwidXNlcl9pZCI6ImNjMmZmNjEzLTA3MjUtNGFhMy1hZWI0LWI5MzRkODA5NWQ1MiIsIm5hbWUiOiJNdWhhbW1hZCBaYWlkIFNoYWhhcmlsIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidGVzdHVzZXIiLCJnaXZlbl9uYW1lIjoiTXVoYW1tYWQgWmFpZCIsImZhbWlseV9uYW1lIjoiU2hhaGFyaWwiLCJlbWFpbCI6ImhlbXBwb2tAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsidW1hX2F1dGhvcml6YXRpb24iLCJ1c2VyIiwib2ZmbGluZV9hY2Nlc3MiXX0.B6DFFXn4N2KpC9S42r65WhWUT2nnBcYVGmTUcATByB3otc2eoInlHlxFY5WS6XXekCU4QU043z-x1xLoayPnDqvFI-m64VxzfHrGk5yDVi7dNfXEOJj7rVgCBFYU3MUYA2_VZArgpx8sNSa5JTRbji2bTUG1OxRQ5EFA-ii1AzfkR7SVCfEzv2n83Y4E3E7dc64JKD9u-0qyVgtKKoRMfKiJgVzXgppJAa-KCwoWWZlaVtQ5Enae8Xk06MKPtUXQZJy-N62DAD63okmk8mZq76fTPrPcSbgX-GBR057vecGWzM75nj8pu3uHumfcIXPl9PWUkDrVZFxBCM0vYQUD5g",
                "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLRHB5b3pxRUg2aGV0bWtQZkhqV0hMZGlpbE9jR3FGcEVSaHR5SEtoWXprIn0.eyJqdGkiOiI2NTJiMzgwMC0xZDg3LTRmZjItYmYxNi0zNmViOTVhY2YzYzIiLCJleHAiOjE1MzQxNDE2MzYsIm5iZiI6MCwiaWF0IjoxNTM0MTM5ODM2LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxODAvYXV0aC9yZWFsbXMvU3ByaW5nQm9vdCIsImF1ZCI6InByb2R1Y3QtYXBwIiwic3ViIjoiY2MyZmY2MTMtMDcyNS00YWEzLWFlYjQtYjkzNGQ4MDk1ZDUyIiwidHlwIjoiUmVmcmVzaCIsImF6cCI6InByb2R1Y3QtYXBwIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiNWQ4YjM3ZWUtYzk5NS00YzlhLWE0NTEtMGM2MzNmNWZkMWRiIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsInZpZXctcmVhbG0iLCJtYW5hZ2UtaWRlbnRpdHktcHJvdmlkZXJzIiwiaW1wZXJzb25hdGlvbiIsInJlYWxtLWFkbWluIiwiY3JlYXRlLWNsaWVudCIsIm1hbmFnZS11c2VycyIsInF1ZXJ5LXJlYWxtcyIsInZpZXctYXV0aG9yaXphdGlvbiIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS11c2VycyIsIm1hbmFnZS1ldmVudHMiLCJtYW5hZ2UtcmVhbG0iLCJ2aWV3LWV2ZW50cyIsInZpZXctdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJtYW5hZ2UtYXV0aG9yaXphdGlvbiIsIm1hbmFnZS1jbGllbnRzIiwicXVlcnktZ3JvdXBzIl19LCJwcm9kdWN0LWFwcCI6eyJyb2xlcyI6WyJ1bWFfcHJvdGVjdGlvbiJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIHJvbGVfbGlzdCBlbWFpbCJ9.dg7T2ToNzwS6n3V4xApgd8tjroOwwLl0u3AugwhsPDJ74107VdvIIXftuUOu7JJtbms-7Q5EN5UEwRTct4WXOP0oTtdFNPpcaXYkhHs6LswfDH6RSYdEVlicwkN-BMw1llksi9J6KD5HfIs05Lx7qlPrRUKpE-AbnKNdHOZj8kzmqeikxaiX6C6yx5H7xta3U0-gqd4uhPCenbW8IrGNmGPgeAEkZ_pG4zSY-oSoKlmRtChBQJol516NKh7x-FPeg40y3pjB6io86sFWidD_-4owohGKi30V2ADeUjNoxXLs7iQaOFrpDbiL7OyVMbDq1RxdgOhrfw5tUHeowqhfIg",
                "result": true
            }
    - If invalid token or refresh token expired, it will return:
    
            {
                "user_id": null,
                "name": null,
                "preferred_username": null,
                "email": null,
                "roles": null,
                "status_code": 400,
                "status_phrase": "Bad Request",
                "access_token": null,
                "refresh_token": null,
                "result": false
            }
    - By default, the access token will expired if idle for 5 minutes. Then it will use the refresh_token to create new access token. The refresh token will be expired in 15 minutes by default.
    - Every time if return success, the access_token and refresh_token from return value must be used and overwritten at the header for the next request.
          
3. Create new user
    - Should be called when new user register or created by administrator
    - http://localhost:8082/createUser
    - Must add these parameters to the header (all mandatory):
        - username
        - password
        - firstName
        - lastName
        - email
        - role
    - If success, it will return the keyCloak user-id _(for example e59b74ce-0d85-43dd-bebc-3c9476b8db3c)_ and should be kept and saved. The user-id is the reference key to that particular user and needed when update info, update role and change password at the keyCloak.
    - If fail, it will return:
        - 409 - user already created or role not exist
        - 801 - parameter 'username' not provided or empty
        - 802 - parameter 'password' not provided or empty
        - 803 - parameter 'firstName' not provided or empty
        - 804 - parameter 'lastName' not provided or empty
        - 805 - parameter 'email' not provided or empty
        - 806 - parameter 'role' not provided or empty

4. Change user password
    - Should be called when user change password or administrator reset the user password.
    - http://localhost:8082/changePassword
    - Must add these parameters to the header (all mandatory):
        - user_id _(example e59b74ce-0d85-43dd-bebc-3c9476b8db3c)_
        - password
    - If success will return true
    - If fail will return false

5. Update user information
    - Should be used when user or administrator update user's email, first name or last name.
    - http://localhost:8082/updateUser
    - Must add these parameters to the header (all mandatory):
        - user_id _(example e59b74ce-0d85-43dd-bebc-3c9476b8db3c)_
        - email
        - firstName
        - lastName
    - If success will return true
    - If fail will return false

6. Disable or enable user
    - Should be used when administrator want to disable or delete user account, and also enable it back.
    - http://localhost:8082/enableUser
    - Must add these parameters to the header (all mandatory):
        - user_id _(example e59b74ce-0d85-43dd-bebc-3c9476b8db3c)_
        - is_enabled (must be '1' if to enable or '0' if to disable)
    - If success will return true
    - If fail will return false

7. Add new role to user
    - Should be used when administrator want to add new role to the user
    - http://localhost:8082/addRole
    - Must add these parameters to the header (all mandatory):
        - user_id _(example e59b74ce-0d85-43dd-bebc-3c9476b8db3c)_
        - role
    - If success will return true
    - If fail will return false

8. Remove existing role from user
    - Should be used when administrator want to remove existing role from the user
    - http://localhost:8082/removeRole
    - Must add these parameters to the header (all mandatory):
        - user_id _(example e59b74ce-0d85-43dd-bebc-3c9476b8db3c)_
        - role
    - If success will return true
    - If fail will return false
    
9. Logout the token session
    - Should be used when user logout the system
    - http://localhost:8082/logout
    - Must add these parameters to the header (all mandatory):
        - access_token (as Authorization header type OAuth 2.0, in the format of "Bearer: "+<access_token>)
        - refresh_token