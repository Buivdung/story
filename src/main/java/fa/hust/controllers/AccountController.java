package fa.hust.controllers;

import fa.hust.req.AccountReq;
import fa.hust.req.SearchReq;
import fa.hust.entities.Account;
import fa.hust.entities.Information;
import fa.hust.service.AccountService;
import fa.hust.utils.SearchUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final SearchUtil searchUtil;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/accounts")
    public String getAccounts(@ModelAttribute SearchReq searchReq,
                              @ModelAttribute AccountReq accountReq,
                              Model model) {
        searchReq = searchUtil.convertSearchReq(searchReq);
        Pageable pageable = PageRequest.of(searchReq.getPageNumber() - 1, searchReq.getPageSize());
        Page<Account> accounts = accountService.findAll(searchReq.getParam(), pageable);
        model.addAttribute("searchResp", searchUtil.setSearch(searchReq, accounts.getTotalPages()));
        model.addAttribute("accounts", accounts);
        return "admin/users";
    }

    @PostMapping("/accounts")
    public String postAccounts(@ModelAttribute @Validated AccountReq accountReq,
                               BindingResult result,
                               Model model) {
        SearchReq searchReq = new SearchReq();
        if (result.hasErrors()) {
            searchReq.setAlert("Thêm người dùng thất bại, mở mục thêm người dùng để biết chi tiết lỗi");
        } else {
            accountService.saveAccount(getAccount(accountReq, new Account()));

            searchReq.setAlert("Thêm người dùng thành công");
        }
        return getAccounts(searchReq, accountReq, model);
    }


    @GetMapping("/accounts/delete/{id}")
    public String deleteAccounts(@PathVariable Long id) {
        Account account = accountService.findAccountById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        accountService.deleteAccount(account);
        return "redirect:/admin/accounts";
    }

    @GetMapping("/account/{id}")
    public String edit(@ModelAttribute AccountReq accountReq,
                       @PathVariable Long id,
                       Model model) {
        Account account = accountService.findAccountById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        model.addAttribute("account", account);
        return "admin/editUser";
    }

    @PostMapping("/account/{id}")
    public String editAccounts(@ModelAttribute @Validated AccountReq accountReq,
                               BindingResult result,
                               @PathVariable Long id,
                               Model model) {
        if (!result.hasErrors()) {
            Account account = accountService.findAccountByIdAndEmail(id, accountReq.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
            accountService.saveAccount(getAccount(accountReq, account));
        }
        return edit(accountReq, id, model);
    }

    private Account getAccount(AccountReq accountReq, Account account) {
        modelMapper.map(accountReq, account);
        Information information = modelMapper.map(accountReq, Information.class);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        information.setAvatar("common.jpg");
        information.setAccount(account);
        account.setInformation(information);
        return account;
    }

}
