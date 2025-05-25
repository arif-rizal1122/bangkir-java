package com.bank.banking.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthenticationFIlter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, 
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // --- PASTE LOGIKA BYPASS INI DI SINI ---
        // Jika request adalah untuk endpoint login atau create, langsung teruskan ke filter berikutnya
        // Tanpa mencoba memvalidasi token JWT
        if (request.getServletPath().equals("/api/user/login") || 
            request.getServletPath().equals("/api/user/create") ||
            request.getServletPath().startsWith("/swagger-ui/") || // Tambahkan ini juga
            request.getServletPath().startsWith("/v3/api-docs/")) { // Tambahkan ini juga
            
            filterChain.doFilter(request, response);
            return; // Sangat penting: Hentikan eksekusi filter ini untuk path-path yang diizinkan
        }
        // --- AKHIR LOGIKA BYPASS ---
        
        String token = getTokenFromRequest(request);     
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }   
        // Panggilan ini hanya akan tercapai jika bukan endpoint yang di-bypass dan tidak ada token
        // atau token tidak valid. Jika tidak ada token (misalnya, di endpoint yang tidak di-bypass tapi belum login),
        // maka filterChain.doFilter akan diteruskan dan filter SecurityContextHolderFilter akan melihat
        // tidak ada Authentication di SecurityContext, yang bisa menyebabkan 403 jika 'anyRequest().authenticated()'
        // adalah aturan terakhir.
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}