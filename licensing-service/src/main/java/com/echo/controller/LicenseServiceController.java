package com.echo.controller;

import com.alibaba.fastjson.JSON;
import com.echo.pojo.License;
import com.echo.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/organizations")
public class LicenseServiceController {
    @Autowired
    LicenseService licenseService;

    @RequestMapping(value = "/{organizationId}/licenses/{licenseId}",method = RequestMethod.GET)
    public License getLicenseByOrganizationIdAndLicenseId(@PathVariable("organizationId")String organizationId,@PathVariable("licenseId")String licenseId){
        return licenseService.findByOrganizationIdAndLicenseId(organizationId, licenseId);
    }

    @RequestMapping(value = "/{organizationId}",method = RequestMethod.GET)
    public String getLicensesByOrganizationId(@PathVariable("organizationId") String organizationId){
        List<License> licenses = licenseService.findByOrganizationId(organizationId);
        return JSON.toJSONString(licenses);
    }
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    public String findAll(){
        List<License> all = licenseService.findAll();
        return JSON.toJSONString(all);
    }

    @RequestMapping(value = "/{organizationId}/{licenseId}/{clientType}")
    public License getLicenseWithClient(@PathVariable("organizationId") String organizationId,
                                        @PathVariable("licenseId") String licenseId,
                                        @PathVariable("clientType") String clientType){
        return licenseService.getLicenseWithClient(organizationId,licenseId,clientType);
    }
}
