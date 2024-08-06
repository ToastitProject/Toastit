package alcoholboot.toastit.exception;

import alcoholboot.toastit.global.response.dto.ApiResponse;
import alcoholboot.toastit.global.response.dto.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testNotFoundEndpoint() throws Exception {
        MvcResult result = mockMvc.perform(get("/non-existent"))
                .andExpect(status().isNotFound())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiResponse<?> response = objectMapper.readValue(content, ApiResponse.class);

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertFalse(response.success());
        assertNull(response.data());
        assertNotNull(response.error());
        assertEquals(ErrorCode.NOT_FOUND_END_POINT.getCode(), response.getErrorCode());
        assertEquals(ErrorCode.NOT_FOUND_END_POINT.getMessage(), response.getErrorMessage());
    }
}

