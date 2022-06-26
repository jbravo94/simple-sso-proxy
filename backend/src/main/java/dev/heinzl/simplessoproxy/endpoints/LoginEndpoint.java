package dev.heinzl.simplessoproxy.endpoints;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/api/v1")
public class LoginEndpoint {

    // @Autowired
    // @Qualifier("authenticationManager")
    // protected AuthenticationManager authenticationManager;

    public LoginEndpoint() {
        super();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestParam("username") final String username,
            @RequestParam("password") final String password) {
        /*
         * UsernamePasswordAuthenticationToken authReq = new
         * UsernamePasswordAuthenticationToken(username, password);
         * Authentication auth = authenticationManager.authenticate(authReq);
         * SecurityContext sc = SecurityContextHolder.getContext();
         * sc.setAuthentication(auth);
         */
    }

}