package com.lydbook.audiobook.config;

import com.lydbook.audiobook.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends GlobalMethodSecurityConfiguration {


    private final DataSource dataSource;

    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, true FROM users WHERE username=?")
                .authoritiesByUsernameQuery("SELECT username, concat('ROLE_', role) FROM users WHERE username=?")
                .passwordEncoder(passwordEncoder());
    }


    @Configuration
    public static class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity.sessionManagement().maximumSessions(10);
            httpSecurity
                    .authorizeRequests()
                    .antMatchers("/css/**","/js/**").permitAll()
                    .antMatchers("/favicon.ico").permitAll()
                    .antMatchers("/admin/**").hasRole(UserRole.ADMIN.name())
                    .antMatchers("/api/v1/progress/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.USER.name())
                    .antMatchers(HttpMethod.POST,"/api/v1/audiobooks/**").hasRole(UserRole.ADMIN.name())
                    .antMatchers(HttpMethod.PUT,"/api/v1/audiobooks/**").hasRole(UserRole.ADMIN.name())
                    .antMatchers(HttpMethod.PUT,"/api/v1/user/password").hasAnyRole(UserRole.ADMIN.name(), UserRole.USER.name())
                    .anyRequest().authenticated().and()
                    .csrf().ignoringAntMatchers("/api/**")
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                    .logout()
                    .permitAll();
        }

    }
}
