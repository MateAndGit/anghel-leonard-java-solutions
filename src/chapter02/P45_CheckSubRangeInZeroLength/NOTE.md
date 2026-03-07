# [Java] P45: 하위 범위 검사 (Sub-Range Validation) 완벽 정리

44번이 단일 인덱스(Point)를 검사했다면, 45번은 "시작(from)부터 끝(to)까지의 구간"이 전체 길이 내에 안전하게 포함되는지 검증합니다. Java 9에서 추가된 `Objects.checkFromToIndex`를 활용해 복잡한 `if` 문을 한 줄로 대체할 수 있습니다.

---

## 1. Objects.checkFromToIndex 상세 분석

### ① 메서드 동작 원리
- **Objects.checkFromToIndex(fromIndex, toIndex, length)**
- **검증 조건**: 0 <= fromIndex <= toIndex <= length 가 모두 참이어야 합니다.
- **결과**: 조건을 만족하면 시작점인 fromIndex를 반환하고, 하나라도 위반하면 IndexOutOfBoundsException을 던집니다.



### ② 예제 코드 적용 포인트 (Function 클래스)
- **생성자**: 필드 n이 0 ~ 100 사이인지 확인합니다 (N_UPPER_BOUND가 101이므로 101 미만까지 허용).
- **yMinusX 메서드**: 인자로 받은 x(시작)와 y(끝)가 전체 길이 n 내에 유효한 '구간'인지 검사합니다.

---

## 2. 왜 이 메서드가 '꿀'인가요?

만약 이 메서드 없이 직접 검증하려면 다음과 같은 실수가 잦은 복잡한 로직이 필요합니다.

// [고전적 방식] 실수하기 매우 쉬운 구간
if (x < 0 || x > y || y > n) {
throw new IndexOutOfBoundsException("잘못된 범위 설정입니다.");
}

// [현대적 방식] P45 적용 - 코드 한 줄로 의도 전달 끝
Objects.checkFromToIndex(x, y, n);

---

## 3. 실무 프로젝트(User 서비스) 적용 시나리오

리스트의 특정 구간을 자르거나(Sub-List), 특정 데이터의 인덱스 범위를 처리할 때 강력한 방어막이 됩니다.

[지혜 1: 사용자 활동 로그 특정 구간 추출]
public List<Log> getLogsByRange(int start, int end, List<Log> allLogs) {
// start부터 end까지가 전체 로그 리스트 범위 안에 있는지 한 줄로 방어!
// end가 리스트 크기(length)와 같아도 되는 점이 특징입니다.
Objects.checkFromToIndex(start, end, allLogs.size());
return allLogs.subList(start, end);
}

---

## 4. 실무형 요약 보고서

| 항목 | Objects.checkFromToIndex | 비고 |
| :--- | :--- | :--- |
| 체크 항목 | 시작점, 끝점, 전체 길이 | 세 변수의 상관관계를 한 번에 검증 |
| 특이 사항 | fromIndex == toIndex 허용 | 길이가 0인 구간도 유효한 것으로 처리 |
| 발생 예외 | IndexOutOfBoundsException | 범위 위반 시 즉시 발생 |
| 연관 메서드 | checkFromIndexSize | 시작점과 '개수'를 기준으로 검사할 때 사용 |

---

## 5. 현업 개발자의 유지보수 팁 📝

### ⚠️ 인자 순서가 핵심!
checkFromToIndex(시작, 끝, 상한선) 순서를 절대 잊지 마세요. 특히 toIndex는 length와 같아도 예외가 발생하지 않습니다(Inclusive). 이는 subList(from, to)와 같은 자바 표준 API의 규칙과 일치합니다.

### 💡 Java 21 프로젝트라면?
최신 자바는 대량의 메모리를 다루는 MemorySegment나 대규모 연산을 처리하는 Vector API 등에서 이 메서드를 내부적으로 적극 활용합니다. 이 표준 도구를 쓰는 습관은 코드를 훨씬 '자바다운(Idiomatic)' 코드로 만들어 줍니다.

---

### 🧱 학습 메모
- checkIndex: 단일 점(Point)이 범위 안에 있는가?
- checkFromToIndex: 특정 구간(Range)이 범위 안에 있는가?
- 설계 팁: 페이징 로직이나 리스트 슬라이싱 로직을 직접 구현해야 할 때, if 문 대신 이 유틸리티를 쓰면 버그를 획기적으로 줄일 수 있습니다.