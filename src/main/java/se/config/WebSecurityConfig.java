package se.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.client.RestTemplate;
import se.config.jwt.JwtFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@ComponentScan("se")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {



    private JwtFilter jwtFilter = new JwtFilter();


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers( "/", "/registration").permitAll()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/user/**").hasRole("USER")
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and()
//                .logout()
//                .logoutUrl("/doLogout")
//                .logoutSuccessUrl("/logout_success")
//                .permitAll();
//
//        http    .formLogin()
//                .successForwardUrl("/login_success_handler");
//
//        http    .formLogin()
//                .failureForwardUrl("/login_failure_handler");
//
//        http.logout()
//                .logoutSuccessHandler(new LogoutSuccessHandler() {
//
//                    @Override
//                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
//                                                Authentication authentication)
//                            throws IOException, ServletException {
//
//
//                        System.out.println("This user logged out: " + authentication.getName());
//
//                        response.sendRedirect("/logout_success");
//                    }
//                });
//
//
//        http
//                .csrf()
//                .disable();

        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/user/*").hasRole("USER")
                .antMatchers("/register", "/auth").permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


    }


//    @Configuration
//    protected static class AuthenticationConfiguration extends
//            GlobalAuthenticationConfigurerAdapter {
//
//
//        public AnnotationConfigApplicationContext a = new AnnotationConfigApplicationContext(BDconfig.class);
//        DataSource dataSource = a.getBean("dataSource", DataSource.class);
//
//        @Override
//        public void init(AuthenticationManagerBuilder auth) throws Exception {
//            auth.jdbcAuthentication()
//                    .dataSource(dataSource)
//                    .usersByUsernameQuery(
//                            "select login, password, 'true' from usr " +
//                                    "where login=?")
//                    .authoritiesByUsernameQuery(
//                            "select login, role from usr " +
//                                    "where login=?");
//        }
//    }
//
//
//
//
//    @Bean
//    public RestTemplate restTemplate() {
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setReadTimeout(600000);
//        requestFactory.setConnectTimeout(600000);
//        return new RestTemplate(requestFactory);
//    }
//





}