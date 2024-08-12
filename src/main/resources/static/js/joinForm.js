let timerInterval; // 타이머를 반복적으로 실행하는 setInterval 함수의 참조를 저장할 변수
let remainingTime = 180; // 타이머의 초기 시간을 3분(180초)으로 설정

// 타이머를 시작하고, 남은 시간을 UI에 업데이트하는 함수
function startTimer() {
    // setInterval을 사용하여 1초마다 실행
    timerInterval = setInterval(function() {
        if (remainingTime <= 0) { // 남은 시간이 0초 이하이면
            clearInterval(timerInterval); // 타이머를 정지
            document.getElementById("timer").innerText = "인증 코드가 만료되었습니다."; // 사용자에게 인증 코드가 만료되었음을 알림
            document.getElementById("resendCodeButton").classList.remove("hidden"); // 재발송 버튼을 화면에 표시
        } else {
            let minutes = Math.floor(remainingTime / 60); // 남은 시간을 분 단위로 계산
            let seconds = remainingTime % 60; // 남은 시간 중 초 단위 계산
            // 분과 초를 MM:SS 형식으로 화면에 표시
            document.getElementById("timer").innerText = minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
            remainingTime--; // 남은 시간을 1초 줄임
        }
    }, 1000); // 이 작업을 1초마다 반복
}

// 사용자가 입력한 이메일 주소로 인증 코드를 서버에 요청하는 함수
function sendAuthCode() {
    const email = document.getElementById("email").value; // 이메일 입력란의 값을 가져옴
    if (!email) { // 이메일이 입력되지 않은 경우
        alert("이메일을 입력해주세요."); // 사용자에게 이메일을 입력하도록 경고창을 띄움
        return; // 함수 종료
    }

    // 이메일을 서버로 POST 요청을 통해 전송
    fetch("/email/send", {
        method: "POST", // POST 메서드 사용
        headers: {
            "Content-Type": "application/json" // 요청 헤더에 JSON 형식으로 데이터를 전송한다고 명시
        },
        body: JSON.stringify({ email: email }) // 이메일을 JSON 형식으로 변환하여 전송
    }).then(response => { // 서버 응답을 처리하는 부분
        if (response.ok) { // 응답이 성공적인 경우
            document.getElementById("authSection").classList.remove("hidden"); // 인증 코드 입력 섹션을 화면에 표시
            document.getElementById("sendCodeButton").classList.add("hidden"); // 인증 코드 보내기 버튼을 숨김
            startTimer(); // 타이머 시작
        } else { // 응답이 실패한 경우
            alert("이메일 형식이 잘못되었습니다."); // 오류 메시지 표시
        }
    });
}

// 사용자가 입력한 인증 코드를 서버에 검증 요청하는 함수
function verifyAuthCode() {
    const email = document.getElementById("email").value;
    const authCode = document.getElementById("authCode").value;

    fetch("/email/verify", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ email: email, authCode: authCode })
    }).then(response => {
        if (response.ok) {
            document.getElementById("userDetailsSection").classList.remove("hidden");
            document.getElementById("authSection").classList.add("hidden");
            document.getElementById("submitButton").classList.remove("hidden");

            // 필드의 값을 콘솔에 출력해 확인
            console.log("Email:", document.getElementById("email").value);
            console.log("AuthCode:", document.getElementById("authCode").value);
            console.log("Password:", document.getElementById("password").value);
            console.log("PasswordCheck:", document.getElementById("passwordCheck").value);

            clearInterval(timerInterval);
        } else {
            alert("인증 코드가 일치하지 않습니다.");
        }
    });
}

// 인증 코드 재발송을 처리하는 함수
function resendAuthCode() {
    document.getElementById("resendCodeButton").classList.add("hidden"); // 재발송 버튼을 숨김
    remainingTime = 180; // 타이머를 다시 3분으로 초기화
    startTimer(); // 타이머 다시 시작
    sendAuthCode(); // 인증 코드 다시 전송
}
