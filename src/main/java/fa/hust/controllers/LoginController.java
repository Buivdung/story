package fa.hust.controllers;

import fa.hust.entities.Account;
import fa.hust.entities.Information;
import fa.hust.enums.ERoles;
import fa.hust.req.CodeReq;
import fa.hust.req.RegisterReq;
import fa.hust.service.AccountService;
import fa.hust.utils.EmailUtil;
import fa.hust.utils.StringUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AccountService accountService;
    private final EmailUtil emailUtil;
    private final StringUtil stringUtil;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/")
    public String loginSuccess() {
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("message", null);
        model.addAttribute("active", null);
        return "ui/login";
    }

    @PostMapping("/login")
    public String postLogin() {
        return "ui/login";
    }

    @GetMapping("/register")
    public String getRegister(@ModelAttribute RegisterReq registerReq) {
        return "ui/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterReq registerReq,
                           Model model) {

        if (!Objects.equals(registerReq.getPassword(), registerReq.getPassword2())) {
            model.addAttribute("message", "Tài khoản nhập lại không chính xác");
            return "ui/register";
        }
        Account account = getAccount(registerReq);
        accountService.saveAccount(account);
        return "redirect:/verify/" + account.getId();
    }

    @GetMapping("verify/{id}")
    public String enable(@PathVariable Long id,
                         HttpServletRequest req,
                         Model model) throws MessagingException {
        Account account = accountService.findAccountById(id).orElseThrow();
        String code = stringUtil.randomCode(6);
        HttpSession session = req.getSession();
        session.setAttribute("code", code);
        emailUtil.sendMail(account.getEmail(), "Mã xác nhận của bạn là", code);
        model.addAttribute("acc", account);
        return "ui/verifyCode";
    }

    @GetMapping("send-code")
    public String sendCode( ) {
        return "ui/send-code";
    }

    @PostMapping("send-code")
    public String enable(@RequestParam String email,
                         HttpServletRequest req,
                         Model model) throws MessagingException {
        Account account = accountService.findByEmail(email).orElseThrow();
        String code = stringUtil.randomCode(6);
        HttpSession session = req.getSession();
        session.setAttribute("code", code);
        emailUtil.sendMail(account.getEmail(), "Mã xác nhận của bạn là", code);
        model.addAttribute("acc", account);
        return "redirect:/verify/" + account.getId();
    }

    @PostMapping("verify/{id}")
    public String postEnable(@PathVariable Long id,
                             @ModelAttribute CodeReq codeReq,
                             HttpServletRequest req,
                             Model model) {
        Account account = accountService.findAccountById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy acc"));
        HttpSession session = req.getSession();
        String code = (String) session.getAttribute("code");
        if (Objects.equals(code, getCode(codeReq))) {
            account.setEnable(true);
            accountService.saveAccount(account);
            model.addAttribute("message", "Kích họat tài khoản thành công");
        } else {
            model.addAttribute("error", "Mã xác nhận không đúng.");
        }
        model.addAttribute("acc", account);
        return "ui/verifyCode";
    }

    @GetMapping("forgot-password")
    public String getPassword() {
        return "ui/forgot-password";
    }

    @PostMapping("forgot-password")
    public String postPassword(@RequestParam String email, Model model) throws MessagingException {
        accountService.findByEmail(email).orElseThrow();
        String password = stringUtil.randomPassword(8);
        emailUtil.sendMail(email, "Mật khẩu mới của bạn là:", password);
        model.addAttribute("message", "Chúng tôi đã gửi mật khẩu đến email của bạn");
        return "ui/forgot-password";
    }

    private Account getAccount(RegisterReq registerReq) {
        Account account = modelMapper.map(registerReq, Account.class);
        Information information = modelMapper.map(registerReq, Information.class);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setRole(ERoles.ROLE_USER);
        information.setAvatar("common.jpg");
        information.setAccount(account);
        account.setInformation(information);
        return account;
    }
    private String getCode(CodeReq codeReq) {
        StringBuilder code = new StringBuilder();
        code.append(codeReq.getNumberOne());
        code.append(codeReq.getNumberTwo());
        code.append(codeReq.getNumberThree());
        code.append(codeReq.getNumberFour());
        code.append(codeReq.getNumberFive());
        code.append(codeReq.getNumberSix());
        return code.toString();
    }
}
