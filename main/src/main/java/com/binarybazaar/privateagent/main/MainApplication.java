package com.binarybazaar.privateagent.main;

import com.binarybazaar.privateagent.main.configuration.Config;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.util.logging.Level;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jvnet.hk2.annotations.Contract;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.LoggerFactory;

public class MainApplication extends MainApplicationBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    private final Server server;
    private final Scheduler scheduler;

    private final ScanResult scanResult;

    public static void main(String[] args) throws Exception {
        java.util.logging.Logger bridgeLogger = java.util.logging.Logger.getLogger("");
        bridgeLogger.setLevel(Level.FINEST);

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();

        Server server = new Server(Config.get().port());
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath(Config.get().basePath());
        server.setHandler(context);

        ResourceConfig rc = new ResourceConfig();
//        AppRequestFilter appRequestFilter = new AppRequestFilter(new AppSessionAuthenticator(emp), new AppBearerAuthenticator(emp));
//        rc.register(new AppAuthenticationDynamicFeature(appRequestFilter));
//        rc.register(new AppAuthorizationDynamicFeature());
        rc.register(MultiPartFeature.class);

        initRequestLogging(rc, bridgeLogger);

        ServletHolder resourceServletHolder = new ServletHolder(new ServletContainer(rc));
        resourceServletHolder.setInitOrder(0);
        context.addServlet(resourceServletHolder, "/api/*");

        initMessaging(context);
        try ( ScanResult scanResult = scanApp()) {
            bindResources(scanResult, rc);
            bindProviders(scanResult, rc);

            MainApplication kms = new MainApplication(server, scanResult, scheduler);
            rc.register(kms);
            kms.init();
        }
        initBridgeLogging();
    }

    public MainApplication(Server server, ScanResult scanResult,
            Scheduler scheduler) {
        this.server = server;
        this.scanResult = scanResult;
        this.scheduler = scheduler;
    }

    @Override
    protected void configure() {
        super.bind(scheduler).to(Scheduler.class);

        for (ClassInfo routeClassInfo : scanResult.getClassesWithAnnotation(Singleton.class.getName())) {
            super.bindAsContract(routeClassInfo.loadClass()).in(Singleton.class);
        }
        for (ClassInfo routeClassInfo : scanResult.getClassesWithAnnotation(Contract.class.getName())) {
            super.bindAsContract(routeClassInfo.loadClass());
        }
    }

    public void init() {
        start();
        shutdownHook();
    }

    public void shutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stop();
        }));
    }

    public void start() {
        try {
            server.start();
            scheduler.start();
        } catch (Exception ex) {
            LOGGER.error("Stopping, because of exception!", ex);
            stop();
        }
    }

    public void stop() {
        try {
            scheduler.shutdown();
            server.stop();
            server.join();
        } catch (Exception ex) {
            LOGGER.error("Not stopping cleanly", ex);
        } finally {
            server.destroy();
        }
    }
}
