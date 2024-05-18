package com.eminyidle.gateway.exhandler;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode() == HttpStatus.BAD_REQUEST
                || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

    }
}
