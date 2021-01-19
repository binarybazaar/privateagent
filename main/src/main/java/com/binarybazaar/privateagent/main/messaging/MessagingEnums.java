package com.binarybazaar.privateagent.main.messaging;

public class MessagingEnums {

    public static enum WebsocketService {
        MARKET("market"),
        NONE("");

        private String service;

        private WebsocketService(String service) {
            this.service = service;
        }

        public String getService() {
            return service;
        }

        public static WebsocketService getEnum(String service) {
            for (WebsocketService serviceName : WebsocketService.values()) {
                if (serviceName.getService().toLowerCase().equals(service.toLowerCase())) {
                    return serviceName;
                }
            }
            return NONE;
        }
    }
}
