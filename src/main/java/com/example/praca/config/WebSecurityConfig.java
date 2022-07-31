package com.example.praca.config;



import com.example.praca.service.JwtAuthorization;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Szymon Królik
 */
@Configuration

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserDetailsService userDetailsService;

    public WebSecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/user/login").permitAll()
//                .antMatchers("/user/kazdy").permitAll()
//                .antMatchers(HttpMethod.POST,"/user").permitAll()
//                .antMatchers(HttpMethod.GET,"/user/current").permitAll()
//                .anyRequest().authenticated()
//                .and().httpBasic();
//    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()
                .ignoringAntMatchers("/**")
                .and()
                .addFilter(new JwtAuthorization(authenticationManager()));

        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user").permitAll()
                .antMatchers("/login").permitAll();


    }


/*
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGllcyI6WyJVU0VSIl0sInN1YiI6Imtyb2xpay5zekB3cC5wbCIsImV4cCI6MTY1OTI3ODM4MX0.j8Y4NBekJ4qCXaBt1ZbBbbEaunbCWufM_9VuXgMw-s_152qSIWOMxJYrd96ygPU6Unkg36pcboF6fGvna3NpKA
 */

    /*
    SzymonInteria - "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGllcyI6WyJVU0VSIl0sInN1YiI6InN6Lmtyb2xpa0BpbnRlcmlhLnBsIiwiZXhwIjoxNjU5Mjc5Nzk0fQ.YSGVnutMKpvkau58w0wMVRB2EyuwhOC-_5BkJqPblu8g5OAz03xk1rg8BcVG_-_54gDEFw-cKxhtCaHVz28f3g
    SzymonWp - eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGllcyI6WyJVU0VSIl0sInN1YiI6Imtyb2xpay5zekB3cC5wbCIsImV4cCI6MTY1OTI3OTgyMX0.pgxxLj46q7c8WGumVgdlAm09HQQzf85kmkVBS2V1njAD2uf9_6zJn2I_fu8DG9kY8eLHLtFOJbibdud7ighp4w
     */

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.httpBasic().and().authorizeRequests()
////                .antMatchers(HttpMethod.POST,"/user/*").permitAll()
////                .antMatchers(HttpMethod.GET,"/user/*").permitAll(
//                .antMatchers(HttpMethod.GET,"/*").permitAll()
//                .antMatchers(HttpMethod.POST,"/*").permitAll()
//                .antMatchers(HttpMethod.DELETE,"/*").permitAll()
//                .antMatchers(HttpMethod.PUT,"/*").permitAll()
//                // budowanie formularza logowania
//                .and()
//                .csrf().disable(); //przypisujemy uprwanienia do /api z metoda get
//
//
//    }



}

