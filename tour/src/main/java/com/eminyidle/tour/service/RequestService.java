package com.eminyidle.tour.service;

import com.eminyidle.tour.dto.User;
import com.eminyidle.tour.dto.res.UserRes;
import com.eminyidle.tour.exception.FailToGetUserRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Component
@Slf4j
public class RequestService {

    @Value("${USER_SERVER_URL}")
    private String userServerUrl;

    private RestTemplate restTemplate = new RestTemplate();


    public User getUser(String userId){
        HttpHeaders headers=new HttpHeaders();
        headers.add("UserId",userId);
        HttpEntity<Void> entity=new HttpEntity<>(headers);
        UserRes userRes=null;
        try{
            ResponseEntity<UserRes> res=restTemplate.exchange(userServerUrl+"/user", HttpMethod.GET,entity,UserRes.class);
            userRes= res.getBody();
            log.debug(">>..."+res.toString());
        } catch(HttpClientErrorException ex){
            log.debug("HttpException");
            if(ex.getStatusCode()== HttpStatus.BAD_REQUEST){
                log.debug("bad");
                throw new FailToGetUserRequestException();
            }
        }catch (Exception ex) {
            // 그 외 예외 처리
            log.debug("EXception");
            throw new RuntimeException("API 호출 중 예외 발생", ex);
        }

        return User.builder()
                .userId(userId)
                .userName(userRes.getUserName())
                .userNickname(userRes.getUserNickname())
                .tourList(new ArrayList<>())
                .build();
    }

}
