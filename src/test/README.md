제공해주신 코드들을 바탕으로 보기의 결합도 유형들을 분석해 드릴게요.

---

## 코드의 결합도 분석

전체적으로 코드에서 나타나는 주요 결합도 유형은 **스탬프 결합도**와 **외부 결합도**이며, 가장 바람직한 형태인 **자료 결합도**도 함께 나타납니다.

---

### ㄱ. 자료 결합도 (Data Coupling)


: 단순 문자열을 메서드에 인자로 전달하는 경우 (단순 데이터)

* **해당 부분**:
    * `AutoFileHandler`의 `extractTxt(File file)` 메서드에서 `file.getAbsolutePath()` (단순 문자열)를 `Hwp2HwpxConverter`의 `convert(String inputFilePath)` 메서드에 인자로 전달하는 경우.
    * `HwpExtractor`의 `extractText` 메서드에서 `savePath` (단순 문자열)를 인자로 전달하는 경우.
* **설명**: 모듈 간에 **단순한 데이터(primitive data)**만 주고받는 가장 약하고 바람직한 형태의 결합도입니다. 코드에서 파일 경로와 같은 단순 문자열을 인자로 주고받는 부분에서 관찰됩니다.

---

### ㄴ. 스탬프 결합도 (Stamp Coupling)


: 객체를 메서드에 인자로 받는 경우, (복합적인 데이터 구조인 객체)

* **해당 부분**:
    * `AutoFileHandler`의 `extractTxt(File file)` 메서드에서 `File` 객체(`file`)를 인자로 받는 경우. (`file.getName()`, `file.getAbsolutePath()` 등 `File` 객체의 일부 속성만 사용)
    * `AutoFileHandler`의 `extractTxt(InputStream inputstream)` 메서드에서 `InputStream`, `Metadata`, `ParseContext` 객체를 인자로 받는 경우.
    * `Hwp2HwpxConverter`의 `convert` 메서드가 `ByteArrayOutputStream` 객체를 반환하는 경우. (`AutoFileHandler`가 이 스트림에서 바이트 배열을 추출하여 사용)
    * `HwpExtractor`의 `extractText(File source, Writer writer, String savePath)` 메서드에서 `File` 객체(`source`)와 `Writer` 객체(`writer`)를 인자로 받는 경우. (`source.exists()`, `writer.append()` 등 객체의 일부 기능만 사용)
* **설명**: 모듈 간에 **복합적인 데이터 구조(객체)**를 주고받지만, 호출받는 모듈이 그 객체의 모든 속성이나 모든 기능을 사용하는 것이 아니라 **일부만 사용**하는 경우입니다. $File$, $InputStream$, $OutputStream$, $Writer$ 와 같은 객체는 복합적인 구조를 가지며, 코드는 이 객체들의 특정 메서드나 속성만을 활용합니다.

---

### ㅂ. 외부 결합도 (External Coupling)


: 자바 기준으로는 외부 라이브러리(jar) 등 서드파티를 기능으로 사용하는 경우 

* **해당 부분**:
    * `AutoFileHandler`가 **Apache Tika** 라이브러리(`org.apache.tika.*`)를 사용하여 파일 파싱 및 텍스트 추출을 수행하는 경우.
    * `Hwp2HwpxConverter`가 **`kr.dogfoot.hwp2hwpx` 및 `kr.dogfoot.hwplib`** 라이브러리를 사용하여 $HWP \to HWPX$ 변환을 수행하는 경우.
    * `HwpExtractor`가 **`kr.dogfoot.hwplib` 및 Apache POI** (`kr.dogfoot.hwplib.org.apache.poi.*`) 라이브러리를 사용하여 $HWP$ 파일의 내부 구조를 직접 파싱하고 이진 데이터를 추출하는 경우.
* **설명**: 모듈이 **외부의 특정 형식이나 통신 프로토콜, 또는 외부 라이브러리와 강하게 연결**되어 있는 경우입니다. 이 코드들은 $HWP$, $HWPX$와 같은 특정 파일 형식과 Apache Tika, Dogfoot 라이브러리 등 외부 서드파티 라이브러리에 직접적으로 의존하며 기능을 수행하므로 외부 결합도가 높다고 볼 수 있습니다.

---

### 기타 결합도 (해당 없음)

