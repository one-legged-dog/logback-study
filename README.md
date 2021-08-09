# logback-study

<!—[https://goddaehee.tistory.com/206](https://goddaehee.tistory.com/206) —> 

## 0. Logback

---

1. 자바의 **오픈소스 로깅 프레임워크**
2. SpringBoot 기본 로깅 프레임워크
3. *spring-boot-starter-web* 내부의 *spring-boot-starter-logging* 에 존재

## 1. 작성폴더

---

- 스프링 : ***logback.xml*** 파일을 ***resources*** 폴더에 생성해야 함
- 스프링부트 : ***logback-spring.xml*** 파일을 resources 폴더에 생성해야 함

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/208378c2-3db3-4b70-9b0e-f14d614c5f3a/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/208378c2-3db3-4b70-9b0e-f14d614c5f3a/Untitled.png)

### 1.1 우선순위

1. classpath에 **logback-spring.xml** 파일이 존재
2. logback-spring.xml 파일이 없다면 **.properties 파일** 
3. **logback-spring.xml 과 .properties 파일이 존재**한다면, .properties 파일 적용 후 logback-spring.xml 적용

## 2. 로그레벨

---

1. ERROR = 오류 발생
2. WARN = 처리 가능문제, 향후 시스템에러의 원인이 될 수 있는 요소
3. INFO = 정보성 로그
4. DEBUG = 프로그램 디버그용
5. TRACE = DEBUG 보다 상세정보

TRACE  <  DEBUG  <  INFO  <  WARN  <  ERROR 순으로 설정 레벨 이상의 로그만 출력

INFO 일 경우 WARN, DEBUG 출력 + TRACE, DEBUG 미출력

### 2.1 application.yaml

- ***logging.level : 메인 필드***
    - ***.root*** = 전체로깅 레벨 지정
    - .패키지 = 별도 패키지 레벨 지정

```yaml
# 전체로그레벨
logging.level.root=info 
# 컨트롤러 패키지 로그레벨
logging.level.com.study.logback.controller=debug
# 서비스 패키지 로그레벨
logging.level.com.study.logback.service=info
```

## 3. logger 등록 및 사용법

---

1. ***LoggerFactory*** 에서 로거 객체를 가져와서 로그를 찍는 방식
    1. 가져오기 : **LoggerFactory.getLogger("경로")**

- 서비스

```java
@Service
public class LogService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public void test() {
        LOG.trace("Trace level 테스트");
        LOG.debug("Debug level 테스트");
        LOG.info("Info level 테스트");
        LOG.warn("Warn level 테스트");
        LOG.error("Error level 테스트");
    }
}
```

- 컨트롤러

```java
@Controller
public class LogController {
    // LoggerFactory 클래스에서 .getLogger(경로) 함수로 로거객체를 가져옴
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/test")
    public ModelAndView test() {
        LOG.trace("Trace level 테스트");
        LOG.debug("Debug level 테스트");
        LOG.info("Info level 테스트");
        LOG.warn("Warn level 테스트");
        LOG.error("Error level 테스트");

        ModelAndView mav = new ModelAndView();

        return mav;
    }
}
```

- Controller 클래스의 로거 = debug 레벨이므로 debug 상위 출력
- Service 클래스의 로거 = info 이므로 info 상위 출력

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/24bdeefe-1234-4f82-ba7c-591ab3f7aeaf/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/24bdeefe-1234-4f82-ba7c-591ab3f7aeaf/Untitled.png)

## 4. logback-spring.xml 사용

---

1. 태그의 대소문자는 구별하지 않음
    1. <logger> 태그와 <LOGGER> 태그는 같음
2. ***name attribute*** 지정 필수
3. 크게 **appender** , **logger** 로 나눔
4. 동적 갱신 기능 [ 60초 마다 logback-spring.xml 이 바뀌었는지 검사 후 바뀌었다면 프로그램 갱신 ]

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 60 초 마다 설정 파일의 변경을 확인하여 갱신 -->
<configuration scan="true" scanPeriod="60 seconds">

