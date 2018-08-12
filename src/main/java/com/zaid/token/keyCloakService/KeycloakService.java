package com.zaid.token.keyCloakService;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.keycloak.admin.client.CreatedResponseUtil.getCreatedId;

@Service
public class KeycloakService {

    public TokenResponse infoToken(String access_token){
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setResult(false);
        try {
            System.out.println("Start KeycloakService:infoToken(access_token = " + access_token + ")");
            String uri = "http://localhost:8180/auth/realms/SpringBoot/protocol/openid-connect/userinfo";

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(uri);
            post.setHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
            List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
            urlParameters.add(new BasicNameValuePair("client_id", "product-app"));
            urlParameters.add(new BasicNameValuePair("access_token", access_token));

            try {
                post.setEntity(new UrlEncodedFormEntity(urlParameters));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpResponse httpResponse = null;
            httpResponse = client.execute(post);
            StringBuffer result = new StringBuffer();
            tokenResponse.setStatus_code(httpResponse.getStatusLine().getStatusCode());
            tokenResponse.setStatus_phrase(httpResponse.getStatusLine().getReasonPhrase());
            System.out.println("Response Code : " + tokenResponse.getStatus_code() + " : " + tokenResponse.getStatus_phrase());

            if (tokenResponse.getStatus_code() == 200) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                String line1 = "";
                while ((line1 = rd.readLine()) != null) {
                    result.append(line1);
                }
                JSONObject parse_result = new JSONObject(result.toString());
                tokenResponse.setUser_id(parse_result.getString("sub"));
                tokenResponse.setName(parse_result.getString("name"));
                tokenResponse.setPreferred_username(parse_result.getString("preferred_username"));
                tokenResponse.setEmail(parse_result.getString("email"));
                tokenResponse.setResult(true);
            }
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return tokenResponse;
    }

    public TokenResponse verifyToken(String access_token, String refresh_token) throws IOException {
        TokenResponse tokenResponse = new TokenResponse();
        boolean response = false;
        try {
            System.out.println("Start KeycloakService:verifyToken(access_token = " + access_token + ")");

            tokenResponse = infoToken(access_token);
            if (tokenResponse.isResult()) {
                tokenResponse.setAccess_token(access_token);
                tokenResponse.setRefresh_token(refresh_token);
                response = true;
            } else {
                tokenResponse = new TokenResponse();
                String uri = "http://localhost:8180/auth/realms/SpringBoot/protocol/openid-connect/token";
                System.out.println("Start refresh token: " + refresh_token);
                HttpClient client = HttpClientBuilder.create().build();
                HttpPost post = new HttpPost(uri);
                post.setHeader("User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
                List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
                urlParameters = new ArrayList<BasicNameValuePair>();
                urlParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
                urlParameters.add(new BasicNameValuePair("client_id", "product-app"));
                urlParameters.add(new BasicNameValuePair("client_secret", "d5e59078-bbab-46b9-a48d-ff34abbf517c"));
                urlParameters.add(new BasicNameValuePair("refresh_token", refresh_token));

                try {
                    post.setEntity(new UrlEncodedFormEntity(urlParameters));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                HttpResponse httpResponse = null;
                httpResponse = client.execute(post);
                tokenResponse.setStatus_code(httpResponse.getStatusLine().getStatusCode());
                tokenResponse.setStatus_phrase(httpResponse.getStatusLine().getReasonPhrase());
                System.out.println("Response Code : " + tokenResponse.getStatus_code() + " : " + tokenResponse.getStatus_phrase());

                BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                StringBuffer result = new StringBuffer();
                String line1 = "";
                while ((line1 = rd.readLine()) != null) {
                    result.append(line1);
                }

                JSONObject parse_result = new JSONObject(result.toString());
                System.out.println("New access token : " + parse_result.get("access_token"));
                System.out.println("New refresh token : " + parse_result.get("access_token"));

                tokenResponse = infoToken(parse_result.get("access_token").toString());
                tokenResponse.setAccess_token(parse_result.get("access_token").toString());
                tokenResponse.setRefresh_token(parse_result.get("refresh_token").toString());
                response = true;
            }
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        tokenResponse.setResult(response);
        return tokenResponse;
    }

    public TokenResponse createToken(String username, String password) throws IOException {
        TokenResponse tokenResponse = new TokenResponse();
        String uri = "http://localhost:8180/auth/realms/SpringBoot/protocol/openid-connect/token";

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(uri);
        post.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
        urlParameters.add(new BasicNameValuePair("grant_type", "password"));
        urlParameters.add(new BasicNameValuePair("client_id", "product-app"));
        urlParameters.add(new BasicNameValuePair("username", username));
        urlParameters.add(new BasicNameValuePair("password", password));
        urlParameters.add(new BasicNameValuePair("client_secret", "d5e59078-bbab-46b9-a48d-ff34abbf517c"));

        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpResponse response = null;
        response = client.execute(post);
        tokenResponse.setStatus_code(response.getStatusLine().getStatusCode());
        tokenResponse.setStatus_phrase(response.getStatusLine().getReasonPhrase());
        System.out.println("Response Code : " + tokenResponse.getStatus_code() + " : " + tokenResponse.getStatus_phrase());

        if (response.getStatusLine().getStatusCode() == 200) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line1 = "";
            while ((line1 = rd.readLine()) != null) {
                result.append(line1);
            }

            JSONObject parse_result = new JSONObject(result.toString());
            tokenResponse.setAccess_token(parse_result.getString("access_token"));
            tokenResponse.setRefresh_token(parse_result.getString("refresh_token"));
            System.out.println("Access Token = " + tokenResponse.getAccess_token());
            System.out.println("Refresh Token = " + tokenResponse.getRefresh_token());
        }
        return tokenResponse;
    }

    public String createUser(TokenDetail tokenDetail) {
        System.out.println("Username = " + tokenDetail.getUsername());
        System.out.println("Password = " + tokenDetail.getPassword());
        System.out.println("FirstName = " + tokenDetail.getFirstName());
        System.out.println("LastName = " + tokenDetail.getLastName());
        System.out.println("Email = " + tokenDetail.getEmail());
        System.out.println("Role = " + tokenDetail.getRole());
        System.out.println("Enabled = " + tokenDetail.getEnabled());

        Keycloak keycloak = Keycloak.getInstance("http://localhost:8180/auth", "master", "admin", "Amlyd654987", "admin-cli");

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(tokenDetail.getUsername());
        userRepresentation.setFirstName(tokenDetail.getFirstName());
        userRepresentation.setLastName(tokenDetail.getLastName());
        userRepresentation.setEmail(tokenDetail.getEmail());
        userRepresentation.setEnabled(tokenDetail.getEnabled());

        Response createUserResponse = keycloak.realm("SpringBoot").users().create(userRepresentation);
        createUserResponse.close();
        String user_id;
        user_id = getCreatedId(createUserResponse);

        System.out.printf("User created with id: %s%n", user_id);

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setValue(tokenDetail.getPassword());
        passwordCred.setType(CredentialRepresentation.PASSWORD);

        keycloak.realm("SpringBoot").users().get(user_id).resetPassword(passwordCred);
        System.out.printf("Password for user with id: %s reseted%n", user_id);

        RoleRepresentation userRealmRole = keycloak.realm("SpringBoot").roles().get(tokenDetail.getRole()).toRepresentation();
        keycloak.realm("SpringBoot").users().get(user_id).roles().realmLevel().add(Arrays.asList(userRealmRole));
        System.out.printf("Granted user realm role to user with id: %s%n", user_id);

        return user_id;
    }

    public boolean changePassword(String user_id, String password) {
        boolean response = false;
        try{
            Keycloak keycloak = Keycloak.getInstance("http://localhost:8180/auth", "master", "admin", "Amlyd654987", "admin-cli");
            UserResource resource = keycloak.realm("SpringBoot").users().get(user_id);
            CredentialRepresentation newCredential  = new CredentialRepresentation();
            newCredential.setValue(password);
            newCredential.setType(CredentialRepresentation.PASSWORD);
            newCredential.setTemporary(false);
            resource.resetPassword(newCredential);
            response = true;
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return response;
    }

    public boolean updateUser(String user_id, TokenDetail tokenDetail){
        boolean response = false;
        try{
            Keycloak keycloak = Keycloak.getInstance("http://localhost:8180/auth", "master", "admin", "Amlyd654987", "admin-cli");
            UserResource resource = keycloak.realm("SpringBoot").users().get(user_id);
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setUsername(tokenDetail.getUsername());
            userRepresentation.setFirstName(tokenDetail.getFirstName());
            userRepresentation.setLastName(tokenDetail.getLastName());
            userRepresentation.setEmail(tokenDetail.getEmail());
            resource.update(userRepresentation);
            response = true;
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return response;
    }

    public boolean enableUser(String user_id, boolean is_enabled){
        boolean response = false;
        try{
            System.out.println("User ID = " + user_id);
            System.out.println("Enabled = " + is_enabled);
            Keycloak keycloak = Keycloak.getInstance("http://localhost:8180/auth", "master", "admin", "Amlyd654987", "admin-cli");
            UserResource resource = keycloak.realm("SpringBoot").users().get(user_id);
            UserRepresentation userRepresentation = new UserRepresentation();
            userRepresentation.setEnabled(is_enabled);
            resource.update(userRepresentation);
            response = true;
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return response;
    }

    public boolean addRole(String user_id, String role){
        boolean response = false;
        try{
            Keycloak keycloak = Keycloak.getInstance("http://localhost:8180/auth", "master", "admin", "Amlyd654987", "admin-cli");

            RoleRepresentation userRealmRole = keycloak.realm("SpringBoot").roles().get(role).toRepresentation();
            keycloak.realm("SpringBoot").users().get(user_id).roles().realmLevel().add(Arrays.asList(userRealmRole));
            response = true;
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return response;
    }

    public boolean removeRole(String user_id, String role){
        boolean response = false;
        try{
            Keycloak keycloak = Keycloak.getInstance("http://localhost:8180/auth", "master", "admin", "Amlyd654987", "admin-cli");

            RoleRepresentation userRealmRole = keycloak.realm("SpringBoot").roles().get(role).toRepresentation();
            keycloak.realm("SpringBoot").users().get(user_id).roles().realmLevel().remove(Arrays.asList(userRealmRole));
            response = true;
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
        }
        return response;
    }

}
