package faddy.auth.handler;

import faddy.api.Dto.ResponseDto;
import faddy.global.Utils.FormatConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(FormatConverter.toJson(
                ResponseDto.response("403", "접근 권한이 없습니다.")
        ));
        response.getWriter().flush();
        response.getWriter().close();
    }
}