* **ㄷ. 제어 결합도 (Control Coupling)**: 한 모듈이 다른 모듈의 내부 논리 흐름을 제어하기 위한 플래그나 명령어를 전달하는 경우는 직접적으로 관찰되지 않습니다. 각 모듈은 자신의 역할을 독립적으로 수행합니다.
```
class ReportGenerator { //=> OCRoption 을 통해 FileScanner Parser를 제어하는 경우
    // isDetailReport라는 제어 플래그를 받아 내부 로직을 변경
    public void generateReport(boolean isDetailReport) {
        if (isDetailReport) {
            System.out.println("## 상세 보고서 생성 중...");
            // 상세 보고서 로직
        } else {
            System.out.println("## 요약 보고서 생성 중...");
            // 요약 보고서 로직
        }
    }
}

class MainApp {
    public static void main(String[] args) {
        ReportGenerator generator = new ReportGenerator();
        generator.generateReport(true); // 호출자가 true/false로 보고서 생성 방식을 제어
    }
}
```
* **ㄹ. 공통 결합도 (Common Coupling)**: 여러 모듈이 전역 변수나 공통 데이터를 직접 공유하여 사용하는 경우는 보이지 않습니다. 대부분의 데이터는 메서드 호출의 인자나 반환 값으로 전달됩니다.
* **ㅁ. 내용 결합도 (Content Coupling)**: 한 모듈이 다른 모듈의 내부 구현(예: private 변수나 코드)을 직접 수정하거나 참조하는 가장 좋지 않은 형태의 결합도는 나타나지 않습니다.

---




# 우선순위 

정리하자면, 귀하가 제공한 코드베이스는 객체를 인자로 주고받으며 특정 부분만 사용하는 **스탬프 결합도**와, 특정한 외부 라이브러리 및 파일 형식에 의존하는 **외부 결합도**를 주로 보입니다. 가장 약한 형태인 **자료 결합도**도 함께 사용되고 있습니다.


소프트웨어 공학에서 모듈 간의 결합도는 낮을수록 좋으며, 피해야 하는 결합도의 우선순위는 다음과 같습니다 (가장 피해야 하는 것부터 시작):

1.  **내용 결합도 (Content Coupling)**
  * **설명**: 한 모듈이 다른 모듈의 **내부 구현(예: private 변수, 코드)에 직접 접근하거나 수정**하는 가장 강하고 나쁜 형태의 결합도입니다. 캡슐화를 완전히 파괴하고, 한 모듈의 작은 변경이 다른 모듈에 치명적인 영향을 미칠 수 있습니다.
  * **우선순위**: **최우선적으로 피해야 합니다.**

2.  **공통 결합도 (Common Coupling)**
  * **설명**: 여러 모듈이 **전역 변수나 공통으로 접근 가능한 데이터 구조**를 공유하고 수정하는 경우입니다. 예측 불가능한 부작용을 초래하기 쉬우며, 데이터 변경 시 모든 관련 모듈을 파악하고 수정해야 할 수 있어 유지보수가 매우 어렵습니다.
  * **우선순위**: **매우 위험하며, 가능하면 피해야 합니다.** (특히 수정 가능한 전역 상태)

3.  **외부 결합도 (External Coupling)**
  * **설명**: 모듈이 **특정 외부 장치, 데이터 형식, 통신 프로토콜, 또는 외부 라이브러리**에 강하게 의존하는 경우입니다. 외부 환경이 변경되면 해당 모듈도 변경되어야 하므로 유연성이 떨어집니다.
  * **우선순위**: **가능하면 피하고, 필요한 경우 최소화하며 인터페이스를 명확히 해야 합니다.**

4.  **제어 결합도 (Control Coupling)**
  * **설명**: 한 모듈이 다른 모듈에게 **어떻게 동작해야 하는지 지시하는 제어 플래그나 인자**를 전달하여, 호출받는 모듈의 내부 로직 흐름을 제어하는 경우입니다. 이는 호출받는 모듈의 독립성을 저해하고 재사용성을 떨어뜨릴 수 있습니다.
  * **우선순위**: **피하는 것이 좋지만, 불가피한 경우라면 신중하게 사용해야 합니다.**

5.  **스탬프 결합도 (Stamp Coupling)**
  * **설명**: 모듈 간에 **복합적인 데이터 구조(객체)**를 주고받지만, 호출받는 모듈이 그 객체의 **일부 속성이나 기능만을 사용**하는 경우입니다. 자료 결합도보다는 못하지만, 데이터의 모든 부분을 사용하지 않으면서 복합 객체를 넘기는 것은 불필요한 의존성을 만들 수 있습니다.
  * **우선순위**: **일반적으로 허용 가능한 수준이지만, 더욱 낮은 결합도를 목표로 한다면 단순 데이터 전달을 고려할 수 있습니다.**

6.  **자료 결합도 (Data Coupling)**
  * **설명**: 모듈 간에 **단순한 데이터(primitive data)**만 주고받는 가장 약하고 **가장 바람직한 형태**의 결합도입니다. 모듈의 독립성이 높고 재사용 및 테스트가 용이합니다.
  * **우선순위**: **가장 권장되는 결합도입니다.**

---

**결론적으로, 피해야 하는 우선순위는 다음과 같습니다:**

**내용 결합도 (최악) > 공통 결합도 > 외부 결합도 > 제어 결합도 > 스탬프 결합도 > 자료 결합도 (최선)**