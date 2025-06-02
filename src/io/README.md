# java.nio / java.io

---


---

### 1. `ssh`를 이용한 원격 파일 전송

`ssh`는 원격 서버에 안전하게 접속하기 위한 프로토콜이며, 파일을 전송할 때는 주로 `scp` (Secure Copy Protocol)나 `sftp` (SSH File Transfer Protocol) 명령어가 활용됩니다.

* **`scp` 명령어 예시 (로컬로 파일 가져오기):**
    ```bash
    scp 사용자명@원격_호스트:/경로/파일이름 로컬_경로/
    ```
* **`sftp` 명령어 예시 (대화형):**
    ```bash
    sftp 사용자명@원격_호스트
    get /경로/파일이름 로컬_경로/
    ```

**자바에서 SSH/SCP/SFTP를 위한 라이브러리:**

자바 표준 라이브러리에는 SSH 클라이언트 기능이 내장되어 있지 않습니다. 따라서 원격 서버와 `ssh` 프로토콜을 이용해 파일을 주고받으려면 **서드파티 라이브러리**를 사용해야 합니다.

* **JSch (Java Secure Channel):**
  `JSch`는 자바에서 SSH, SCP, SFTP 기능을 구현하기 위해 가장 널리 사용되는 라이브러리입니다. 이 라이브러리를 사용하면 자바 애플리케이션 내에서 SSH 세션을 생성하고, 이를 통해 원격 서버의 파일을 안전하게 다운로드하거나 업로드할 수 있습니다. 파일 전송 외에도 포트 포워딩, 원격 명령 실행 등 다양한 SSH 관련 작업을 지원합니다.

  `JSch`를 사용해 파일을 전송하는 개념적인 코드는 다음과 같습니다. (실제 사용 시에는 더 많은 설정과 예외 처리가 필요합니다.)

    ```java
    // import com.jcraft.jsch.*; // JSch 라이브러리 임포트 필요

    // JSch jsch = new JSch();
    // Session session = jsch.getSession("사용자명", "원격_호스트", 22);
    // session.setConfig("StrictHostKeyChecking", "no"); // 실제 서비스에서는 권장되지 않음
    // session.setPassword("비밀번호");
    // session.connect();

    // ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
    // channelSftp.connect();
    // channelSftp.get("/경로/원격_파일", "로컬_저장_경로"); // 파일 다운로드
    // channelSftp.put("로컬_파일_경로", "/경로/원격_대상"); // 파일 업로드

    // channelSftp.exit();
    // session.disconnect();
    ```

---

### 2. `cp`를 이용한 로컬 파일 복사

`cp`는 리눅스/유닉스 운영체제에서 파일을 **로컬 환경 내에서 복사**할 때 사용하는 명령어입니다. 원격 서버와의 통신 없이 현재 시스템 내부에서 동작합니다.

* **`cp` 명령어 예시:**
    ```bash
    cp 원본_파일_경로 대상_경로/
    ```

**자바에서 `cp`와 유사하게 로컬 파일을 복사하기 위한 라이브러리:**

자바에서 로컬 파일을 복사하는 방법은 크게 두 가지로 나눌 수 있습니다.

#### 2.1. 자바 표준 라이브러리 사용 (권장)

`cp` 명령어와 동일한 기능을 자바 코드 내에서 직접 수행하려면 **자바의 표준 I/O 및 NIO.2 라이브러리**를 사용합니다. 이 방법은 운영체제에 독립적이며, 자바 애플리케이션의 이식성과 안정성을 높여줍니다.

* **`java.nio.file.Files` 클래스:**
  자바 7부터 도입된 **NIO.2 (New I/O 2)** 패키지(`java.nio.file`)의 핵심 클래스 중 하나입니다. 파일 및 디렉토리 작업을 위한 강력하고 효율적인 유틸리티 메서드를 제공하며, 파일 복사 기능도 포함되어 있습니다.

    ```java
    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.nio.file.StandardCopyOption;

    public class LocalFileCopyExample {
        public static void main(String[] args) {
            Path source = Paths.get("/path/to/source/file.txt"); // 원본 파일 경로
            Path destination = Paths.get("/path/to/destination/file.txt"); // 대상 파일 경로

            try {
                // 파일을 복사합니다. 만약 대상 파일이 이미 존재하면 덮어씁니다.
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("파일 복사 완료: " + source + " -> " + destination);
            } catch (IOException e) {
                System.err.println("파일 복사 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    ```
  이 방법은 `cp` 명령어처럼 로컬 시스템 내에서 파일을 복사하며, 자바 코드 내에서 안전하고 효율적으로 처리할 수 있어 가장 권장되는 방식입니다.

#### 2.2. 외부 `cp` 명령어 실행

때로는 운영체제의 `cp` 명령어를 자바 코드 내에서 직접 실행해야 하는 특수한 경우가 있습니다. 이 경우 자바 표준 라이브러리의 **프로세스 제어 관련 클래스**를 사용합니다.

* **`java.lang.ProcessBuilder` / `java.lang.Runtime`:**
  이 클래스들은 자바 애플리케이션에서 외부 운영체제 명령어나 프로그램을 실행하는 데 사용됩니다. `ProcessBuilder`는 `Runtime.exec()`보다 더 유연하고 강력한 기능을 제공하여, 실행할 외부 프로세스의 환경 변수나 작업 디렉토리 등을 세밀하게 제어할 수 있습니다.

    ```java
    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;

    public class ExecuteCpCommandExample {
        public static void main(String[] args) {
            String sourcePath = "/path/to/source/file.txt";
            String destinationPath = "/path/to/destination/"; // 대상이 디렉토리인 경우

            try {
                // cp 명령어를 실행하는 예시
                ProcessBuilder pb = new ProcessBuilder("cp", sourcePath, destinationPath);
                // 외부 프로세스의 표준 출력을 읽기 위해 리다이렉트
                pb.redirectErrorStream(true); // 에러 스트림을 표준 출력으로 통합

                Process process = pb.start();

                // 명령어 실행 결과(표준 출력)를 읽음
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                // 프로세스가 종료될 때까지 대기하고 종료 코드 얻기
                int exitCode = process.waitFor();
                System.out.println("cp 명령어 종료 코드: " + exitCode);

                if (exitCode != 0) {
                    System.err.println("cp 명령어가 비정상적으로 종료되었습니다.");
                }

            } catch (IOException | InterruptedException e) {
                System.err.println("cp 명령어 실행 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    ```
  이 방법은 자바 코드가 실행되는 환경에 해당 `cp` 명령어가 존재하고 실행 가능해야 한다는 전제 조건이 있습니다.

---

**요약하자면,**

* **원격 파일 전송 (SSH/SCP/SFTP):** 자바 표준 라이브러리에 내장된 기능은 없으므로, **JSch**와 같은 **서드파티 라이브러리**를 사용해야 합니다.
* **로컬 파일 복사 (`cp`와 유사):**
    * 가장 일반적으로는 자바 표준 라이브러리의 **`java.nio.file.Files.copy()`** 메서드를 사용하는 것이 권장됩니다.
    * 운영체제의 `cp` 명령어를 직접 실행해야 하는 경우, 자바 표준 라이브러리의 **`java.lang.ProcessBuilder`** 또는 `java.lang.Runtime.exec()`를 사용할 수 있습니다.

