package io.github.chait.swaggerdemonew.config;

import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationEntryPoint;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.authentication.KeycloakLogoutHandler;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfigKeycloak extends KeycloakWebSecurityConfigurerAdapter {

    private static final List<String> AUTH_LIST = Arrays.asList(
            "/swagger-resources/**",
            "/swagger-ui.html**",
            "/swagger-ui/",
            "/webjars/**",
            "favicon.ico");

    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;


    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(AdapterDeploymentContext adapterDeploymentContext) throws Exception {
        KeycloakAuthenticationEntryPoint entryPoint = new KeycloakAuthenticationEntryPoint(adapterDeploymentContext);
        entryPoint.setRealm("documentation");
        entryPoint.setLoginUri("/");
        return entryPoint;
    }

    @Override
    protected KeycloakLogoutHandler keycloakLogoutHandler() throws Exception {
        return super.keycloakLogoutHandler();
    }

    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        /*http
                .antMatcher("/swagger-**")
                .csrf().disable()
                .logout().logoutUrl("/logout").addLogoutHandler(keycloakLogoutHandler())
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();*/
        http
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/swagger-resources/**","/swagger-ui.html**","/swagger-ui/").hasRole("admin")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
                .defaultAuthenticationEntryPointFor(authenticationEntryPoint(), new CustomRequestMatcher(AUTH_LIST))
                .and()
                .httpBasic()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .csrf().disable()
                .logout()
                    .logoutUrl("/logout")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .addLogoutHandler(keycloakLogoutHandler());
    }

    @Bean
    public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
            KeycloakAuthenticationProcessingFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
            KeycloakPreAuthActionsFilter filter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = new KeycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        authenticationManagerBuilder.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }


}
