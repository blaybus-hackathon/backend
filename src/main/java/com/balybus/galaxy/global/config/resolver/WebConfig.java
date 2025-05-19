package com.balybus.galaxy.global.config.resolver;

import com.balybus.galaxy.helper.aspect.CurrentHelperArgumentResolver;
import com.balybus.galaxy.helper.repository.HelperRepository;
import com.balybus.galaxy.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private HelperRepository helperRepo;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentHelperArgumentResolver(memberRepo, helperRepo));
    }
}
