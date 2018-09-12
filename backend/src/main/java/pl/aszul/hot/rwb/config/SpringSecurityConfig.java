package pl.aszul.hot.rwb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import pl.aszul.hot.rwb.user.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    //private final JwtAuthenticationProvider jwtAuthenticationProvider;

/*    @Autowired
    private UserDetailServiceImpl userDetailsService;*/

/*    @Autowired
    public SpringSecurityConfig(JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }*/

/*    private TokenAuthenticationFilter getAuthenticationFilter() throws Exception {
        return new TokenAuthenticationFilter(new TokenAuthenticationFailureHandler(), authenticationManager(),
                ALLOW_UNAUTHENTICATED_ACCESS);
    }*/

/*    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(jwtAuthenticationProvider);
    }*/

/*    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**");
    }*/

    //TODO sprawdzic czy users/login dziala
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .successForwardUrl("/index")
                .and()
                .logout()
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login");*/


        http.csrf().disable().authorizeRequests().antMatchers("/").permitAll();

/*        http
                .authorizeRequests().antMatchers("/css/**", "/api/**", "/api/whereis/**", "/signup", "/saveuser", "/locations.html", "/users.html", "/editLocation/**", "/locations/maps/**", "/users/avatars/**", "/swagger-ui.html").permitAll() // Enable css when logged out

                .antMatchers("/api/whereis/**", "/v2/api-docs", "/swagger-resources/configuration/ui", "/swagger-resources", "/swagger-resources/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()

                .and()
                .authorizeRequests().anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/students")
                .permitAll()

                .and()
                .logout()
                .permitAll()
                .and();*/

/*        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new UnauthorizedEntryPoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers("/css/**", "/users/login", "/students.html").permitAll()
                .and().authorizeRequests().antMatchers(REST_ENTRY_POINT).authenticated()
                .and().headers().frameOptions().disable()

                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/students")
                .permitAll()

                .and()
                .logout()
                .permitAll()

                .and().addFilterBefore(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);*/
    }

/*    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }*/

/*    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }*/

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .and()
                .withUser("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
