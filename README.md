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

# Lesson 4&5

### 1.

> Spring Security가 실행되는 지점
- indexController.java
```java

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() //인증만 되면 들어갈 수 있는 주소!!
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") //manager도 되고 admin도 된다.
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll() //위의 api아니면 다 접근권한이 있다.
                .and()
                .formLogin()
                .loginPage("/loginForm")

                .loginProcessingUrl("/login") // lesson4: /login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줍니다.
                .defaultSuccessUrl("/");

    }
}

```
- loginProcessingUrl("/login") 으로 설정을 하게 되면 /login 호출시 시큐리티가 낚아채서 대신 로그인을 진행하게 됩니다. 

> Security Session 구조 

  - **Security Session => Authentication => UserDetails**

    - **Security Session** : 로그인이 완료가 되면 시큐리티 Session을 만들어 줍니다. 이때 Security ContextHolder에 session을 저장하게 됩니다. 
    - **Authentication 객체** : session에 들어가야할 객체
    - **UserDetails 객체** : Authentication 안에 있는 유저 정보를 저장하는 객체
    
> Session에 들어가야할 정보 저장하는 코드 
   
 - **PrincipalDetailsService.java**
      ```java
      @Service
      public class PrincipalDetailsService implements UserDetailsService {


      @Autowired
      private UserRepository userRepository;


      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

          User userEntity = userRepository.findByUsername(username);
          if(userEntity != null){
              return new PrincipalDetails(userEntity);
          }
          return null;
         }
       }

      ```
    - /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername함수가 실행
    -


