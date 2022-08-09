package com.tongue.merchantservice.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.tongue.merchantservice.auth.dto.AuthenticationCredentials;
import com.tongue.merchantservice.auth.dto.GoogleAuthenticationCredentials;
import com.tongue.merchantservice.core.exceptions.MalformedGoogleIdTokenException;
import com.tongue.merchantservice.core.exceptions.NoSuchEmailRegisteredException;
import com.tongue.merchantservice.core.exceptions.WrongPasswordException;
import com.tongue.merchantservice.domain.Merchant;
import com.tongue.merchantservice.repositories.MerchantRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MerchantAuthenticationService {

    private MerchantRepository merchantRepository;
    private GoogleIdTokenVerifier tokenVerifier;
    private String secretKey;
    private String googleOauthClientId;

    public MerchantAuthenticationService(@Autowired MerchantRepository merchantRepository,
                                         @Value("${security.jwt.secret.key}") String secretKey,
                                         @Value("${security.google.client.id}") String googleOauthClientId){

        GooglePublicKeysManager publicKeysManager =
                new GooglePublicKeysManager(new NetHttpTransport(),new GsonFactory());
        tokenVerifier = new GoogleIdTokenVerifier.Builder(publicKeysManager)
                .setAudience(Collections.singletonList(googleOauthClientId))
                .build();

        this.merchantRepository=merchantRepository;
        this.secretKey=secretKey;
        this.googleOauthClientId=googleOauthClientId;
    }

    public String authenticateAndReturnJwt(AuthenticationCredentials credentials){
        Optional<Merchant> wrapper = merchantRepository.findByEmail(credentials.getEmail());
        if (wrapper.isEmpty())
            throw new NoSuchEmailRegisteredException();
        Merchant merchant = wrapper.get();
        if (!merchant.getPassword().equalsIgnoreCase(credentials.getPassword()))
            throw new WrongPasswordException();
        return generateJwt(credentials.getEmail());
    }

    public Merchant getMerchantFromJwt(String jwt){
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(jwt)
                .getBody();
        String username = claims.getSubject();
        Optional<Merchant> wrapper = merchantRepository.findByEmail(username);
        if (wrapper.isEmpty())
            throw new NoSuchEmailRegisteredException();
        return wrapper.get();
    }

    public String authenticateWithGoogleAndReturnJwt(GoogleAuthenticationCredentials authenticationCredentials)
            throws GeneralSecurityException, IOException {

        try {
            GoogleIdToken googleIdToken = tokenVerifier.verify(authenticationCredentials.getIdToken());
            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String userId = payload.getSubject();
            Optional<Merchant> wrapper = merchantRepository.findByEmail(authenticationCredentials.getEmail());
            if (wrapper.isEmpty())
                throw new NoSuchEmailRegisteredException();
        }catch (Exception e){
            log.trace(e.getMessage());
            throw new MalformedGoogleIdTokenException();
        }
        return generateJwt(authenticationCredentials.getEmail());
    }

    private String generateJwt(String username) {

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("MERCHANT");

        String token = Jwts
                .builder()
                .setId(username)
                .setSubject(username)
                .setIssuer("merchant-service")
                .setAudience("merchant-service")
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 Day
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();

        return token;
    }

}
