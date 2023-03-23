package project.br.useAuthentication.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.br.useAuthentication.exception.AuthenticationExceptionResponse;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
	
	@Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
	
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException {
    	AuthenticationExceptionResponse exception = new AuthenticationExceptionResponse(res.getContentType());
    	resolver.resolveException(req, res, null, exception);
    }
}