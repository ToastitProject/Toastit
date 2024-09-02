// 타이머를 반복적으로 실행하는 setInterval 함수의 참조를 저장할 변수
let timerInterval;

// 타이머의 초기 시간을 3분(180초)으로 설정
let remainingTime = 180;

// 페이지 로드 시 세션 스토리지에 저장된 이메일 인증 상태를 확인 및 유지
document.addEventListener("DOMContentLoaded", function () {
    const isEmailVerified = sessionStorage.getItem("emailVerified");
    const verifiedEmail = sessionStorage.getItem("verifiedEmail");

    // 새로고침인 경우 세션 스토리지를 초기화
    if (window.performance.navigation.type === 1) {
        // 두 값이 모두 존재할 때만 초기화 작업을 실행
        if (isEmailVerified && verifiedEmail) {
            sessionStorage.removeItem("emailVerified");
            sessionStorage.removeItem("verifiedEmail");
            // window.location.replace("http://localhost/user/join");
        }
    } else {
        // 유효성 검증 실패로 인한 페이지 로드라면 세션 스토리지 유지
        if (isEmailVerified === "true" && verifiedEmail) {
            document.getElementById("email").value = verifiedEmail;
            document.getElementById("email").readOnly = true;

            document.getElementById("userDetailsSection").classList.remove("hidden");
            document.getElementById("authSection").classList.add("hidden");
            document.getElementById("sendCodeButton").classList.add("hidden");
            document.getElementById("submitButton").classList.remove("hidden");
            document.getElementById("resetAuthButtonContainer").classList.remove("hidden");
        }
    }
});

// 타이머를 시작하고, 남은 시간을 UI에 업데이트하는 함수
function startTimer() {
    timerInterval = setInterval(function () {
        if (remainingTime <= 0) {
            clearInterval(timerInterval);
            document.getElementById("timer").innerText = ""; // 타이머 숫자를 사라지게 만듦
            document.getElementById("endtimer").innerText = "인증 코드가 만료되었습니다.";
            document.getElementById("resendCodeButton").classList.remove("hidden");
        } else {
            let minutes = Math.floor(remainingTime / 60);
            let seconds = remainingTime % 60;
            document.getElementById("timer").innerText = minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
            remainingTime--;
        }
    }, 1000);
}

// 사용자가 입력한 이메일 주소로 인증 코드를 서버에 요청하는 함수
function sendAuthCode() {
    const email = document.getElementById("email").value;

    if (!email) {
        alert("이메일을 입력해주세요.");
        return;
    }

    fetch("/email/send", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({email: email})
    }).then(response => {
        if (response.ok) {
            alert("인증 코드가 발송되었습니다.");

            document.getElementById("authSection").classList.remove("hidden");
            document.getElementById("sendCodeButton").classList.add("hidden");
            startTimer();
        } else {
            return response.text().then(message => {
                alert(message);
            });
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
        body: JSON.stringify({email: email, authCode: authCode})
    }).then(response => {
        if (response.ok) {
            sessionStorage.setItem("emailVerified", "true"); // 세션 스토리지에 이메일 인증 상태 저장
            sessionStorage.setItem("verifiedEmail", email); // 인증된 이메일 저장

            // 비밀번호 입력란을 보이게 함
            document.getElementById("userDetailsSection").classList.remove("hidden");
            document.getElementById("authSection").classList.add("hidden");
            document.getElementById("submitButton").classList.remove("hidden");
            document.getElementById("resetAuthButtonContainer").classList.remove("hidden"); // "다시 인증하기" 버튼 표시

            clearInterval(timerInterval);
        } else {
            alert("인증 코드가 일치하지 않습니다.");
        }
    });
}


// 인증 코드 재발송을 처리하는 함수
function resendAuthCode() {
    document.getElementById("resendCodeButton").classList.add("hidden");
    document.getElementById("endtimer").innerText = "";
    remainingTime = 180; // 타이머를 다시 3분으로 초기화
    startTimer();
    sendAuthCode();
}

// 세션 스토리지 데이터를 삭제하는 함수
function clearSessionStorage() {
    sessionStorage.removeItem("emailVerified");
    sessionStorage.removeItem("verifiedEmail");
    // document.getElementsByClassName("passwordError")[0].remove();
    // document.getElementsByClassName("passwordError")[1].remove();
    alert("인증 과정이 초기화되었습니다.");
    location.reload(); // 페이지를 새로고침하여 인증 상태 초기화
}