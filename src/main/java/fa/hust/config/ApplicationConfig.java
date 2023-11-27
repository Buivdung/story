package fa.hust.config;

import fa.hust.entities.Chapter;
import fa.hust.req.ChapterReq;
import fa.hust.req.SearchReq;
import fa.hust.req.StoryReq;
import fa.hust.entities.Stories;
import fa.hust.entities.Users;
import fa.hust.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ExpressionMap;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.event.AuthenticationFailureServiceExceptionEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import static org.modelmapper.Conditions.isNotNull;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    final private AccountService accountService;
    final private ExpressionMap<SearchReq, SearchReq> MAPPER_SEARCH = mapper -> {
        mapper.when(isNotNull()).map(SearchReq::getPageSize,SearchReq::setPageSize);
        mapper.when(isNotNull()).map(SearchReq::getPageNumber,SearchReq::setPageNumber);
        mapper.when(isNotNull()).map(SearchReq::getParam,SearchReq::setParam);
    };
    final private ExpressionMap<StoryReq, Stories> MAPPER_STORY = mapper -> {
        mapper.when(isNotNull()).map(StoryReq::getIntroduction,Stories::setIntroduction);
        mapper.when(isNotNull()).map(StoryReq::getThumbnail,Stories::setThumbnail);
        mapper.when(isNotNull()).map(StoryReq::getId,Stories::setId);
    };
    final private ExpressionMap<ChapterReq, Chapter> MAPPER_CHAPTER = mapper -> {
        mapper.when(isNotNull()).map(ChapterReq::getContent,Chapter::setContent);
        mapper.when(isNotNull()).map(ChapterReq::getNumber,Chapter::setNumber);
    };

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(SearchReq.class,SearchReq.class).addMappings( MAPPER_SEARCH);
        modelMapper.createTypeMap(StoryReq.class,Stories.class).addMappings( MAPPER_STORY);
        modelMapper.createTypeMap(ChapterReq.class,Chapter.class).addMappings( MAPPER_CHAPTER);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> new Users(accountService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User don't found")));
    }

    @Bean
    public AuthenticationProvider provider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.ALL_VALUE)
                .build();
    }
}
