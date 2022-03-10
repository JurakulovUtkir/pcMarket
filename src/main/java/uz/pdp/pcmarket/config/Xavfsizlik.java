package uz.pdp.pcmarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Xavfsizlik extends WebSecurityConfigurerAdapter {

    // avvaldan userlarni qo'shib olish


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("super_admin").password(passwordEncoder().encode("123")).roles("SUPER_ADMIN").authorities("ALL")
                .and()
                .withUser("moderator").password(passwordEncoder().encode("123")).roles("MODERATOR").authorities("ADD","EDIT")
                .and()
                .withUser("operator").password(passwordEncoder().encode("123")).roles("OPERATOR").authorities("GET");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(HttpMethod.DELETE ,"/api/**").hasAnyAuthority("DELETE","ALL")
                .antMatchers(HttpMethod.POST,"/api/**").hasAnyAuthority("ADD","ALL")
                .antMatchers(HttpMethod.PUT,"/api/**").hasAnyAuthority("EDIT","ALL")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
