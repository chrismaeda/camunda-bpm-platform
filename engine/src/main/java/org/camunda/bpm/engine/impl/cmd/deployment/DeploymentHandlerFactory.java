package org.camunda.bpm.engine.impl.cmd.deployment;

import org.camunda.bpm.engine.ProcessEngine;

public interface DeploymentHandlerFactory {

  DeploymentHandler buildDeploymentHandler(ProcessEngine processEngine);
}
