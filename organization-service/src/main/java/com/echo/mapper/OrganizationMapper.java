package com.echo.mapper;

import com.echo.pojo.Organization;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrganizationMapper {

    @Select("select * from organizations where id = #{id}")
    @Results({
            @Result(property = "organizationId",column = "id"),
            @Result(property = "contactName",column = "contact_name"),
            @Result(property = "contactEmail",column = "contact_email"),
            @Result(property = "contactPhone",column = "contact_phone"),
            @Result(property = "comment",column = "conment")
    })
    public Organization findOrganizationById(@Param("id") String id);
}
