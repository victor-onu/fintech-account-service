package com.victor.fintechaccountservice.client;

import com.victor.fintechaccountservice.config.LoadBalancerConfiguration;
import com.victor.fintechaccountservice.service.dto.AccountOwnerDTO;
import java.util.List;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@FeignClient(value = "notification-service", url = "${application.notificationServiceUrl}", configuration = UserFeignClientInterceptor.class)
@FeignClient(name = "notification-service", configuration = UserFeignClientInterceptor.class)
@LoadBalancerClient(name = "notification-service", configuration = LoadBalancerConfiguration.class)
public interface NotificationServiceClient {
    @RequestMapping(method = RequestMethod.POST, value = "/api/emails/send-mails")
    Boolean sendCreateAccountMail(@RequestBody AccountOwnerDTO accountOwnerDTO);
}
