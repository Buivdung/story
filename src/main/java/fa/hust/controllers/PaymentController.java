package fa.hust.controllers;

import fa.hust.entities.*;
import fa.hust.enums.EPaymentStatus;
import fa.hust.req.PayResultReq;
import fa.hust.req.SearchReq;
import fa.hust.resp.VnPayResp;
import fa.hust.service.PaidService;
import fa.hust.service.PaymentService;
import fa.hust.service.StoriesService;
import fa.hust.utils.SearchUtil;
import fa.hust.utils.VnPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Objects;


@Controller
@RequiredArgsConstructor
public class PaymentController {
    private final VnPayUtil vnPayUtil;
    private final StoriesService storiesService;
    private final PaymentService paymentService;
    private final PaidService paidService;
    private final SearchUtil searchUtil;

    @GetMapping("/create_pay/{id}")
    public String createPay(@PathVariable Long id, HttpServletRequest req, Model model) {
        Stories story = storiesService.findById(id).orElseThrow();
        Account account = (Account) req.getSession().getAttribute("account");
        if (account.getId() == null) {
         return "ui/login";
        }
        Payment payment = Payment.builder()
                .story(story)
                .account(account)
                .amount(story.getMoney())
                .status(EPaymentStatus.PENDING)
                .bankCode("NCB")
                .comment("Thanh toán hóa đơn truyện " + story.getName())
                .build();
        payment = paymentService.savePayment(payment);
        VnPayResp vnPayResp = VnPayResp.builder().url(vnPayUtil.getQuery(req, story, payment)).build();
        model.addAttribute("vnPayResp", vnPayResp);
        model.addAttribute("story", story);
        return "ui/create-payment";
    }

    @GetMapping("/vnpay/transaction")
    public String getResult(@ModelAttribute PayResultReq payResultReq, Model model) {

        Payment payment = paymentService.findById(Long.valueOf(payResultReq.getVnp_TxnRef()))
                .orElseThrow(() -> new RuntimeException("Không tồn tại hóa đơn này."));
        if (Objects.equals(payment.getAmount() * 100, payResultReq.getVnp_Amount())
                && "00".equals(payResultReq.getVnp_TransactionStatus())
                && EPaymentStatus.PENDING.equals(payment.getStatus())) {
            payment.setStatus(EPaymentStatus.SUCCESS);
            model.addAttribute("message", "Thanh cong");
            payment = paymentService.savePayment(payment);
            KeyPaid keyPaid = KeyPaid.builder()
                    .accountId(payment.getAccount().getId())
                    .storyId(payment.getStory().getId())
                    .build();
            Paid paid = Paid.builder()
                    .id(keyPaid)
                    .account(payment.getAccount())
                    .story(payment.getStory()).build();
            paidService.savePaid(paid);
        } else {
            payment.setStatus(EPaymentStatus.FAIL);
            model.addAttribute("message", "That bai");
        }
        model.addAttribute("story", payment.getStory());
        return "ui/create-payment";
    }
    @GetMapping("/admin/payment")
    public String getPayment(@ModelAttribute SearchReq searchReq, Model model) {
        searchReq = searchUtil.convertSearchReq(searchReq);
        Pageable pageable = PageRequest.of(searchReq.getPageNumber() - 1, searchReq.getPageSize());
        Page<Payment> payments = paymentService.findAll(searchReq.getParam(), pageable);
        model.addAttribute("searchResp", searchUtil.setSearch(searchReq, payments.getTotalPages()));
        model.addAttribute("payments", payments);
        return "admin/payment-history";
    }
}
