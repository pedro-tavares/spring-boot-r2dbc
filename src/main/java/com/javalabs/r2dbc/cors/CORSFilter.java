package com.javalabs.r2dbc.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A Cross Origin Requests (CORS) Filter
 *
 * @author pedro-tavares
 * @since Mar 2015
 *
 */
@Component
public class CORSFilter implements Filter {

    private Logger LOG = LoggerFactory.getLogger(CORSFilter.class);

    public void init(FilterConfig filterConfig) {
        LOG.debug("{} initialised.", filterConfig.getFilterName());
    }

    public void destroy() {}

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600"); // 1 Hour
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        chain.doFilter(req, res);
    }

}