</configuration>
```

### 4.1 appender

1. log 형태를 설정
2.  로그메시지 출력대상을 결정

💚 종류

- ch.qos.logback.core.**ConsoleAppender** : 로그를 OutputStream 에 작성하여 **콘솔**에 로그를 찍음.
- ch.qos.logback.core.**FileAppender** : 로그를 **파일**에 찍고, 최대 보관 일 수 등을 지정
- ch.qos.logback.core.rolling.**RollingFileAppender** : **여러개의 파일을 순회**하며 로그를 찍음, 용량이 넘어간 Log file을 넘버링하여 저장가능
- ch.qos.logback.classic.net.**SMTPAppender :** 로그를 **메일**에 찍어 전송
- ch.qos.logback.classic.db.**DBAppender** : 로그를 **데이터베이스**에 찍음.

### 4.2 root, logger

- appender 설정을 보고 package 와 로그 level 설정

1. **<root>** : 전역설정, 지역으로 설정된 <logger> 존재시 logger 가 default
2. <logger> : 지역설정, additivity 값으로 root 설정 상속 유무 결정
    1. default 값 = true

### 4.3 property

- 설정 파일에서 사용되는 변수 선언

### 4.4 layout, encoder

- **layout** : 로그 출력 포맷 지정
- **encoder** : Appender 에 포함되어 사용자가 지정한 형식으로 표현될 로그메시지를 변환해주는 요소
    - net.logstash.logback.encoder.LogstashEncoder : Logstash JSON 형식으로 인코딩

FileAppender 하위클래스들은 encoder 를 필요로 하기 때문에, layout은 쓰지 않는다.

### 4.5 pattern

- %Logger{길이} : 로거 이름 축약이 가능하다. {길이} 가 최대 자리 수
- %-5level : 로그 레벨, -5 는 출력의 고정폭 값(5글자)
- %msg : - 로그 메시지
- ${PID:-} : 프로세스 아이디
- %d : 로그 기록 시간
- %p : 로깅 레벨
- %F : 로깅 발생 프로그램 파일명
- %M : 로깅 발생 메소드 명
- %l : 로깅 발생 호출지 정보
- %L : 로깅 발생 호출지 라인 수
- %thread : 현재 Thread 명
- %t : 로깅이 발생한 Thread 명
- %c : 로깅 발생 카테고리
- %C : 로깅 발생 클래스 명
- %m : 로그메시지
- %n : 줄바꿈
- %% : % 출력
- %r : 애플리케이션 시작부터 로깅 발생까지의 시간
- %i : 롤링 순번

### 4.6 etc

- <incloud> : 내부 블록을 나누어서 xml 로 저장한 후 , <incloud resource="참고할xml파일">식으로 설정 추가가능

```xml
<!-- logback-console-appender.xml  -->
<?xml version="1.0" encoding="UTF-8"?>
<included>
<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
<encoder>
<pattern>[%d{HH:mm:ss.SSS}][%-5level][%logger.%method:line%line] - %msg%n</pattern>
<charset>utf8</charset>
</encoder>
</appender>
</included>

####
<!-- incloud 로 나누어 저장한 xml을 incloud 요소의 resource 속성으로 불러오기 가능 -->

<configuration>
<include resource="logback-default.xml"/>
<if condition='"${spring.profiles.active}".equals("local")'>
<then>
<include resource="logback-console-appender.xml" />
```

- <file> : 기록할 파일명과 경로 설정

```xml
<springProfile name="prod">
        <property name="LOG_PATH" value="/app/logs"/>
        <property name="FILE_NAME" value="${application_name}"/>

				<appender name="JSON_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
				            <File>${LOG_PATH}/${FILE_NAME}-json.log</File>
				...
