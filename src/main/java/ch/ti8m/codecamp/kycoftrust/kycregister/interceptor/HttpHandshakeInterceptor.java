package ch.ti8m.codecamp.kycoftrust.kycregister.interceptor;

import ch.ti8m.codecamp.kycoftrust.kycregister.domain.UsernameEntity;
import ch.ti8m.codecamp.kycoftrust.kycregister.service.UsernameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private UsernameService usernameService;

    public HttpHandshakeInterceptor(UsernameService usernameService) {
        this.usernameService = usernameService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            log.info("Before Handshake");

            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) serverHttpRequest;
            HttpSession session = servletRequest.getServletRequest().getSession();
            map.put("sessionId", session.getId());

            HttpServletRequest request = servletRequest.getServletRequest();
            String usernameString = request.getHeader("username");
            UsernameEntity username = new UsernameEntity();
            username.setUsername(usernameString);
            UsernameEntity savedUsername = usernameService.saveUsername(username);
            log.info("Username saved: " + savedUsername.getUsername());
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        log.info("After Handshake");
    }
}
