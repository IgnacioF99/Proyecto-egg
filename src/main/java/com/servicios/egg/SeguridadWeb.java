package com.servicios.egg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb {
   @Bean
   public SecurityFilterChain filterChain( HttpSecurity http ) throws Exception {
      http
            .authorizeHttpRequests(( authorize ) -> authorize
                  .requestMatchers("/admin/").hasRole("ADMIN")
                  .requestMatchers("/user/").hasRole("USER")
                  .requestMatchers("/user/").hasRole("PROV")
                  .requestMatchers("/css/", "/js/", "/img/", "/**").permitAll()
            )
            .formLogin(( form ) -> form
                  .loginPage("/login")
                  .loginProcessingUrl("/logincheck")
                  .usernameParameter("email")
                  .passwordParameter("password")
                  .defaultSuccessUrl("/inicio", true)
                  .permitAll()
            )
            .logout(( logout ) -> logout
                  .logoutUrl("/logout")
                  .logoutSuccessUrl("/login")
                  .permitAll()
            )
            .csrf(csrf -> csrf.disable());
      return http.build();
   }


   @Bean
   public BCryptPasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

}
