package com.eminyidle.user.adapter.out.web;

import com.eminyidle.user.application.port.out.DeleteUserRequestPort;
import com.eminyidle.user.exception.SendRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class UserWebClientAdapter implements DeleteUserRequestPort {

	@Value("${INTERNAL_KEY}")
	private String INTERNAL_KEY;

	@Value("${AUTH_SERVICE_URL}")
	private String AUTH_SERVICE_URL;

	@Override
	public void deleteUserRequest(String userId) {
		WebClient webClient = WebClient.builder().baseUrl(AUTH_SERVICE_URL)
			.defaultHeader("InternalKey", INTERNAL_KEY).build();

		webClient.get().uri("/auth/user").header("UserId", userId).exchangeToMono(
			clientResponse -> {
				if (clientResponse.statusCode().is4xxClientError()) {
					throw new SendRequestException();
				}
				return clientResponse.bodyToMono(new ParameterizedTypeReference<Void>() {
				});
			}
		).block();
	}
}
