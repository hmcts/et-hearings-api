package uk.gov.hmcts.reform.et.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ecm.common.idam.models.UserDetails;
import uk.gov.hmcts.reform.et.config.OAuth2Configuration;
import uk.gov.hmcts.reform.et.domain.TokenRequest;
import uk.gov.hmcts.reform.et.domain.TokenResponse;
import uk.gov.hmcts.reform.et.idam.IdamApi;

@Component
public class UserService implements uk.gov.hmcts.ecm.common.service.UserService {
    private final IdamApi idamApi;
    private final OAuth2Configuration oauth2Configuration;

    public static final String OPENID_GRANT_TYPE = "password";

    @Autowired
    public UserService(IdamApi idamApi, OAuth2Configuration oauth2Configuration) {
        this.idamApi = idamApi;
        this.oauth2Configuration = oauth2Configuration;
    }

    @Override
    public UserDetails getUserDetails(String authorisation) {
        return idamApi.retrieveUserDetails(authorisation);
    }

    @Override
    public UserDetails getUserDetailsById(String authToken, String userId) {
        return idamApi.getUserByUserId(authToken, userId);
    }

    public TokenResponse getAccessTokenResponse(String username, String password) {
        return idamApi.generateOpenIdToken(
            new TokenRequest(
                oauth2Configuration.getClientId(),
                oauth2Configuration.getClientSecret(),
                OPENID_GRANT_TYPE,
                oauth2Configuration.getRedirectUri(),
                username,
                password,
                oauth2Configuration.getClientScope(),
                null,
                null
            ));
    }

    public String getAccessToken(String username, String password) {
        return getAccessTokenResponse(username, password).accessToken;
    }

}
