package com.echo.mapper;

import com.echo.pojo.License;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface LicenseMapper {
    public List<License> findByOrganizationId(@Param("organizationId") String organizationId);

    public License findByOrganizationIdAndLicenseId(@Param("organizationId") String organizationId,
                                                    @Param("licenseId") String licenseId);
    public List<License> findAll();
}
