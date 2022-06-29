package vaim.io.api;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-03-26T11:32:33.543Z[GMT]")
public class ApiOriginFilter implements javax.servlet.Filter, GeneralApi{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;        
        res.addHeader("Access-Control-Allow-Origin", ORIGIN);
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
