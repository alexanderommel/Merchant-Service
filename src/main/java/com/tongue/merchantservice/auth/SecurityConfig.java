package com.tongue.merchantservice.auth;

import com.tongue.merchantservice.services.MerchantAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private String secretKey;
    private MerchantAuthenticationService authenticationService;

    public SecurityConfig(@Autowired MerchantAuthenticationService authenticationService,
                          @Value("${security.jwt.secret.key}") String secretKey){
        this.authenticationService=authenticationService;
        this.secretKey=secretKey;
    }


    public JwtAuthorizationFilter filter(){
        return new JwtAuthorizationFilter(authenticationService,secretKey);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(filter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable();
    }
}