```

- <springProperty> : spring의 environment 정보를 가져와 변수로 삼는다.

    ```xml
    <!--Environment 내의 프로퍼티들을 개별적으로 설정할 수도 있다.--> 
    <springProperty scope="context" name="LOG_LEVEL" source="logging.level.root"/>

    ```

- <property> :
    - resource 속성 : .properties 파일 불러오기
    - name & value 속성 : 값을 가져와서 변수로 삼기 , ${} 로 EL 가능

    ```xml
    <!-- 프로퍼티 파일 참고 -->
    <springProfile name="local"> 
    	<property resource="logback-local.properties"/> 
    </springProfile>

    <!-- 변수화 -->
    <property name="LOG_PATH" value="${log.config.path}"/>
    ```

- <rollingPolicy class="" > : 로그 순회 방식
    - ch.qos.logback.core.rolling.**TimeBasedRollingPolicy : 일자별로 적용**
    - ch.qos.logback.core.rolling.**SizeAndTimeBasedFNATP** : 일자별 + 크기별 적용
    - ch.qos.logback.core.rolling.**SizeAndTimeBasedRollingPolicy** : 일자 + 크기별 적용
- <fileNamePattern> : 파일쓰기가 종료된 log 파일명의 패턴 지정
- <timeBasedFileNamingAndTriggeringPolicy class=""> :  TimeBasedRollingPolicy 선언 후 각 로그파일 크기별 순회를 적용시키고 싶을 때 사용

    ```xml
    <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
        <FileNamePattern>${LOG_PATH}/${FILE_NAME}_%d{yyyy-MM-dd}.%i.json</FileNamePattern>
        <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
            <maxFileSize>5MB</maxFileSize>
        </timeBasedFileNamingAndTriggeringPolicy>
        <maxHistory>3</maxHistory>
    </rollingPolicy>
    ```

- <maxFileSize> : 한 로그 파일 당 최대 용량 지정, IO 성능에 영향을 미치므로 10M 이하 권장 됨. 이름뒤에 .zip, .gz 기입 시 자동 압축
- <maxHistory> : 최대 저장 일 수, 초과 시 오래된 것부터 삭제
- <filer> : 로그 외에 필터링이 필요한 경우 사용
- <springProfile name=""> : 프로필에 따른 설정을 기술하고, 로그를 찍을 append 설정 가능
    - 프로필에 따른 appender 설정

    ```xml
    <springProfile name="prod">
        <property name="LOG_PATH" value="/app/logs"/>
        <property name="FILE_NAME" value="${application_name}"/>

        <!-- Logstash JSON 형식으로 파일 로그 생성 -->
        <!-- 5MB 초과, 날짜 변경 시점마다 생성, 생성된지 3일 이상된 파일은 삭제 -->
        <appender name="JSON_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <File>${LOG_PATH}/${FILE_NAME}-json.log</File>
            <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <FileNamePattern>${LOG_PATH}/${FILE_NAME}_%d{yyyy-MM-dd}.%i.json</FileNamePattern>
                <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                    <maxFileSize>5MB</maxFileSize>
                </timeBasedFileNamingAndTriggeringPolicy>
                <maxHistory>3</maxHistory>
            </rollingPolicy>
        </appender>

    </springProfile>
    ```

    - 프로필에 따른 appender 등록

    ```xml
    <springProfile name="prod">
        <root level="INFO">
            <appender-ref ref="JSON_FILE"/>
        </root>
    </springProfile>
    ```

## 5. 총합본 및 팁

---

- 직접 사용해봄

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- 60 초 마다 설정 파일의 변경을 확인하여 갱신 -->
<configuration scan="true" scanPeriod="60 seconds">
    <!-- 기본 로그 설정 가져오기 -->
    <include resource="org/springframework/boot/logging/logback/base.xml" />

    <springProperty scope="context" name="application_name" source="spring.application.name" />

    <springProfile name="default">
        <!-- 프로필에 대한 appender 설정 -->
        <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
            <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
                <pattern>%d{yyyy-MM-dd HH:mm:ss}  [%-5p] [%F]%M\(%L\) : %m%n</pattern>
            </encoder>
        </appender>

        <property name="LOG_PATH" value="/app/logs/error" />
        <property name="ERROR_FILE_NAME" value="${application_name}" />
        <!-- 프로필에 대한 appender 설정 -->
        <appender name="Error" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <!-- 로깅 레벨에 따른 필터설정 -->
            <filter class="ch.qos.logback.classic.filter.LevelFilter">
                <!-- 레벨이 에러라면 -->
                <level>error</level>\
                <!-- 작동 허용 -->
                <onMatch>ACCEPT</onMatch>
                <!-- 아니라면 appender 작동 안함 -->
                <onMismatch>DENY</onMismatch>
            </filter>
            <!-- 저장할 파일 경로+이름-->
            <file>${LOG_PATH}/${ERROR_FILE_NAME}-json.log</file>
            <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
                <!-- 찍을 로그 패턴 -->
                <pattern>%d{yyyy-MM-dd HH:mm:ss}  [%-5p] [%F]%M\(%L\) : %m%n</pattern>
            </encoder>
            <!-- 순회정책 -->
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>${LOG_PATH}/${ERROR_FILE_NAME}_%d{yyyy-MM-dd}.%i.json</fileNamePattern>
                <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                    <maxFileSize>10MB</maxFileSize>
                </timeBasedFileNamingAndTriggeringPolicy>
                <!-- 일자별 로그파일 최대 보관주기(~일), 해당 설정일 이상된 파일은 자동으로 제거-->
                <maxHistory>60</maxHistory>
            </rollingPolicy>
        </appender>
    </springProfile>

    <springProfile name="prod">
        <!-- 변수 선언 -->
        <property name="LOG_PATH" value="/app/logs"/>
        <property name="FILE_NAME" value="${application_name}" />

        <appender name="JSON_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
            <file>${LOG_PATH}/${FILE_NAME}-json.log</file>
            <!-- JSON 형태로 인코딩하는 인코더 -->
            <encoder class="net.logstash.logback.encoder.LogstashEncoder" />
            <!-- 시간, 사이즈 둘다 설정되는 녀석 -->
            <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
                <fileNamePattern>${LOG_PATH}/${FILE_NAME}_%d{yyyy-MM-dd}.%i.json</fileNamePattern>
                <!-- 한 파일당 최대 용량, 권장사항 10MB 이하 -->
                <maxFileSize>5MB</maxFileSize>
                <!-- 최대 저장 일수, 초과시 오래된 로그부터 삭제 -->
                <maxHistory>3</maxHistory>
                <!--<MinIndex>1</MinIndex>
                <MaxIndex>10</MaxIndex>-->
            </rollingPolicy>
        </appender>
    </springProfile>

    <!-- 특정 패키지 로깅레벨 설정 -->
    <logger name="jdbc.resultset" level="WARN" />
    <logger name="jdbc.resultsettable" level="WARN" />
    <logger name="jdbc.audit" level="WARN" />
    <logger name="jdbc.sqltiming" level="INFO" />
    <logger name="jdbc.connection" level="WARN" />
    <logger name="log4jdbc.debug" level="WARN" />
    <logger name="org.mybatis" level="WARN" />
    <logger name="javax.activation" level="WARN"/>
    <logger name="javax.mail" level="WARN"/>
    <logger name="javax.xml.bind" level="WARN"/>
    <logger name="ch.qos.logback" level="WARN"/>
    <logger name="com.codahale.metrics" level="WARN"/>
    <logger name="com.ryantenney" level="WARN"/>
    <logger name="com.sun" level="WARN"/>
    <logger name="com.zaxxer" level="WARN"/>
    <logger name="org.ehcache" level="WARN"/>
    <logger name="org.apache" level="WARN"/>
    <logger name="org.apache.catalina.startup.DigesterFactory" level="OFF"/>
    <logger name="org.bson" level="WARN"/>
    <logger name="org.hibernate.validator" level="WARN"/>
    <logger name="org.hibernate" level="WARN"/>
    <logger name="org.springframework" level="WARN"/>
    <logger name="org.springframework.web" level="WARN"/>
    <logger name="org.springframework.security" level="WARN"/>
    <logger name="org.springframework.cache" level="WARN"/>
    <logger name="org.xnio" level="WARN"/>
    <logger name="sun.rmi" level="WARN"/>
    <logger name="sun.rmi.transport" level="WARN"/>
    <logger name="com.netflix" level="WARN"/>
    <logger name="javax.management.remote.rmi" level="INFO"/>
    <logger name="org.postgresql" level="INFO"/>

    <!-- 프로필에 따른 appender 추가 -->
    <springProfile name="default">
        <root level="DEBUG">
            <appender-ref ref="STDOUT" />
            <appender-ref ref="Error" />
        </root>
    </springProfile>

    <springProfile name="prod">
        <root level="INFO">
            <appender-ref ref="JSON_FILE"/>
        </root>
    </springProfile>
</configuration>
```

