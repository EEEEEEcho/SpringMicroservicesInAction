package com.echo.service;

import com.echo.pojo.License;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface LicenseService {

    public List<License> findByOrganizationId(String organizationId);

    public License findByOrganizationIdAndLicenseId(String organizationId, String licenseId);

    public List<License> findAll();

    public License getLicenseWithClient(String organizationId, String licenseId, String clientType);
}
