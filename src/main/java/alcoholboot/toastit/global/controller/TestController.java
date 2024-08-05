package alcoholboot.toastit.global.controller;

import alcoholboot.toastit.global.exception.ApiResponse;
import alcoholboot.toastit.global.exception.CustomException;
import alcoholboot.toastit.global.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/responseEntity")
    public ResponseEntity<String> responseEntity() {
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/success1")
    public ApiResponse<?> successWithMessage() {
        return ApiResponse.ok("ok");
    }

    @GetMapping("/success2")
    public ApiResponse<?> successWithoutMessage() {
        return ApiResponse.ok(null);
    }

    @GetMapping("/success3")
    public ApiResponse<?> successWithObject() {
        return ApiResponse.created("created");
    }

    @GetMapping("/exception1")
    public ApiResponse<?> testCustomException() { //test error 100코드
        throw new CustomException(ErrorCode.TEST_ERROR);
    }

    @GetMapping("/exception2")
    public ApiResponse<?> testException() {
        throw new RuntimeException();
    }
}
