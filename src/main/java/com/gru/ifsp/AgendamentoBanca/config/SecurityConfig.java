package com.gru.ifsp.AgendamentoBanca.config;

import com.gru.ifsp.AgendamentoBanca.entity.springsecurity.filter.CorsFilter;
import com.gru.ifsp.AgendamentoBanca.entity.springsecurity.filter.EmailPasswordAuthenticationFilter;
import com.gru.ifsp.AgendamentoBanca.entity.springsecurity.filter.TokenAuthorizationFilter;
import com.gru.ifsp.AgendamentoBanca.repositories.PermissaoRepository;
import com.gru.ifsp.AgendamentoBanca.repositories.UserRepository;
import com.gru.ifsp.AgendamentoBanca.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.gru.ifsp.AgendamentoBanca.util.Constants.AUTH_ROUTE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final UserServiceImpl userDetailsService;

    private final PermissaoRepository permissionRepository;

    private final UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        EmailPasswordAuthenticationFilter authenticationFilter = new EmailPasswordAuthenticationFilter(this.authenticationManagerBean(), permissionRepository, userRepository);
        authenticationFilter.setFilterProcessesUrl(AUTH_ROUTE+"/login");

        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(
                        AUTH_ROUTE+"/**",
                        "/h2-console/**",
                        "/auth/**",
                        "/public/**"
                ).permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();

        http.addFilter(authenticationFilter);
        http.addFilterBefore(new TokenAuthorizationFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CorsFilter(), TokenAuthorizationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
