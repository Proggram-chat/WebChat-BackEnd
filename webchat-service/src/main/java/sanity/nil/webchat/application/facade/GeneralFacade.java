package sanity.nil.webchat.application.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sanity.nil.webchat.application.consts.TokenType;
import sanity.nil.webchat.application.dto.role.FunctionDTO;
import sanity.nil.webchat.application.interfaces.repository.MemberRoleRepository;
import sanity.nil.webchat.infrastructure.security.auth.TokenHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeneralFacade {

    private final TokenHelper tokenHelper;
    private final MemberRoleRepository memberRoleRepository;

    public List<FunctionDTO> getAllFunctions() {
        return memberRoleRepository.getAllFunctions().stream()
                .map(e -> new FunctionDTO(e.getId(), e.getAction()))
                .toList();
    }

    public String getToken(TokenType tokenType, String channel) {
        // "personal:{userID}"
        Map<String, Object> claims = new HashMap<>();
        switch (tokenType) {
            case CONNECTION -> {
                // TODO: channel isn't needed here, we should understand which user it is for from IdentityProvider
                log.info(channel.substring(channel.lastIndexOf(":") + 1));
                UUID userID = UUID.fromString(channel.substring(channel.lastIndexOf(":") + 1));
                claims.put("sub", userID.toString());
            }
            case SUBSCRIPTION -> {
                log.info(channel.substring(channel.lastIndexOf(":") + 1));
                UUID userID = UUID.fromString(channel.substring(channel.lastIndexOf(":") + 1));
                claims.put("sub", userID.toString());
                claims.put("channel", channel);
            }
            default -> {
                log.error("Unsupported token type {}", tokenType);
            }
        }

        return tokenHelper.createToken(claims);
    }
}
