package uni.tukl.cs.cps.agilaserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import uni.tukl.cs.cps.agilaserver.filters.CsrfHeaderFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static final String REMEMBER_ME_KEY = "rememberme_key";

	@Autowired
	private RestUnauthorizedEntryPoint restAuthenticationEntryPoint;

	@Bean
	public RestAuthenticationSuccessHandler restAuthenticationSuccessHandler() {
		return new RestAuthenticationSuccessHandler();
	}

	@Bean
	public RestAuthenticationFailureHandler restAuthenticationFailureHandler() {
		return new RestAuthenticationFailureHandler();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/api/login", "/api/sign-up", "/error");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.exceptionHandling().defaultAuthenticationEntryPointFor(getRestAuthenticationEntryPoint(), new AntPathRequestMatcher("/api/**"))
			.and()
			.authorizeRequests()
			.antMatchers("/resources/**", "/css/**", "/js/**", "/plugins/**", "/templates/**", "/images/**","/views/**").permitAll()
			.anyRequest().authenticated().and().addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
			.formLogin()
			.loginProcessingUrl("/authenticate")
			.successHandler(restAuthenticationSuccessHandler())
			.failureHandler(restAuthenticationFailureHandler())
			.usernameParameter("username")
			.passwordParameter("password")
			.loginPage("/login").defaultSuccessUrl("/").permitAll()
			.and()
			.logout()
			.logoutUrl("/logout")
			.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
			.deleteCookies("JSESSIONID")
			.permitAll()
			.and()
			.rememberMe()
			.rememberMeServices(new NullRememberMeServices())
			.key(REMEMBER_ME_KEY)
			.and().csrf().csrfTokenRepository(csrfTokenRepository());
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

	private AuthenticationEntryPoint getRestAuthenticationEntryPoint() {
		return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
}