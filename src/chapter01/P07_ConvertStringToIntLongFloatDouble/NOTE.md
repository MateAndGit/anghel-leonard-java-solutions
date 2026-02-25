# [Java] 문자열 ↔ 숫자 형변환 (Parsing)

### 1. 핵심 메서드 비교
- **`Integer.parseInt(str)`**:
    - 반환 타입: `int` (기본 타입)
    - 특징: 메모리 효율적, 산술 연산에 적합
- **`Integer.valueOf(str)`**:
    - 반환 타입: `Integer` (객체 타입)
    - 특징: `List<Integer>` 처럼 컬렉션에 저장할 때 유용

### 2. 타입별 사용법
| 변환 타입 | 사용 메서드 | 예시 (String -> Number) |
| :--- | :--- | :--- |
| **정수** | `Integer.parseInt()` | "123" -> 123 |
| **큰 정수** | `Long.parseLong()` | "10000000000" -> 10000000000L |
| **실수** | `Double.parseDouble()` | "3.14" -> 3.14 |

### 3. 실무 활용 (Use Cases)
- **사용자 입력값 처리**: 웹 폼에서 넘어온 "가격", "수량" 문자열을 계산을 위해 숫자로 변환
- **환경 설정**: `timeout=3000` 같은 설정 파일 문자열을 시스템 로직에 적용

### 4. 안전한 변환 (Best Practice)
변환할 문자열에 숫자가 아닌 문자(알파벳 등)가 섞여 있으면 `NumberFormatException`이 발생합니다. 반드시 예외 처리가 필요합니다.



```java
// 실무용 안전한 변환 템플릿
public int safeParseInt(String input, int defaultValue) {
    try {
        return Integer.parseInt(input);
    } catch (NumberFormatException e) {
        // 로그를 남기거나 미리 정의된 기본값 반환
        System.err.println("잘못된 숫자 형식: " + input);
        return defaultValue;
    }
}