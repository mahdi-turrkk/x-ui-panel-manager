package online.gixmetir.xuipanelmanagerbackend.security.filter;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import online.gixmetir.xuipanelmanagerbackend.entities.UserEntity;
import online.gixmetir.xuipanelmanagerbackend.models.Role;
import online.gixmetir.xuipanelmanagerbackend.security.jwt.JwtService;
import online.gixmetir.xuipanelmanagerbackend.services.app.AuthenticationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;


    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "*");
//        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
//        response.addHeader("Access-Control-Expose-Headers", "xsrf-token");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUserName(jwt);
        if (StringUtils.isNotEmpty(userEmail)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserEntity userDetails = authenticationService.loadUserByUsername(userEmail);

            boolean tokenValid = jwtService.isTokenValid(jwt, userDetails);
            if (tokenValid) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
    }

//    private boolean getAccess(@Nonnull HttpServletRequest request, Role role) {
//        String uri = request.getRequestURI();
//        if (uri.contains("servers") || uri.contains("inbounds") || uri.contains("databases")) {
//            return role == Role.Admin;
//        }
//
//        if (uri.contains("authentication")) {
//            return true;
//        }
//
//        if (uri.contains("users")) {
//            if (uri.contains("change-password"))
//                return role == Role.Admin || role == Role.Customer;
//            return role == Role.Admin;
//        }
//        if (uri.contains("subscriptions")) {
//            if (uri.contains("report") || uri.contains("client"))
//                return true;
//            if (uri.contains("create") || uri.contains("update") || uri.contains("get-all") || uri.contains("change-status"))
//                return role == Role.Admin || role == Role.Customer;
//            return role == Role.Admin;
//        }
//        return false;
//    }

//    private void denyAccess(@Nonnull HttpServletResponse response) throws IOException {
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        response.setContentType("application/json");
//        response.getWriter().write("{\"error\": \"Access Forbidden\"}");
//        response.getWriter().flush();
//    }
}
