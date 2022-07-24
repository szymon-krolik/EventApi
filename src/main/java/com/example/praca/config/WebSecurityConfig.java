package com.example.praca.config;



import com.example.praca.model.UserRole;
import com.example.praca.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Szymon Królik
 */
@Configuration
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().authorizeRequests()
//                .antMatchers(HttpMethod.POST,"/user/*").permitAll()
//                .antMatchers(HttpMethod.GET,"/user/*").permitAll(
                .antMatchers(HttpMethod.GET,"/*").permitAll()
                .antMatchers(HttpMethod.POST,"/*").permitAll()
                .antMatchers(HttpMethod.DELETE,"/*").permitAll()
                .antMatchers(HttpMethod.PUT,"/*").permitAll()
                // budowanie formularza logowania
                .and()
                .csrf().disable(); //przypisujemy uprwanienia do /api z metoda get


    }


}

