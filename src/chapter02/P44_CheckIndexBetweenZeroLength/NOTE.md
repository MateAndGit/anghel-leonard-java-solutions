# [Java] P44: 경계 조건 검사 (Index Range Validation) 정리

데이터가 null이 아니더라도, 그 값이 "0 이상이고 특정 상한선(Bound) 미만"인지 확인해야 할 때가 있습니다. 자바의 Objects.checkIndex는 이 과정을 단 한 줄로 해결해 줍니다.

---

## 1. Objects.checkIndex 상세 분석

### ① 메서드 동작 원리
- Objects.checkIndex(index, length)
- 조건: 0 <= index < length 를 만족해야 합니다.
- 결과: 조건을 만족하면 index를 그대로 반환하고, 위반하면 IndexOutOfBoundsException을 던집니다.



### ② 예제 코드 적용 포인트 (Function 클래스)
- 생성자: x가 0 ~ 10 사이인지 확인 (X_UPPER_BOUND가 11이므로).
- xMinusY 메서드: y가 0 이상이고 x 미만인지 확인.
- oneMinusY 메서드: y가 0 이상이고 16 미만인지 확인 (Y_UPPER_BOUND가 16이므로).

---

## 2. 왜 if 문 대신 이걸 쓰나요?

전통적인 방식과 비교하면 가독성 차이가 명확하며 실수를 줄여줍니다.

// 고전적 방식 (실수 유발 가능성 높음)
if (y < 0 || y >= x) {
throw new IndexOutOfBoundsException("범위를 벗어났습니다.");
}

// 현대적 방식 (P44 적용)
Objects.checkIndex(y, x);

---

## 3. 실무 프로젝트(User 엔티티 등) 적용 아이디어

사용자님이 진행 중인 프로젝트의 서비스 로직이나 엔티티 수치 검증에 활용해 보세요.

[지혜 1: 페이지 번호 검증]
public List<User> getUsersByPage(int pageNumber, int totalPages) {
// 페이지 번호는 0부터 시작하며 전체 페이지 수를 넘을 수 없음
Objects.checkIndex(pageNumber, totalPages);
return userRepository.findAll(PageRequest.of(pageNumber, 10));
}

[지혜 2: 등급/권한 레벨]
public void updateRole(int roleLevel) {
// 시스템 최대 권한 레벨이 5라면 (상한선 6 미만)
Objects.checkIndex(roleLevel, 6);
this.roleLevel = roleLevel;
}

---

## 4. 실무형 요약 보고서

| 항목 | Objects.checkIndex | 비고 |
| :--- | :--- | :--- |
| 최저값 | 0 (고정) | 0 미만은 무조건 예외 |
| 최고값 | length - 1 | 인자로 넣은 length 자체는 포함 안 됨 |
| 발생 예외 | IndexOutOfBoundsException | 배열/리스트 인덱스 에러와 동일 |
| 권장 버전 | Java 9 이상 | 자바 21 프로젝트라면 적극 권장 |

---

## 5. 현업 개발자의 유지보수 팁 📝

### ⚠️ 주의사항: 0을 포함한 상한선
checkIndex는 "미만(<)" 조건입니다. 만약 "이하(<=)" 조건을 검증하고 싶다면 length + 1을 인자로 넘겨야 합니다.

### 💡 Java 16+ 추가 기능: checkFromToIndex
자바 16부터는 범위 자체(Range)를 검사하는 checkFromToIndex(from, to, length) 등이 추가되어 리스트 슬라이싱 등에 유용합니다.

---

### 🧱 학습 메모
- null 체크가 '존재'에 대한 방어라면, checkIndex는 '경계'에 대한 방어입니다.
- 숫자를 다루는 로직에서 범위를 벗어날 위험이 있을 때 이 유틸리티를 떠올리세요.