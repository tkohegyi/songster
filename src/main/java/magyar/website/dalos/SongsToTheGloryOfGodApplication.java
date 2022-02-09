package magyar.website.dalos;

import magyar.website.dalos.web.oauth2.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@SpringBootApplication
public class SongsToTheGloryOfGodApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(SongsToTheGloryOfGodApplication.class, args);
	}

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.authorizeRequests(a -> a.antMatchers("/", "/error", "/webjars/**").permitAll().anyRequest().authenticated())
				.exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
				.logout(l -> l.logoutSuccessUrl("/").permitAll())
				.csrf(c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
				.oauth2Login(o -> o.successHandler(loginSuccessHandler));
		// @formatter:on
	}
}
