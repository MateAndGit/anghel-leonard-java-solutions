# [Java] P46: Objects.equals()와 hashCode()를 이용한 객체 비교

이번 46번 예제는 자바의 모든 객체가 가져야 할 가장 기본적이면서도 중요한 규칙인 **객체의 동등성(Equality)**과 **해시 코드(Hash Code)** 생성을 다룹니다. 특히 `null` 안전성을 보장하는 `Objects.equals()`의 진가가 발휘되는 구간입니다.

---

## 1. Objects.equals(a, b) 상세 분석

### ① 왜 직접 비교(==)나 a.equals(b)를 안 쓰나요?
- **`this.name.equals(other.name)`**: 만약 `this.name`이 `null`이라면 바로 `NullPointerException`이 발생하여 서버가 터집니다.
- **`Objects.equals(a, b)`**: 내부적으로 `null` 체크를 먼저 수행합니다.
    - 둘 다 `null`이면 `true`
    - 하나만 `null`이면 `false`
    - 둘 다 값이 있으면 `a.equals(b)` 호출



### ② 예제 코드 적용 포인트 (Player 클래스)
- `equals` 메서드 하단에서 `name` 필드를 비교할 때 사용되었습니다.
- 이름이 설정되지 않은(null) 선수끼리 비교하거나, 이름이 있는 선수와 없는 선수를 비교할 때 예외 없이 안전하게 `boolean` 값을 반환합니다.

---

## 2. hashCode()와 Objects.hashCode()

### ① hashCode의 역할
- 객체를 식별하는 '정수값'입니다. `HashMap`, `HashSet` 같은 컬렉션에서 객체를 빠르게 찾기 위한 주소록 인덱스 역할을 합니다.

### ② Objects.hashCode(obj)
- 인자가 `null`이면 `0`을 반환하고, 아니면 `obj.hashCode()`를 호출합니다. 이 역시 `NPE` 방어의 핵심입니다.

---

## 3. 실무 프로젝트(User 엔티티) 적용 아이디어

사용자님이 작성하신 `User` 엔티티에서도 특정 유저가 동일한 유저인지 판단할 때 이 방식을 그대로 사용하시면 됩니다.

[지혜 1: 엔티티 동등성 비교]
@Override
public boolean equals(Object o) {
if (this == o) return true;
if (!(o instanceof User user)) return false;

    // ID가 같다면 같은 유저로 인식 (엔티티의 경우)
    // 하지만 필드 전체를 비교해야 할 때는 Objects.equals가 필수!
    return Objects.equals(id, user.id) && 
           Objects.equals(email, user.email);
}

---

## 4. 실무형 요약 보고서

| 항목 | Objects.equals(a, b) | Objects.hashCode(o) |
| :--- | :--- | :--- |
| **주 목적** | 두 객체의 값이 같은지 안전하게 비교 | 객체의 해시값을 안전하게 생성 |
| **Null 방어** | 완벽 지원 (NPE 발생 안 함) | 완벽 지원 (null일 경우 0 반환) |
| **권장 상황** | 모든 클래스의 equals 재정의 시 | 모든 클래스의 hashCode 재정의 시 |
| **자바 버전** | Java 7 이상 공통 | Java 7 이상 공통 |

---

## 5. 현업 개발자의 유지보수 팁 📝

### ⚠️ 중요 규칙: equals를 재정의하면 hashCode도 반드시!
둘은 세트입니다. `equals`가 `true`를 반환하는 두 객체는 반드시 같은 `hashCode`를 가져야 합니다. 그렇지 않으면 `HashSet` 같은 곳에 넣었을 때 중복 제거가 안 되는 대참사가 발생합니다.

### 💡 Java 21+ 레코드(Record)의 비밀
최신 자바의 `record`를 쓰면, 지금 보시는 이 복잡한 `equals`와 `hashCode` 코드를 자바가 자동으로 생성해 줍니다.
하지만 내부적으로는 여전히 `Objects.equals` 방식을 사용하므로, 이 원리를 아는 것이 매우 중요합니다.

---

### 🧱 학습 메모
- **requireNonNull**: 존재를 방어한다.
- **checkIndex**: 범위를 방어한다.
- **Objects.equals**: 비교 시 발생하는 NPE를 방어한다.
- **팁**: 인텔리제이(IntelliJ)의 `Generate` 기능을 쓰면 `Objects.equals`를 사용하는 표준 코드를 자동으로 만들어 줍니다.