package com.echo.service.impl;

import com.echo.client.OrganizationClient;
import com.echo.mapper.LicenseMapper;
import com.echo.pojo.License;
import com.echo.pojo.Organization;
import com.echo.service.LicenseService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
//类级别的Hystrix注解，控制该类中所有的Hystrix共享相同的配置
@DefaultProperties(
        commandProperties = {
                @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "10000")
        }
)
public class LicenseServiceImpl implements LicenseService {
    @Autowired
    private LicenseMapper licenseMapper;
//    @Autowired
//    private OrganizationRestTemplateClient client;
    @Autowired
    OrganizationClient organizationClient;

    @Override
    //@HystrixCommand //该注解将该方法交由Hystrix断路器管理，Spring看到该注解时，将生成一个动态代理。该代理将包装该方法，
                    //并通过专门用于处理远程调用的线程池来管理对该方法的所有调用，默认超时1秒钟，发生断路

//    @HystrixCommand(
//            //定制断路器行为
//            commandProperties = {
//                    //设置断路器超时时间为12秒
//                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "12000")
//            }
//    )
    //设置后备方法
    //@HystrixCommand(fallbackMethod = "buildFallbackLicenseList")
    //实现壁仓模式，隔离线程池
    @HystrixCommand(fallbackMethod = "buildFallbackLicenseList",
        threadPoolKey = "licenseByOrgThreadPool",//线程池的名字
        threadPoolProperties = {    //如果不定义该属性，将会使用Hystrix的默认配置，定义的话则使用该属性中的配置
            @HystrixProperty(name = "coreSize",value = "30"),   //线程池中线程的最大数量
            @HystrixProperty(name = "maxQueueSize",value = "10")    //线程池前面的队列，可以对传入的请求进行排队
        }
    )
    public List<License> findByOrganizationId(String organizationId) {
        try{
            Thread.sleep(1100);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return licenseMapper.findByOrganizationId(organizationId);
    }

    @Override
    public License findByOrganizationIdAndLicenseId(String organizationId, String licenseId) {
        return licenseMapper.findByOrganizationIdAndLicenseId(organizationId, licenseId);
    }

    //断路器的行为
    @HystrixCommand(
            fallbackMethod = "buildFallbackLicenseList",
            threadPoolKey = "licenseByOrgThreadPool",//线程池的名字
            threadPoolProperties = {    //如果不定义该属性，将会使用Hystrix的默认配置，定义的话则使用该属性中的配置
                    @HystrixProperty(name = "coreSize",value = "30"),   //线程池中线程的最大数量
                    @HystrixProperty(name = "maxQueueSize",value = "10")    //线程池前面的队列，可以对传入的请求进行排队
            },
            commandProperties = {
                    //断路监控时间窗口期内，需要发生的连续调用的数量
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
                    //超过连续调用的阈值之后，失败的调用数量所占用的百分比
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "75"),
                    //断路器跳闸之后，允许另一个请求通过以便查看被调用服务是否恢复健康之前的休眠时间
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "7000"),
                    //这个是用来监视服务调用问题的窗口大小，默认值为10秒(10000ms)
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds",value = "15000"),
                    //在定义的滚动窗口中收集统计信息的次数，在这个窗口中，Hystrix在桶中收集数据，并检查桶中统计的信息，以确定远程资源调用是否失败
                    //监视窗口的大小必须被桶的数量整除，本例中，用15s的窗口，将统计数据收集到长度为3s的5个桶中
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets",value = "5")
            }
    )
    @Override
    public List<License> findAll() {
        return licenseMapper.findAll();
    }

    @Override
    public License getLicenseWithClient(String organizationId, String licenseId, String clientType) {
        License license = licenseMapper.findByOrganizationIdAndLicenseId(organizationId,licenseId);
        Organization organization = retrieveOrgInfo(organizationId,clientType);
        license.setOrganization(organization);
        return license;
    }

    private Organization retrieveOrgInfo(String organizationId,String clientType){
        System.out.println(clientType);
        return organizationClient.getOrganizationById(organizationId);
    }

    private List<License> buildFallbackLicenseList(String origanizationId){
        ArrayList<License> licenses = new ArrayList<>();
        License license = new License();
        license.setLicenseId("0000000-00-00000");
        license.setOrganizationId(origanizationId);
        license.setProductName("Sorry no licensing information currently available");
        licenses.add(license);
        return licenses;
    }
}
