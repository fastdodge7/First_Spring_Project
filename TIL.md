p115의 Posts_수정된다() 테스트에서 발생한 에러

핵심 에러 메시지는 다음과 같다.

- 2022-12-22T03:02:49.385+09:00 ERROR 23132 --- [o-auto-1-exec-1] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: org.springframework.dao.DataIntegrityViolationException: not-null property references a null or transient value : com.fastdodgespring.domain.posts.Posts.content] with root cause

- org.springframework.web.client.RestClientException: Error while extracting response for type [class java.lang.Long] and content type [application/json]

- Caused by: org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot deserialize value of type `java.lang.Long` from Object value (token `JsonToken.START_OBJECT`)

대략 다음과 같은 내용이다
- null이면 안되는 정보가 null이다.
- Long 타입, 컨텐츠 타입은 Json인 응답을 추출하다가 문제가 발생
- JsonToken.START_OBJECT를 Long으로 deserialize 할 수 없다.

문제가 됐던 부분
- PostsApiController 클래스의 update 메소드에서, PostsUpdateRequestDto 파라미터에 @RequestBody 어노테이션을 빼먹었다.

원인 분석
- @RequestBody 어노테이션이 빠지면서, HTTP Request로 들어온 Title, Content 정보가 파라미터에 들어가지 않아서 null이 되었고, 
null이면 안되는 필드에 null 값을 집어넣는 수정을 가하자, H2에서 안된다고 에러를 뱉은 것.


