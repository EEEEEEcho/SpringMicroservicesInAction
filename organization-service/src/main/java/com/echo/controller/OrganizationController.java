package com.echo.controller;



import com.echo.pojo.Organization;
import com.echo.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/organizations/")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @RequestMapping("/{organizationId}")
    public Organization findOrganizationById(@PathVariable("organizationId") String organizationId){
        return organizationService.findOrganizationById(organizationId);
    }
}
