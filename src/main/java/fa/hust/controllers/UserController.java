package fa.hust.controllers;

import fa.hust.entities.Account;
import fa.hust.entities.Paid;
import fa.hust.entities.Payment;
import fa.hust.enums.ETypeFile;
import fa.hust.req.FileUpload;
import fa.hust.req.PasswordReq;
import fa.hust.service.AccountService;
import fa.hust.service.PaidService;
import fa.hust.service.PaymentService;
import fa.hust.utils.FileUtil;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


@Controller
@RequestMapping("user")
@RequiredArgsConstructor
@MultipartConfig
public class UserController {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final PaymentService paymentService;
    private final PaidService paidService;
    private final FileUtil fileUtil;

    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable Long id, Model model) {
        Account account = accountService.findAccountById(id).orElseThrow();
        model.addAttribute("account", account);
        return "user/information";
    }

    @PostMapping("/profile/{id}")
    public String postProfile(@PathVariable Long id,
                              @RequestParam MultipartFile avatar,
                              Model model) throws IOException {
        Account account = accountService.findAccountById(id).orElseThrow();
        FileUpload fileUpload = fileUtil.checkFile(avatar, ETypeFile.AVATAR);
        String suffix = fileUpload.getFileName().split("\\.", 2)[1];
        String avatarName = account.getInformation().getId() + "." + suffix;
        fileUpload.setFileName(avatarName);
        fileUtil.saveFile(fileUpload, null);
        account.getInformation().setAvatar(avatarName);
        account = accountService.saveAccount(account);
        model.addAttribute("account", account);
        return "user/information";
    }

    @GetMapping("/story/{id}")
    public String getStory(@PathVariable Long id,
                           @RequestParam(required = false) Optional<Integer> number,
                           Model model) {
        Integer pageNumber = number.orElse(1);
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.by("id").descending());
        Page<Paid> paids = paidService.findAll(id, pageable);
        model.addAttribute("paids", paids);
        model.addAttribute("number", pageNumber);
        model.addAttribute("total", paids.getTotalPages());
        return "user/paidStory";
    }

    @GetMapping("/password/{id}")
    public String getPassword(@PathVariable Long id) {
        return "user/password";
    }

    @GetMapping("/payment/{id}")
    public String getPayment(@PathVariable Long id,
                             @RequestParam(required = false) Optional<Integer> number,
                             Model model) {
        Integer pageNumber = number.orElse(1);
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.by("id").descending());
        Page<Payment> payments = paymentService.findAll(id, pageable);
        model.addAttribute("payments", payments);
        model.addAttribute("number", pageNumber);
        model.addAttribute("total", payments.getTotalPages());
        return "user/payment";
    }

    @PostMapping("/password/{id}")
    public String getPassword(@PathVariable Long id,
                              @ModelAttribute PasswordReq passwordReq,
                              Model model) {
        String message = "Thay đổi mật khẩu thành công";
        if (!Objects.equals(passwordReq.getPasswordNew(), passwordReq.getPasswordNew2())) {
            message = "Mật khẩu nhập lại không khớp";
        } else {
            Account account = accountService.findAccountById(id).orElseThrow();
            if (passwordEncoder.matches(passwordReq.getPasswordNew(), account.getPassword())) {
                message = "Mật khẩu cũ không đúng";
            } else {
                account.setPassword(passwordEncoder.encode(passwordReq.getPasswordNew()));
                accountService.saveAccount(account);
            }
        }
        model.addAttribute("message", message);
        return "user/password";
    }

}
