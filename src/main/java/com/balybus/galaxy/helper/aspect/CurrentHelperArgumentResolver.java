package com.balybus.galaxy.helper.aspect;

import com.balybus.galaxy.helper.domain.TblHelper;
import com.balybus.galaxy.helper.repository.HelperRepository;
import com.balybus.galaxy.member.domain.TblUser;
import com.balybus.galaxy.member.repository.MemberRepository;
import com.balybus.galaxy.global.exception.BadRequestException;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.balybus.galaxy.global.exception.ExceptionCode.*;

public class CurrentHelperArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberRepository memberRepo;
    private final HelperRepository helperRepo;

    public CurrentHelperArgumentResolver(MemberRepository memberRepo, HelperRepository helperRepo) {
        this.memberRepo = memberRepo;
        this.helperRepo = helperRepo;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentHelper.class)
                && parameter.getParameterType().equals(AccessorHelper.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetails)) {
            throw new BadRequestException(MEMBER_NOT_FOUND);
        }

        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        TblUser user = memberRepo.findByEmail(username)
                .orElseThrow(() -> new BadRequestException(MEMBER_NOT_FOUND));
        TblHelper helper = helperRepo.findByUserId(user.getId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_HELPER));

        return new AccessorHelper(helper);
    }
}
