# sp-fastcampus-spring-sec


## gradle 문제와 관련하여 ...

```
제가 해당 프로젝트를 작성했을때 버전을 확인해보니, spring boot 2.4.x 버전이었습니다.
당시의 gradle 버전과 스프링 버전으로 돌리는데는 무리가 없었지만,
spring boot 가 버전 업을 하면서 이전의 gradle 에서 deprecated 되었던 내용을 정리하게 되었습니다.

그때문에, 현재 버전의 spring boot 에서 이 소스들을 사용하는데 문제가 발생하고 있습니다.

근본적인 이유는 gradle 에서 compile 의존성을 제거한 데 따른 것입니다.

컴파일 의존성은 컴파일시에 해당 의존성을 갖는 프로젝트를 같이 컴파일 해야 하고, 
그 프로젝트의 컴파일 의존성이 있는 프로젝트들 또한 역 추적해서 컴파일을 해야 하기 때문에
intelliJ 나 Eclipse 에서 프로젝트를 무겁게 만드는 근본 원인이었습니다.

그래서, gradle 6.8.x 혹은 7 이상의 버전에서는 compile 의존성이 사라지고 대부분 implementation 의존성으로 대체되었습니다.

api 의존성이 compile 의존성의 일부를 대체한다고 하지만, 사용해보니, 크게 쓸모가 있지는 않은 것 같습니다.

불편하시더라도 implementation 의존성을 사용하고, 필요한 의존성을 충분히 적어주고
모듈을 사용하는 편이 나은 것 같습니다

그렇게 하면 기존에 멀티 프로젝트를 만들면 툴이 무거워진다는 평가를 누그러트릴 수 있을 것입니다.

```
 
-----------------------------------------------------------------------------------------

이 프로젝트가 만들어졌을때의 gradle 버전을 보면 아래와 같습니다. 

```
>>  ./gradlew -v

Welcome to Gradle 6.7.1!

Here are the highlights of this release:
 - File system watching is ready for production use
 - Declare the version of Java your build requires
 - Java 15 support

For more details see https://docs.gradle.org/6.7.1/release-notes.html


------------------------------------------------------------
Gradle 6.7.1
------------------------------------------------------------
```


이를 해결하려면 다음과 같은 과정을 해보시길 바랍니다.

1. 로컬의 gradle 버전을 확인해야 합니다. 

```
> gradle -v

------------------------------------------------------------
Gradle 7.5.1
------------------------------------------------------------

Build time:   2022-08-05 21:17:56 UTC
Revision:     d1daa0cbf1a0103000b71484e1dbfe096e095918

Kotlin:       1.6.21
Groovy:       3.0.10
Ant:          Apache Ant(TM) version 1.10.11 compiled on July 10 2021
JVM:          17.0.5 (Homebrew 17.0.5+0)
OS:           Mac OS X 12.6 x86_64
```

2. gradle 파일에서 아래 내용들을 수정해 주십시오.

```
runtime -> implementation
testRuntime -> testImplementation
compile -> implementation
testCompile -> testImplementation
```

3. gradle 버전을 바꿔주어야 합니다. 제 로컬 버전이 7.5.1 이기 때문에 아래와 같이 버전을 변경했습니다.

```
> gradle wrapper --gradle-version 7.5.1
```

그렇게 한 다음 테스트 케이스를 돌려보시기 바랍니다.


4. 문의해 주신 14-1-part4-jwt branch는 수정해서 14-1-part4-jwt-gradle7 브렌치에 커밋해 놓았습니다.


