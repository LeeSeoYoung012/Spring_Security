# Spring_Security
스프링 시큐리티를 공부하는 레포지토리 입니다.


# Lesson 1&2

### Spring Security 설정
- SecurityConfig.java

```java
@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//Lesson1&2
  @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll() //위의 api아니면 다 접근권한이 있다.
                .and()
                .formLogin()
                .loginPage("/loginForm"); // 접근 권한이 없는 경우에는 loginForm으로 이동

    }

```

# Lesson 3

### 회원가입시 설정한 비밀번호 인코딩하기 

- SecurityConfig.java 

```java
@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록이 된다
public class SecurityConfig extends WebSecurityConfigurerAdapter {

   //Lesson3 : 해당 메서드의 리턴되는 오브젝트를 IoC로 등록을 해준다.
   @Bean
   public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

@Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll() //위의 api아니면 다 접근권한이 있다.
                .and()
                .formLogin()
                .loginPage("/loginForm");

    }
}

```

- indexController.java

```java
@PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword); //인코딩된 password로 설정
        userRepository.save(user);// 비밀번호 1234 일경우 시큐리티로 로그인할 수 없음 그래서 인코딩을 해주어야함
        return "redirect:/loginForm"; //리다이렉트는 함수 재사용이 가능하다.
    }

```