- 에러 발생시 저장 파일

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4f8ce5b8-834a-4b14-b1ec-38945e7deaed/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4f8ce5b8-834a-4b14-b1ec-38945e7deaed/Untitled.png)

- JSON 형식 로그 전송 파일

![https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e099936b-bd60-4e64-9e8e-b144c33c2c8a/Untitled.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e099936b-bd60-4e64-9e8e-b144c33c2c8a/Untitled.png)

### 5.1 로그와 기존 설정이 같이 나오며, 기존 로그가 자세한 편이다.

- 기존 로그를 사용하면서 에러발생이나 로그 수집의 경우에만 appender 를 사용함이 좋아보임

```xml
<springProfile name="default">

    <property name="LOG_PATH" value="/app/logs/error" />
    <property name="ERROR_FILE_NAME" value="${application_name}" />
    <!-- 프로필에 대한 appender 설정 -->
    <appender name="Error" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 로깅 레벨에 따른 필터설정 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <!-- 레벨이 에러라면 -->
            <level>error</level>\
            <!-- 작동 허용 -->
            <onMatch>ACCEPT</onMatch>
            <!-- 아니라면 appender 작동 안함 -->
            <onMismatch>DENY</onMismatch>
        </filter>
        <!-- 저장할 파일 경로+이름-->
        <file>${LOG_PATH}/${ERROR_FILE_NAME}-json.log</file>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!-- 찍을 로그 패턴 -->
            <pattern>%d{yyyy-MM-dd HH:mm:ss}  [%-5p] [%F]%M\(%L\) : %m%n</pattern>
        </encoder>
        <!-- 순회정책 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_PATH}/${ERROR_FILE_NAME}_%d{yyyy-MM-dd}.%i.json</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>10MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <!-- 일자별 로그파일 최대 보관주기(~일), 해당 설정일 이상된 파일은 자동으로 제거-->
            <maxHistory>60</maxHistory>
        </rollingPolicy>
    </appender>
</springProfile>

<!-- appender 참조할 때도 저장용만 적용 -->
<springProfile name="default">
    <root level="DEBUG">
        <appender-ref ref="Error" />
    </root>
</springProfile>
```

