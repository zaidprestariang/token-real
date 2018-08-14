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
    public TokenResponse createToken(@PathVariable String username, @PathVariable String password) {
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
        if (null == headers.get("username") || headers.get("username").get(0) == "") {
            return "801";
        } else if (null == headers.get("password") || headers.get("password").get(0) == "") {
            return "802";
        } else if (null == headers.get("firstName") || headers.get("firstName").get(0) == "") {
            return "803";
        } else if (null == headers.get("lastName") || headers.get("lastName").get(0) == "") {
            return "804";
        } else if (null == headers.get("email") || headers.get("email").get(0) == "") {
            return "805";
        } else if (null == headers.get("role") || headers.get("role").get(0) == "") {
            return "806";
        }
        TokenDetail tokenDetail = new TokenDetail(headers.get("username").get(0), headers.get("password").get(0), headers.get("firstName").get(0), headers.get("lastName").get(0), headers.get("email").get(0), headers.get("role").get(0), true);
        return keycloakService.createUser(tokenDetail);
    }

    @RequestMapping("/changePassword")
    public boolean changePassword(@RequestHeader HttpHeaders headers) {
        if (null == headers.get("user_id") || headers.get("user_id").get(0) == "") {
            return false;
        } else if (null == headers.get("password") || headers.get("password").get(0) == "") {
            return false;
        }
        return keycloakService.changePassword(headers.get("user_id").get(0), headers.get("password").get(0));
    }

    @RequestMapping("/updateUser")
    public boolean updateUser(@RequestHeader HttpHeaders headers) {
        if (null == headers.get("user_id") || headers.get("user_id").get(0) == "") {
            return false;
        } else if (null == headers.get("firstName") || headers.get("firstName").get(0) == "") {
            return false;
        } else if (null == headers.get("lastName") || headers.get("lastName").get(0) == "") {
            return false;
        } else if (null == headers.get("email") || headers.get("email").get(0) == "") {
            return false;
        }
        TokenDetail tokenDetail = new TokenDetail();
        tokenDetail.setFirstName(headers.get("firstName").get(0));
        tokenDetail.setLastName(headers.get("lastName").get(0));
        tokenDetail.setEmail(headers.get("email").get(0));
        return keycloakService.updateUser(headers.get("user_id").get(0), tokenDetail);
    }

    @RequestMapping("/enableUser")
    public boolean enableUser(@RequestHeader HttpHeaders headers){
        if (null == headers.get("user_id") || headers.get("user_id").get(0) == "") {
            return false;
        } else if (null == headers.get("is_enabled") || !(headers.get("is_enabled").get(0).equals("1") || headers.get("is_enabled").get(0).equals("0"))) {
            return false;
        }
        return keycloakService.enableUser(headers.get("user_id").get(0), headers.get("is_enabled").get(0).equals("1"));
    }

    @RequestMapping("/addRole")
    public boolean addRole(@RequestHeader HttpHeaders headers){
        if (null == headers.get("user_id") || headers.get("user_id").get(0) == "") {
            return false;
        } else if (null == headers.get("role") || headers.get("role").get(0) == "") {
            return false;
        }
        return keycloakService.addRole(headers.get("user_id").get(0), headers.get("role").get(0));
    }

    @RequestMapping("/removeRole")
    public boolean removeRole(@RequestHeader HttpHeaders headers){
        if (null == headers.get("user_id") || headers.get("user_id").get(0) == "") {
            return false;
        } else if (null == headers.get("role") || headers.get("role").get(0) == "") {
            return false;
        }
        return keycloakService.removeRole(headers.get("user_id").get(0), headers.get("role").get(0));
    }
}

