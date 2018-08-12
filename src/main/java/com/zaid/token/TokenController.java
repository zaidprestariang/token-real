package com.zaid.token;


import com.zaid.token.keyCloakService.KeycloakService;
import com.zaid.token.keyCloakService.TokenDetail;
import com.zaid.token.keyCloakService.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TokenController {

    @Autowired
    private KeycloakService keycloakService;

    @RequestMapping("/checkToken")
    public TokenResponse checkToken(@RequestHeader String Authorization, @RequestHeader String refresh_token) throws IOException {
        TokenResponse tokenResponse = new TokenResponse();
        try {
            Authorization = Authorization.replace("Bearer ", "");
            tokenResponse = keycloakService.verifyToken(Authorization, refresh_token);
        } catch (Exception ex) {
            tokenResponse.setResult(false);
            tokenResponse.setStatus_phrase(ex.getMessage());
        }
        return tokenResponse;
    }

    @RequestMapping("/createToken/{username}/{password}")
    public TokenResponse createToken(@PathVariable String username, @PathVariable String password)  throws IOException {
        TokenResponse tokenResponse = new TokenResponse();
        try {
            tokenResponse = keycloakService.createToken(username, password);
        } catch (Exception ex) {
            tokenResponse.setResult(false);
            tokenResponse.setStatus_phrase(ex.getMessage());
        }
        return tokenResponse;
    }

    @RequestMapping("/createUser")
    public String createUser(@RequestHeader HttpHeaders headers) {
        TokenDetail tokenDetail = new TokenDetail(headers.get("username").get(0), headers.get("password").get(0), headers.get("firstName").get(0), headers.get("lastName").get(0), headers.get("email").get(0), headers.get("role").get(0), true);
        return keycloakService.createUser(tokenDetail);
    }

    @RequestMapping("/changePassword")
    public boolean changePassword(@RequestHeader HttpHeaders headers) {
        return keycloakService.changePassword(headers.get("user_id").get(0), headers.get("password").get(0));
    }

    @RequestMapping("/updateUser")
    public boolean updateUser(@RequestHeader HttpHeaders headers) {
        TokenDetail tokenDetail = new TokenDetail();
        tokenDetail.setFirstName(headers.get("firstName").get(0));
        tokenDetail.setLastName(headers.get("lastName").get(0));
        tokenDetail.setEmail(headers.get("email").get(0));
        return keycloakService.updateUser(headers.get("user_id").get(0), tokenDetail);
    }

    @RequestMapping("/enableUser")
    public boolean enableUser(@RequestHeader HttpHeaders headers){
        System.out.println("Enabled = "+headers.get("is_enabled").get(0));
        return keycloakService.enableUser(headers.get("user_id").get(0), headers.get("is_enabled").get(0).equals("1"));
    }

    @RequestMapping("/addRole")
    public boolean addRole(@RequestHeader HttpHeaders headers){
        return keycloakService.addRole(headers.get("user_id").get(0), headers.get("role").get(0));
    }

    @RequestMapping("/removeRole")
    public boolean removeRole(@RequestHeader HttpHeaders headers){
        return keycloakService.removeRole(headers.get("user_id").get(0), headers.get("role").get(0));
    }
}

