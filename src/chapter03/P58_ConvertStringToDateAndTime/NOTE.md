# [Java] P58: 문자열을 날짜와 시간으로 변환하기 (날짜 API의 진화)

이번 P58 예제는 자바 개발자들이 가장 많이 다루는 주제 중 하나인 **문자열(String)과 날짜/시간(Date/Time) 사이의 변환**을 다룹니다. 특히 Java 8 이전의 유물인 `Date`와 `SimpleDateFormat`, 그리고 현대적인 `java.time` API의 차이점을 한눈에 볼 수 있습니다.

---

## 1. Java 8 이전: 고전적인 방식 (비권장)

Java 8 이전에는 `java.util.Date`와 `java.text.SimpleDateFormat`을 사용했습니다.

* **가변성(Mutable)**: `Date` 객체는 내부 값을 변경할 수 있어 멀티스레드 환경에서 위험합니다.
* **Thread-Safe하지 않음**: `SimpleDateFormat`은 여러 스레드가 동시에 접근하면 데이터가 꼬일 수 있어 `static`으로 공유해서 쓰면 안 됩니다.
* **복잡한 로캘(Locale) 처리**: 로캘마다 날짜 형식이 달라 `DateFormat.getDateTimeInstance` 등을 복잡하게 써야 했습니다.



---

## 2. Java 8 이후: 현대적인 날짜 API (권장)

Java 8부터 도입된 `java.time` 패키지는 이전의 단점들을 완벽히 보완했습니다. 모든 객체는 **불변(Immutable)**이며 **Thread-safe**합니다.

### ① 포매터 없이 변환 (ISO 표준)
ISO 8601 표준 형식(`yyyy-MM-dd`, `HH:mm:ss`)을 따르면 별도의 포매터 없이 `parse()` 메서드만으로 즉시 변환이 가능합니다.
* `LocalDate.parse("2020-06-01")`
* `LocalDateTime.parse("2020-06-01T11:20:15")`

### ② DateTimeFormatter를 이용한 커스텀 변환
원하는 형식이 있을 때는 `DateTimeFormatter.ofPattern()`을 사용합니다.
* `dd.MM.yyyy`: 일.월.년도 형식
* `HH|mm|ss`: 파이프 기호로 구분된 시간 형식
* `XXXXX`: 타임존 오프셋 (`+09:00`)
* `VV`: 타임존 ID (`Asia/Tokyo`)



---

## 3. 핵심 클래스 요약 테이블

| 클래스 | 설명 | 주요 특징 |
| :--- | :--- | :--- |
| **LocalDate** | 날짜 정보만 포함 | 시간, 타임존 없음 (생일, 기념일 등) |
| **LocalTime** | 시간 정보만 포함 | 날짜, 타임존 없음 |
| **LocalDateTime** | 날짜와 시간 포함 | 타임존 정보 없음 |
| **OffsetDateTime** | 날짜, 시간 + 오프셋 | UTC 기준 오프셋 포함 (`+09:00`) |
| **ZonedDateTime** | 날짜, 시간 + 타임존 ID | 서머타임(DST) 등이 고려된 타임존 포함 |

---

## 4. 실무형 유지보수 팁 📝

### 💡 왜 parse() 시 에러가 나나요?
문자열과 포맷이 1:1로 정확히 일치해야 합니다. 공백 하나, 대소문자 하나만 틀려도 `DateTimeParseException`이 발생합니다. 특히 **월(MM)과 분(mm)**, **시간(HH: 24시, hh: 12시)** 구분에 주의하세요.

### 💡 타임존 처리의 정석
단순한 로그 기록은 `LocalDateTime`으로 충분하지만, 글로벌 서비스라면 `ZonedDateTime`이나 `OffsetDateTime`을 사용하여 서버 시간과 사용자 로컬 시간 사이의 간격을 명확히 관리해야 합니다.

---

## 5. 학습 메모
* **P55**: `switch` 표현식으로 로직 분기를 최적화했습니다.
* **P58**: 이제 문자열 데이터를 날짜 객체로 자유자재로 다룰 수 있습니다.
* **핵심**: "신규 프로젝트라면 무조건 `java.time` API와 `DateTimeFormatter`를 사용하세요."

---

**날짜와 시간 변환은 실무 데이터 처리의 80%를 차지한다고 해도 과언이 아닙니다. 이 포맷들을 잘 익혀두시면 데이터 파싱에서 겪는 삽질을 크게 줄이실 수 있어요! 다음 P59로 넘어가 볼까요?** 😊