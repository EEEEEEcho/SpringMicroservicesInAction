package com.echo.client;

import com.echo.pojo.Organization;

public class OrganizationClientFallBack implements OrganizationClient{
    @Override
    public Organization getOrganizationById(String organizationId) {
        System.out.println("出错了");
        return null;
    }
}
