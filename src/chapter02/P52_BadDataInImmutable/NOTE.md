# [Java] P52: 불변 클래스 내 잘못된 데이터 방어 (Validation in Immutable)

P52 예제는 P51에서 배운 빌더 패턴에 **데이터 유효성 검증(Validation)**을 더한 완성형 구조를 보여줍니다. 객체가 한 번 생성되면 바꿀 수 없는 '불변'의 특성을 가진 만큼, 생성되는 시점에 데이터가 올바른지 확인하는 것은 시스템의 무결성을 위해 매우 중요합니다.

---

## 1. 불변 객체와 유효성 검증의 결합

불변 객체는 생성 후 수정이 불가능하므로, "잘못된 데이터"를 가진 채로 태어나는 것을 반드시 막아야 합니다. 이번 예제에서는 **Bean Validation(JSR 380)** 어노테이션을 빌더 내부에 배치하여 데이터의 제약 조건을 명시적으로 선언했습니다.

### ① 어노테이션을 통한 제약 조건 명시
- **@NotNull**: 필드가 `null`이 되는 것을 방지합니다.
- **@Size**: 문자열의 길이를 제한합니다 (예: 비밀번호는 6~50자).
- **@Email**: 이메일 형식이 유효한지 검증합니다.

---

## 2. 코드 핵심 분석: 검증의 위치

### ① 빌더(UserBuilder)에 검증 로직 배치
빌더 패턴의 가장 큰 장점 중 하나는 **객체 생성 전 최종 관문** 역할을 수행할 수 있다는 점입니다.
- 필드에 어노테이션을 선언해 둠으로써, 실제 `User` 객체가 생성되기 전(`build()` 호출 시점)에 유효성을 체크할 수 있는 기반을 마련합니다.

### ② 생성자 및 Getter 보호 (복습)
- **private 생성자**: 외부에서 직접 생성을 막아 반드시 빌더를 거치게 강제합니다.
- **방어적 복사**: `getCreated()`에서 `new Date(created.getTime())`를 반환하여 가변 객체인 `Date`로부터 불변성을 지킵니다.



---

## 3. 실무형 요약 보고서

| 항목 | 사용된 기술 | 목적 |
| :--- | :--- | :--- |
| **필수값 체크** | `@NotNull` | 객체 식별에 필수적인 nickname, password 누락 방지 |
| **길이 제한** | `@Size` | 보안(비밀번호) 및 DB 제약 조건에 맞는 데이터 크기 보장 |
| **형식 검증** | `@Email` | 비즈니스 로직에서 이메일 발송 등이 가능하도록 형식 일치 |
| **데이터 무결성** | 불변 객체 (`final`) | 생성 시점에 검증된 데이터가 평생 유지됨을 보장 |

---

## 4. 현업 개발자의 유지보수 팁 📝

### 💡 Validator 연동
단순히 어노테이션만 붙인다고 자동으로 에러가 터지지는 않습니다. 실무에서는 `ValidatorFactory`를 통해 `build()` 메서드 내부에서 아래와 같은 로직을 추가하여 검증을 수행합니다.
```text
// build() 내부 예시
Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
Set<ConstraintViolation<UserBuilder>> violations = validator.validate(this);
if (!violations.isEmpty()) { throw new ConstraintViolationException(violations); }