/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cabot;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;

/**
 *
 * @author Yocho
 */
public class MyModule extends AbstractModule {

    private final Scheduler scheduler;

    public MyModule(Scheduler scheduler){
        this.scheduler = scheduler;
    }
    
    @Override
    protected void configure() {
        bind(Scheduler.class).toInstance(scheduler);
        install(new FactoryModuleBuilder().implement(JobFactory.class, GuiceJobFactory.class).build(GuiceAssistedJobFactory.class));
    }
    
}
