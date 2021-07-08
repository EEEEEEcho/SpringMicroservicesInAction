package com.echo.client;

import com.echo.pojo.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "organizationservice",fallback = OrganizationClientFallBack.class)
public interface OrganizationClient {
    @RequestMapping(value = "/v1/organizations/{organizationId}",method = RequestMethod.GET,consumes = "application/json")
    public Organization getOrganizationById(@PathVariable("organizationId") String organizationId);
}
