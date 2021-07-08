package com.echo.service.impl;

import com.echo.mapper.OrganizationMapper;
import com.echo.pojo.Organization;
import com.echo.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Organization findOrganizationById(String id) {
        System.out.println(id);
        Organization organizationById = organizationMapper.findOrganizationById(id);
        System.out.println(organizationById);
        return organizationById;
    }
}
