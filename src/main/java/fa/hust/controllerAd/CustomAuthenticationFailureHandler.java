package fa.hust.controllerAd;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationFailureHandler
        implements AuthenticationFailureHandler {



    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
        if(exception.getMessage().equals("User is disabled")){
            request.setAttribute("message", "Tài khoản chưa được kích hoạt.");
            request.setAttribute("active", "Kích hoạt ngay");
        }
        System.out.println(exception.getMessage());
        if(exception.getMessage().equals("Bad credentials")){
            request.setAttribute("message", "Tài khoản mật khẩu không chính xác.");
        }
        request.getRequestDispatcher("/login").forward(request, response);
    }


}