아래는 에러 메시지 전문.
Hibernate: insert into posts (author, content, title) values (?, ?, ?)
2022-12-22T03:02:49.277+09:00  INFO 23132 --- [o-auto-1-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2022-12-22T03:02:49.277+09:00  INFO 23132 --- [o-auto-1-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2022-12-22T03:02:49.285+09:00  INFO 23132 --- [o-auto-1-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 8 ms
Hibernate: select p1_0.id,p1_0.author,p1_0.content,p1_0.title from posts p1_0 where p1_0.id=?
2022-12-22T03:02:49.385+09:00 ERROR 23132 --- [o-auto-1-exec-1] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: org.springframework.dao.DataIntegrityViolationException: not-null property references a null or transient value : com.fastdodgespring.domain.posts.Posts.content] with root cause

org.hibernate.PropertyValueException: not-null property references a null or transient value : com.fastdodgespring.domain.posts.Posts.content
at org.hibernate.engine.internal.Nullability.checkNullability(Nullability.java:111) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.engine.internal.Nullability.checkNullability(Nullability.java:55) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.event.internal.DefaultFlushEntityEventListener.scheduleUpdate(DefaultFlushEntityEventListener.java:257) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.event.internal.DefaultFlushEntityEventListener.onFlushEntity(DefaultFlushEntityEventListener.java:136) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.event.service.internal.EventListenerGroupImpl.fireEventOnEachListener(EventListenerGroupImpl.java:107) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.event.internal.AbstractFlushingEventListener.flushEntities(AbstractFlushingEventListener.java:214) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.event.internal.AbstractFlushingEventListener.flushEverythingToExecutions(AbstractFlushingEventListener.java:90) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.event.internal.DefaultFlushEventListener.onFlush(DefaultFlushEventListener.java:38) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.event.service.internal.EventListenerGroupImpl.fireEventOnEachListener(EventListenerGroupImpl.java:107) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.internal.SessionImpl.doFlush(SessionImpl.java:1422) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.internal.SessionImpl.managedFlush(SessionImpl.java:476) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.internal.SessionImpl.flushBeforeTransactionCompletion(SessionImpl.java:2233) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.internal.SessionImpl.beforeTransactionCompletion(SessionImpl.java:1929) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.engine.jdbc.internal.JdbcCoordinatorImpl.beforeTransactionCompletion(JdbcCoordinatorImpl.java:439) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl.beforeCompletionCallback(JdbcResourceLocalTransactionCoordinatorImpl.java:183) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl$TransactionDriverControlImpl.commit(JdbcResourceLocalTransactionCoordinatorImpl.java:281) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.hibernate.engine.transaction.internal.TransactionImpl.commit(TransactionImpl.java:101) ~[hibernate-core-6.1.5.Final.jar:6.1.5.Final]
at org.springframework.orm.jpa.JpaTransactionManager.doCommit(JpaTransactionManager.java:562) ~[spring-orm-6.0.2.jar:6.0.2]
at org.springframework.transaction.support.AbstractPlatformTransactionManager.processCommit(AbstractPlatformTransactionManager.java:743) ~[spring-tx-6.0.2.jar:6.0.2]
at org.springframework.transaction.support.AbstractPlatformTransactionManager.commit(AbstractPlatformTransactionManager.java:711) ~[spring-tx-6.0.2.jar:6.0.2]
at org.springframework.transaction.interceptor.TransactionAspectSupport.commitTransactionAfterReturning(TransactionAspectSupport.java:654) ~[spring-tx-6.0.2.jar:6.0.2]
at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:407) ~[spring-tx-6.0.2.jar:6.0.2]
at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:119) ~[spring-tx-6.0.2.jar:6.0.2]
at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184) ~[spring-aop-6.0.2.jar:6.0.2]
at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:752) ~[spring-aop-6.0.2.jar:6.0.2]
at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:703) ~[spring-aop-6.0.2.jar:6.0.2]
at com.fastdodgespring.service.posts.PostsService$$SpringCGLIB$$0.update(<generated>) ~[main/:na]
at com.fastdodgespring.web.PostsApiController.update(PostsApiController.java:36) ~[main/:na]
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77) ~[na:na]
at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
at java.base/java.lang.reflect.Method.invoke(Method.java:568) ~[na:na]
at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:207) ~[spring-web-6.0.2.jar:6.0.2]
at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:152) ~[spring-web-6.0.2.jar:6.0.2]
at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:117) ~[spring-webmvc-6.0.2.jar:6.0.2]
at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:884) ~[spring-webmvc-6.0.2.jar:6.0.2]
at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:797) ~[spring-webmvc-6.0.2.jar:6.0.2]
at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-6.0.2.jar:6.0.2]
at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1080) ~[spring-webmvc-6.0.2.jar:6.0.2]
at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:973) ~[spring-webmvc-6.0.2.jar:6.0.2]
at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1003) ~[spring-webmvc-6.0.2.jar:6.0.2]
at org.springframework.web.servlet.FrameworkServlet.doPut(FrameworkServlet.java:917) ~[spring-webmvc-6.0.2.jar:6.0.2]
at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:734) ~[tomcat-embed-core-10.1.1.jar:6.0]
at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:880) ~[spring-webmvc-6.0.2.jar:6.0.2]
at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:814) ~[tomcat-embed-core-10.1.1.jar:6.0]
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:223) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:158) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) ~[tomcat-embed-websocket-10.1.1.jar:10.1.1]
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:185) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:158) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-6.0.2.jar:6.0.2]
at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.0.2.jar:6.0.2]
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:185) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:158) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-6.0.2.jar:6.0.2]
at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.0.2.jar:6.0.2]
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:185) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:158) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-6.0.2.jar:6.0.2]
at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.0.2.jar:6.0.2]
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:185) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:158) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:197) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:119) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:400) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:861) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1739) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[tomcat-embed-core-10.1.1.jar:10.1.1]
at java.base/java.lang.Thread.run(Thread.java:833) ~[na:na]

Hibernate: select p1_0.id,p1_0.author,p1_0.content,p1_0.title from posts p1_0
Hibernate: delete from posts where id=?

