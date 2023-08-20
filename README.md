# board-exercise-project

> 소스 코드는 [Github Repository](https://github.com/Jinwon-Dev/board-exercise-project)에서 관리하였으며, 이슈 또한 [Github Issue](https://github.com/Jinwon-Dev/board-exercise-project/issues)로 관리하였습니다.
> 
> Flow Chart, Sequence Diagram, E-R Diagram은 [Draw.io](http://draw.io/)를 이용하여 작성하였습니다.
> 
> 또한, Swagger를 통해 API 명세를 관리합니다.
> 배포는 되어 있지 않아 애플리케이션 실행 후, 다음 URI에서 확인할 수 있습니다.
> - http://localhost:8080/api/swagger-ui/swagger-ui/index.html

</br>
</br>

> ***Project Environment***
- OS
  - macOS Ventura 13.5
- IDE
  - Intellij, DataGrip
- Language
  - Java 17
- FrameWork
  - SpringBoot 3.1.2
  - Junit5
  - Mockito
- Database
  - PostgreSQL

</br>
</br>

> ### ***Introduction***

게시글 등록, 게시글 단건 + 목록 조회, 수정, 삭제 API를 개발하였습니다.

아키텍처는 레이어드 아키텍처를 사용하였고, Junit5와 Mockito를 이용한 도메인별 단위 & 통합 테스트를 총 52개 작성하였습니다.

</br>

또한, 아래와 같은 비즈니스 요구사항들을 충족하게 개발하였습니다.
- 하나의 게시판에는 여러 개의 게시글을 등록할 수 있다.
- 작성자 ID는 HTTP Header의 X-USERID 값을 사용한다.
- 게시글 작성자만 해당 게시글을 수정/삭제할 수 있다.
- 게시글 제목은 100자를 초과할 수 없다.
- 게시글 목록 조회 API는 페이징 처리, 제목 검색 기능이 가능하게 한다.

</br>

추가적으로 아래와 같은 요구사항, 제한사항을 추가하였습니다.
- 게시글 작성, 수정, 삭제는 회원만이 가능하다.
- 게시판은 회원이여도 생성할 수 없고, 사전에 만들어진 게시판에서만 게시글을 작성할 수 있다.
  - ***<u>그에 따라, 사전에 미리 게시판을 생성해두었습니다.</u>***

</br>
</br>

> ### ***Quick Start***

현재 log4j2를 이용하여 비동기로 동작하는 로깅을 하고 있어, 애플리케이션을 실행하는 방법은 다음 두 가지가 있습니다.

***1. IDE를 사용하여 실행***
  - 실행시 VM Options에 다음 옵션을 추가하여 실행합니다.
    - `Dlog4j2.ContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector`
  <img width="886" alt="image" src="https://github.com/Jinwon-Dev/board-exercise-project/assets/106216912/d638c8f4-a16f-4db2-8d9d-94f5da65caa9">
  <img width="841" alt="스크린샷 2023-08-20 오전 12 26 24" src="https://github.com/Jinwon-Dev/board-exercise-project/assets/106216912/ab8299e0-72c9-4d70-a8cb-43711de7fd6e">

***2. Terminal에서 실행***
  - 애플리케이션 빌드 후 `java -jar Dlog4j2.ContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector homework-0.0.1-SNAPSHOT.jar`
    ![스크린샷 2023-08-20 오전 12 33 18](https://github.com/Jinwon-Dev/board-exercise-project/assets/106216912/32162327-ef08-4217-8a6c-d7a27c7eb013)

</br>
</br>

추가적으로 Jacoco를 사용해 테스트 커버리지를 확인할 수 있습니다.

***현재는 92%의 테스트 커버리지를 기록중입니다.***

<img width="1153" alt="스크린샷 2023-08-21 오전 12 48 31" src="https://github.com/Jinwon-Dev/board-exercise-project/assets/106216912/fe2607b3-8640-4a1d-8fe0-bd29237c4b94">

</br>
</br>

> ### ***Flow Chart***

![flow-chart](https://github.com/Jinwon-Dev/board-exercise-project/assets/106216912/294bad44-531d-4698-90ca-b6f8b702a448)

</br>
</br>

> ### ***Sequence Diagram***

![sequence-diagram](https://github.com/Jinwon-Dev/board-exercise-project/assets/106216912/1f2ef7da-d0b3-4978-8181-4600993b82e1)

</br>
</br>

> ### ***E-R Diagram***

![er-diagram](https://github.com/Jinwon-Dev/board-exercise-project/assets/106216912/7e16173d-ea9b-4166-9595-e6fc3b188478)
