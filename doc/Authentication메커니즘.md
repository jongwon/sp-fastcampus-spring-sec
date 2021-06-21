# Authentication 메커니즘

## 인증 (Authentication)


- Authentication 는 인증된 결과만 저장하는 것이 아니고, 인증을 하기 위한 정보와 인증을 받기 위한 정보가 하나의 객체에 동시에 들어 있습니다. 왜냐하면, 인증을 제공해줄 제공자(AuthenticationProvider)가 어떤 인증에 대해서 허가를 내줄 것인지 판단하기 위해서는 직접 입력된 인증을 보고 허가된 인증을 내주는 방식이기 때문입니다. 그래서 AuthenticationProvider 는 처리 가능한 Authentication에 대해 알려주는 support 메소드를 지원하고, authenticate() 에서 Authentication을 입력값과 동시에 출력값으로도 사용합니다.

    - Credentials : 인증을 받기 위해 필요한 정보, 비번등 (input)
    - Principal : 인증된 결과. 인증 대상 (output)
    - Details : 기타 정보, 인증에 관여된 된 주변 정보들
    - Authorities : 권한 정보들,

- Authentication 을 구현한 객체들은 일반적으로 Token(버스 토큰과 같은 통행권) 이라는 이름의 객체로 구현됩니다. 그래서 Authentication의 구현체를 인증 토큰이라고 불러도 좋습니다.
- Authentication 객체는 SecurityContextHolder 를 통해 세션이 있건 없건 언제든 접근할 수 있도록 필터체인에서 보장해 줍니다.

## 인증 제공자(AuthenticationProvider)


- 인증 제공자(AuthenticationProvider)는 기본적으로 Authentication 을 받아서 인증을 하고 인증된 결과를 다시 Authentication 객체로 전달 합니다.
- 그런데 인증 제공자는 어떤 인증에 대해서 도장을 찍어줄지 AuthenticationManager 에게 알려줘야 하기 때문에 support() 라는 메소드를 제공합니다. 인증 대상과 방식이 다양할 수 있기 때문에 인증 제공자도 여러개 올 수 있습니다.

## 인증 관리자(AuthenticationManager)


- 인증 제공자들을 관리하는 인터페이스가 AuthenticationManager (인증 관리자)이고, 이 인증 관리자를 구현한 객체가 ProviderManager 입니다.
- ProviderManager 도 복수개 존재할 수 있습니다.
- 개발자가 직접 AuthenticationManager를 정의해서 제공하지 않는다면, AuthenticationManager 를 만드는 AuthenticationManagerFactoryBean 에서 DaoAuthenticationProvider 를 기본 인증제공자로 등록한 AuthenticationManage를 만든다.
- DaoAuthenticationProvider 는 반드시 1개의 UserDetailsService 를 발견할 수 있어야 한다. 만약 없으면 InmemoryUserDetailsManager 에 [username=user, password=(서버가 생성한 패스워드)]인 사용자가 등록되어 제공됩니다.

## 참고자료

- JavaBrain 의 설명 : https://www.youtube.com/watch?v=caCJAJC41Rk&t=979s
