package uk.gov.hmcts.reform.et.config.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.gov.hmcts.reform.et.service.VerifyTokenService;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Intercepts any call to the et-hearings-api and validates the token.
 */
@Slf4j
@Component
public class RequestInterceptor implements HandlerInterceptor {

    private static final String FAILED_TO_VERIFY_TOKEN = "Failed to verify the following token: {}";

    final VerifyTokenService verifyTokenService;

    public RequestInterceptor(VerifyTokenService verifyTokenService) {
        this.verifyTokenService = verifyTokenService;
    }

    /**
     * Intercepts any incoming calls and throws exception if token is invalid.
     *
     * @param requestServlet  current HTTP request
     * @param responseServlet current HTTP response
     * @param handler         chosen handler to execute, for type and/or instance evaluation
     * @return true if the token is verified
     */
    @Override
    public boolean preHandle(HttpServletRequest requestServlet,
                             @NotNull HttpServletResponse responseServlet,
                             @NotNull Object handler) {
        String authorizationHeader = requestServlet.getHeader(AUTHORIZATION);
        boolean jwtVerified = verifyTokenService.verifyTokenSignature(authorizationHeader);
        if (!jwtVerified) {
            log.error(FAILED_TO_VERIFY_TOKEN, authorizationHeader);
            throw new UnAuthorisedServiceException("Failed to verify bearer token.");
        }
        return true;
    }
}
