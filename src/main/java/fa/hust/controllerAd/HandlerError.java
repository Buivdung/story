package fa.hust.controllerAd;


import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;

@ControllerAdvice
@RequiredArgsConstructor
public class HandlerError {


    @ExceptionHandler(Exception.class)
    public String handlerError(Exception e, Model model){
        model.addAttribute("message",e.getMessage());
        return "error";
    }

}
