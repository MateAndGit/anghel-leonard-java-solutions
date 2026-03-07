# [Java] P42: 특정 예외를 던지는 Null 방어 유틸리티 (Custom Exception)

`Objects.requireNonNull()`의 한계를 넘어, 비즈니스 상황에 맞는 **특정 예외(IAE 등)**를 던지고 **지연 생성(Lazy Evaluation)**을 통해 성능까지 챙기는 고급 패턴입니다.

---

## 1. MyObjects 유틸리티 상세 분석

### ① requireNonNullElseThrowIAE (IllegalArgumentException)
- **용도**: 주로 서비스 계층의 파라미터 검증에 사용합니다.
- **특징**: "값이 잘못 들어왔다"는 의미의 `IAE`를 던져 API 400 에러 처리를 명확하게 합니다.
- **Supplier 활용**: `() -> "Message"` 방식을 쓰면, 에러가 발생하지 않을 때는 문자열 연산을 수행하지 않아 성능에 이득입니다.

### ② requireNonNullElseThrow (커스텀 예외)
- **용도**: 비즈니스 예외(예: `UserNotFoundException`)를 직접 던질 때 사용합니다.
- **특징**: 제네릭 `<X extends Throwable>`을 사용하여 호출자가 원하는 어떤 예외든 수용할 수 있습니다.

---

## 2. 설계의 핵심: 지연 생성 (Lazy Loading)

예외 객체를 생성하는 것은 스택 트레이스(Stack Trace)를 만드는 비용이 듭니다. `Supplier`를 사용하면 이 비용을 절약할 수 있습니다.



- **Eager(즉시)**: `new Exception()` - 무조건 생성 (비효율)
- **Lazy(지연)**: `() -> new Exception()` - **null일 때만** 생성 (효율적)

---

## 3. 실무 계층별 적용 가이드 (Entity & Service)

방금 말씀하신 "DTO -> Service -> Entity" 흐름에 이 유틸리티를 녹여내면 다음과 같습니다.

### [Service 레이어]
입력값이 잘못되었음을 알리는 `IllegalArgumentException` 위주로 배치합니다.
```java
public void assignDriver(String license, Point location) {
    // IAE를 던져서 호출자(Controller 등)에게 잘못된 인자임을 알림
    MyObjects.requireNonNullElseThrowIAE(license, "면허 정보는 필수입니다.");
    MyObjects.requireNonNullElseThrowIAE(location, () -> "출발 위치가 누락되었습니다.");
}

```
### ⚠️ 왜 IAE(IllegalArgumentException)를 더 선호하나요?
- **NPE**: "코드가 버그다"라는 시스템적 오류 인상을 줌.
- **IAE**: "전달된 값이 잘못되었다"는 **비즈니스적 의미**를 전달함.
- **결과**: API 응답 구성(400 Bad Request 등)이나 로그 분석 시 원인 파악이 압도적으로 빠릅니다.

#### 💡 팁: 프로젝트 공통 유틸리티화
`MyObjects` 클래스를 프로젝트의 `common.util` 패키지에 넣고 팀 표준으로 선언하세요. `if (obj == null) throw...` 패턴이 사라지면서 서비스 로직이 놀라울 정도로 깨끗해집니다.

---