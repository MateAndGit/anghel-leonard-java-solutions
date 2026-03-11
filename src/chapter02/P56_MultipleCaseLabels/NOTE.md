# [Java] P55: Switch 표현식 (Switch Expressions)

이번 P55 예제는 Java 12부터 도입되어 Java 14에서 표준화된 **Switch 표현식(Switch Expressions)**의 진화를 보여줍니다. 기존의 장황했던 `switch` 문이 얼마나 간결하고 안전하게 변했는지 비교 정리해 드립니다.

---

## 1. Switch 문(Statement) vs Switch 표현식(Expression)

가장 큰 차이는 **"값을 반환할 수 있는가?"**입니다.

* **기존 Switch 문**: 단순히 로직을 수행하는 '문장'입니다. 값을 반환하려면 외부 변수에 대입하거나 `return`을 써야 하며, `break`를 누락하면 다음 `case`로 넘어가는(Fall-through) 위험이 있습니다.
* **새로운 Switch 표현식**: 그 자체로 하나의 '값'이 됩니다. `->` 화살표 연산자를 사용하여 결과를 바로 반환하며, `break`가 필요 없어 코드가 매우 직관적입니다.



---

## 2. 코드 분석: 3단계의 진화

제공해주신 코드는 `switch`가 어떻게 현대화되었는지 단계별로 보여줍니다.

### ① Ugly: 외부 변수 대입 방식
* `Player player = null;` 처럼 임시 변수를 선언해야 합니다.
* 모든 `case`마다 `break`를 명시해야 하며, 실수로 빼먹으면 엉뚱한 객체가 생성될 위험이 큽니다.

### ② Nice: 즉시 반환(Return) 방식
* 임시 변수 없이 각 `case`에서 바로 `return` 합니다.
* `Ugly`보다는 낫지만, 여전히 코드가 중복되고 장황한 '문장'의 형태입니다.

### ③ Switch 표현식 (Java 14+)
[작성 예시]
return switch (playerType) {
case TENNIS -> new TennisPlayer();
case FOOTBALL -> new FootballPlayer();
case SNOOKER -> new SnookerPlayer();
case UNKNOWN -> throw new UnknownPlayerException("...");
default -> throw new IllegalArgumentException("...");
};

* `switch` 결과값을 바로 `return`에 사용합니다.
* `->`를 사용하므로 `break`가 필요 없습니다.
* **Exhaustiveness(망라성)**: 모든 케이스를 다루지 않으면 컴파일 에러가 발생하여 훨씬 안전합니다.

---

## 3. 실무형 요약 보고서

| 항목 | 기존 Switch 문 | 새로운 Switch 표현식 |
| :--- | :--- | :--- |
| **결과 처리** | 문장 (Statement) | 값 (Value / Expression) |
| **연산자** | `:` (콜론) | `->` (화살표) 또는 `yield` |
| **Break** | 필수 (누락 시 버그 위험) | 필요 없음 (자동 차단) |
| **가독성** | 장황함 (Boilerplate) | 매우 간결하고 선언적임 |

---

## 4. 현업 개발자의 유지보수 팁 📝

### 💡 yield 키워드 (전달받은 코드의 break 관련)
사용자님의 코드 중 `createPlayerSwitchExpressionBreak` 메서드에서 `break new TennisPlayer();` 부분이 보입니다.
* **주의**: Java 13 이후 표준 스펙에서는 `break` 대신 **`yield`** 키워드를 사용하여 값을 반환합니다.
* 복잡한 로직(블록 `{ }`)이 들어갈 경우 `yield`를 사용해 값을 밖으로 내보낼 수 있습니다.

### 💡 Enum과 환상의 궁합
`Enum` 타입을 `switch` 표현식으로 다룰 때 `default`를 빼먹으면 컴파일러가 에러를 발생시킵니다. 이는 나중에 새로운 `PlayerTypes`가 추가되었을 때, 이를 처리하지 않은 코드를 즉시 찾아낼 수 있게 해주는 아주 강력한 안전장치입니다.

---

## 5. 학습 메모
* **P54**: 객체의 명함(`toString`)을 만들었습니다.
* **P55**: 로직 분기를 더 현대적이고 안전한 도구(`Switch Expression`)로 교체했습니다.
* **핵심**: "이제 자바에서 값을 결정하는 모든 분기문은 `switch` 표현식이 정석입니다."

---
