package amirhs.de.stage.config;

import amirhs.de.stage.auth.AuthenticationController;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final CookieHandler cookieHandler;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, CookieHandler cookieHandler) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.cookieHandler = cookieHandler;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String jwt = getJwtFromCookies(request);
        final String userEmail;

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (jwtService.isTokenBlacklisted(jwt)) {
            invalidateTokenAndSetCookie(response, jwt);
            filterChain.doFilter(request, response);
            return;
        }
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;
            try {
                userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            } catch (UsernameNotFoundException e) {
                invalidateTokenAndSetCookie(response, jwt);
                filterChain.doFilter(request, response);
                return;
            }
            if (userDetails != null && jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void invalidateTokenAndSetCookie(HttpServletResponse response, String jwt) {
        jwtService.invalidateToken(jwt);
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false) // todo set condition for dev and prod value
                .path("/")
                .maxAge(0) // Set maxAge to 0 to delete the cookie
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    @Nullable
    private String getJwtFromCookies(@NotNull HttpServletRequest request) {
        return cookieHandler.getJwtFromCookies(request);
    }
}
