package com.tongue.merchantservice.auth;

import com.tongue.merchantservice.domain.Merchant;
import com.tongue.merchantservice.services.MerchantAuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {



    private String secretKey;
    private String AuthorizationHeader;
    private String AuthorizationPrefix;
    private String JwtRequestParameter;
    private RememberMeServices rememberMeServices;
    private MerchantAuthenticationService authenticationService;

    public JwtAuthorizationFilter(@Autowired MerchantAuthenticationService authenticationService,
                                           @Value("${security.jwt.secret.key}") String secretKey){
        this.authenticationService=authenticationService;
        this.secretKey=secretKey;
        this.AuthorizationHeader="Authorization";
        this.AuthorizationPrefix="Bearer ";
        this.JwtRequestParameter="jwtToken";
        this.rememberMeServices = new NullRememberMeServices();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        doFilter(httpServletRequest,httpServletResponse,filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        log.trace("Jwt Authorization filter called");

        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a!=null){
            log.trace("Aborting since the user is already authenticated");
            chain.doFilter(request,response);
            return;
        }

        log.trace("Searching JWT in the HTTP Authorization Header...");
        String jwt;
        jwt = request.getHeader(AuthorizationHeader);
        if (jwt!=null){
            jwt = jwt.replace(AuthorizationPrefix,"");
        }else {
            log.trace("Searching JWT in HTTP request parameters...");
            jwt = request.getParameter(JwtRequestParameter);
        }

        if (jwt==null){
            log.trace("No JWT found either in HTTP Request Parameters and Authorization Header");
            chain.doFilter(request,response);
            return;
        }

        /**
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(jwt)
                .getBody();

        Authentication authentication = authenticationFromClaims(claims);
         **/

        log.trace("Authenticating...");

        Merchant merchant = authenticationService.getMerchantFromJwt(jwt);
        Authentication authentication = mapMerchantToAuthentication(merchant);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        this.rememberMeServices.loginSuccess(request,response,authentication);
        log.trace("Successful authentication");
        chain.doFilter(request,response);

    }

    private Authentication authenticationFromClaims(Claims claims){
        String username = claims.getSubject();
        List<String> authoritiesList = (List<String>) claims.get("authorities");
        List<GrantedAuthority> grantedAuthorities = authoritiesList
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(username,username,grantedAuthorities);
        return authentication;
    }

    private Authentication mapMerchantToAuthentication(Merchant merchant){
        String username = merchant.getId().toString();
        List<String> authorities = Arrays.asList("MERCHANT");
        List<GrantedAuthority> grantedAuthorities = authorities
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(username,username,grantedAuthorities);
        return authentication;
    }



}
