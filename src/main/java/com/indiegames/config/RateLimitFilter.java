package com.indiegames.config;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private static long quantidade = 20;
    private static Duration duracao = Duration.ofMinutes(1);
    private final Map<String, Bucket> bucketMap = new ConcurrentHashMap<>();

    public Bucket newBucket(){
        return Bucket.builder()
                .addLimit(limite -> limite
                        .capacity(quantidade)
                        .refillIntervally(quantidade, duracao))
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if("OPTIONS".equals(request.getMethod())){
            filterChain.doFilter(request, response);
            return;
        }

        Bucket bucket = bucketMap.computeIfAbsent(clientIp(request), nb -> newBucket());
        ConsumptionProbe probe =  bucket.tryConsumeAndReturnRemaining(1);

        if(probe.isConsumed()){
            response.setHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(request, response);
        }
        else {
            long retryAfter = TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill()) + 1;
            response.setHeader("Retry-After", String.valueOf(retryAfter));
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Too many requests, try again in " + retryAfter + "s\"}");
        }
    }

    private String clientIp(HttpServletRequest request){
        String formatado = request.getHeader("X-Forwarded-For");
        if (formatado != null && !formatado.isBlank()){
            return formatado.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

}
