package com.huiyu.auth.security.core.clientdetails;

import com.huiyu.common.core.result.R;
import com.huiyu.service.api.entity.SysOauthClient;
import com.huiyu.service.api.feign.SysOauthClientFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import com.huiyu.auth.common.enums.PasswordEncoderTypeEnum;

/**
 * OAuth2 客户端信息
 *
 * @author Naccl
 * @date 2022-03-07
 */
@RequiredArgsConstructor
@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {
    private final SysOauthClientFeignClient oauthClientFeignClient;

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        R<SysOauthClient> result = oauthClientFeignClient.queryByClientId(clientId);
        SysOauthClient client = result.getData();
        if (result.isSuccess() && client != null) {
            BaseClientDetails clientDetails = new BaseClientDetails(
                    client.getClientId(),
                    client.getResourceIds(),
                    client.getScope(),
                    client.getAuthorizedGrantTypes(),
                    client.getAuthorities(),
                    client.getWebServerRedirectUri()
            );
            clientDetails.setClientSecret(PasswordEncoderTypeEnum.NOOP.getPrefix() + client.getClientSecret());
            clientDetails.setAccessTokenValiditySeconds(client.getAccessTokenValidity());
            clientDetails.setRefreshTokenValiditySeconds(client.getRefreshTokenValidity());
            return clientDetails;
        }
        throw new NoSuchClientException("No client with requested id: " + clientId);
    }
}
