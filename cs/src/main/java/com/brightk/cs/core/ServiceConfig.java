package com.brightk.cs.core;


public class ServiceConfig {
    private CsService service;
    public final ServiceType type;
    public final Class<CsService> serviceClass;

    public ServiceConfig(Class<CsService> service, ServiceType type) {
        this.serviceClass = service;
        this.type = type;
    }

    public void setService(CsService service) {
        this.service = service;
    }

    public CsService getService() {
        return service;
    }


}
