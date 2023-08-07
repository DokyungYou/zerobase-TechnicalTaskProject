package com.zerobase.shopreservation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.

                httpBasic().disable() // REST API 는 UI를 사용하지 않으므로 기본설정을 비활성화
                .csrf().disable()  // REST API 는 csrf 보안이 필요 없으므로 비활성화

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT Token 인증방식으로 세션은 필요 없으므로 비활성화
                .and()
                .authorizeRequests().anyRequest().permitAll();
        // 직접 권한에 관련한 설정을 해줬으면 좋았겠지만 아직 제대로 이해한 상태가 아니고 마감시간에 제출을 못할 것 같아서
        // 우선 컨트롤러마다 들어온 토큰에서 꺼낸 값으로 만든 조건문으로 돌아가게끔하였다.
        // 평가 후에 수정해볼 계획

    }

}



