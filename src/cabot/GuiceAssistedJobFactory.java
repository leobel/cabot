/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cabot;

import cabot.Settings.Service;
import org.quartz.spi.JobFactory;

/**
 *
 * @author leobel
 */
public interface GuiceAssistedJobFactory {
    JobFactory create(RevolicoAutomaticAdsFactory service, Service s);
}