org.springframework.web.client.RestClientException: Error while extracting response for type [class java.lang.Long] and content type [application/json]

	at org.springframework.web.client.HttpMessageConverterExtractor.extractData(HttpMessageConverterExtractor.java:118)
	at org.springframework.web.client.RestTemplate$ResponseEntityResponseExtractor.extractData(RestTemplate.java:1132)
	at org.springframework.web.client.RestTemplate$ResponseEntityResponseExtractor.extractData(RestTemplate.java:1115)
	at org.springframework.web.client.RestTemplate.doExecute(RestTemplate.java:865)
	at org.springframework.web.client.RestTemplate.execute(RestTemplate.java:764)
	at org.springframework.web.client.RestTemplate.exchange(RestTemplate.java:646)
	at org.springframework.boot.test.web.client.TestRestTemplate.exchange(TestRestTemplate.java:710)
	at com.fastdodgespring.web.PostsApiControllerTest.Posts_수정된다(PostsApiControllerTest.java:100)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:727)
	at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:131)
	at org.junit.jupiter.engine.extension.TimeoutExtension.intercept(TimeoutExtension.java:149)
	at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestableMethod(TimeoutExtension.java:140)
	at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestMethod(TimeoutExtension.java:84)
	at org.junit.jupiter.engine.execution.ExecutableInvoker$ReflectiveInterceptorCall.lambda$ofVoidMethod$0(ExecutableInvoker.java:115)
	at org.junit.jupiter.engine.execution.ExecutableInvoker.lambda$invoke$0(ExecutableInvoker.java:105)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain$InterceptedInvocation.proceed(InvocationInterceptorChain.java:106)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.proceed(InvocationInterceptorChain.java:64)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.chainAndInvoke(InvocationInterceptorChain.java:45)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.invoke(InvocationInterceptorChain.java:37)
	at org.junit.jupiter.engine.execution.ExecutableInvoker.invoke(ExecutableInvoker.java:104)
	at org.junit.jupiter.engine.execution.ExecutableInvoker.invoke(ExecutableInvoker.java:98)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.lambda$invokeTestMethod$6(TestMethodTestDescriptor.java:210)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.invokeTestMethod(TestMethodTestDescriptor.java:206)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:131)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:65)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:151)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:141)
	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:139)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:138)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:95)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:41)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:155)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:141)
	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:139)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:138)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:95)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:41)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:155)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:141)
	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:139)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:138)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:95)
	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.submit(SameThreadHierarchicalTestExecutorService.java:35)
	at org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutor.execute(HierarchicalTestExecutor.java:57)
	at org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine.execute(HierarchicalTestEngine.java:54)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:147)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:127)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:90)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.lambda$execute$0(EngineExecutionOrchestrator.java:55)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.withInterceptedStreams(EngineExecutionOrchestrator.java:102)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:54)
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:114)
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:86)
	at org.junit.platform.launcher.core.DefaultLauncherSession$DelegatingLauncher.execute(DefaultLauncherSession.java:86)
	at org.junit.platform.launcher.core.SessionPerRequestLauncher.execute(SessionPerRequestLauncher.java:53)
	at com.intellij.junit5.JUnit5IdeaTestRunner.startRunnerWithArgs(JUnit5IdeaTestRunner.java:71)
	at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:33)
	at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:221)
	at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:54)
Caused by: org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot deserialize value of type `java.lang.Long` from Object value (token `JsonToken.START_OBJECT`)
at org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.readJavaType(AbstractJackson2HttpMessageConverter.java:406)
at org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.read(AbstractJackson2HttpMessageConverter.java:354)
at org.springframework.web.client.HttpMessageConverterExtractor.extractData(HttpMessageConverterExtractor.java:103)
... 75 more
Caused by: com.fasterxml.jackson.databind.exc.MismatchedInputException: Cannot deserialize value of type `java.lang.Long` from Object value (token `JsonToken.START_OBJECT`)
at [Source: (org.springframework.util.StreamUtils$NonClosingInputStream); line: 1, column: 1]
at com.fasterxml.jackson.databind.exc.MismatchedInputException.from(MismatchedInputException.java:59)
at com.fasterxml.jackson.databind.DeserializationContext.reportInputMismatch(DeserializationContext.java:1746)
at com.fasterxml.jackson.databind.DeserializationContext.handleUnexpectedToken(DeserializationContext.java:1520)
at com.fasterxml.jackson.databind.DeserializationContext.handleUnexpectedToken(DeserializationContext.java:1425)
at com.fasterxml.jackson.databind.DeserializationContext.extractScalarFromObject(DeserializationContext.java:937)
at com.fasterxml.jackson.databind.deser.std.StdDeserializer._parseLong(StdDeserializer.java:949)
at com.fasterxml.jackson.databind.deser.std.NumberDeserializers$LongDeserializer.deserialize(NumberDeserializers.java:575)
at com.fasterxml.jackson.databind.deser.std.NumberDeserializers$LongDeserializer.deserialize(NumberDeserializers.java:550)
at com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.readRootValue(DefaultDeserializationContext.java:323)
at com.fasterxml.jackson.databind.ObjectReader._bindAndClose(ObjectReader.java:2105)
at com.fasterxml.jackson.databind.ObjectReader.readValue(ObjectReader.java:1481)
at org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.readJavaType(AbstractJackson2HttpMessageConverter.java:395)
... 77 more