### 5.2 로그 수집을 위한 JSON 포멧 appender

- 필요의존이 있음

```groovy
implementation('net.logstash.logback:logstash-logback-encoder:5.0')
```

- appender 설정

```xml
<springProfile name="prod">
    <!-- 변수 선언 -->
    <property name="LOG_PATH" value="/app/logs"/>
    <property name="FILE_NAME" value="${application_name}" />

    <appender name="JSON_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_PATH}/${FILE_NAME}-json.log</file>
        <!-- JSON 형태로 인코딩하는 인코더 -->
        <encoder class="net.logstash.logback.encoder.LogstashEncoder" />
        <!-- 시간, 사이즈 둘다 설정되는 녀석 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${LOG_PATH}/${FILE_NAME}_%d{yyyy-MM-dd}.%i.json</fileNamePattern>
            <!-- 한 파일당 최대 용량, 권장사항 10MB 이하 -->
            <maxFileSize>5MB</maxFileSize>
            <!-- 최대 저장 일수, 초과시 오래된 로그부터 삭제 -->
            <maxHistory>3</maxHistory>
            <!--<MinIndex>1</MinIndex>
            <MaxIndex>10</MaxIndex>-->
        </rollingPolicy>
    </appender>
</springProfile>

<!-- appender 참조할 때도 저장용만 적용 -->
<springProfile name="prod">
    <root level="INFO">
        <appender-ref ref="JSON_FILE"/>
    </root>
</springProfile>
```

### 5.4 자동스캔 기능은 필요할 때만 사용하자

```xml
<!-- scan 이 자동갱신 여부, scanPeriod 가 xml 파일 변경 여부 확인 주기 -->
<configuration scan="true" scanPeriod="60 seconds">
...
</configuration >
```
