package fa.hust.controllers;

import fa.hust.dto.TotalMoney;
import fa.hust.entities.Account;
import fa.hust.entities.Stories;
import fa.hust.service.AccountService;
import fa.hust.service.PaymentService;
import fa.hust.service.StoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class StatisticalController {

    private final StoriesService storiesService;
    private final AccountService accountService;
    private final PaymentService paymentService;
    @GetMapping("/statistical")
    public String getStatistical(Model model){
        List<Stories> stories = storiesService.findAll();
        Integer full = stories.stream().filter(x->x.getStatus().name().equals("FULL")).toList().size();
        Integer pending = stories.stream().filter(x->x.getStatus().name().equals("PENDING")).toList().size();
        Integer account = accountService.findAll().size();
        Long totalAmount = paymentService.totalAmount();
        List<TotalMoney> totalMonies = paymentService.getT();
        List<Long> ids = totalMonies.stream().map(TotalMoney::getStoryId).toList();
        List<Stories> storiesList = storiesService.findByIds(ids);
        List<Account> accounts = accountService.findA();
        model.addAttribute("full",full);
        model.addAttribute("accounts",accounts);
        model.addAttribute("totalMonies",totalMonies);
        model.addAttribute("storiesList",storiesList);
        model.addAttribute("pending",pending);
        model.addAttribute("account",account);
        model.addAttribute("totalAmount",totalAmount);
        return "/admin/statistical";
    }
}
