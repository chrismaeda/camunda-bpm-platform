package org.camunda.bpm.engine.impl.cmd.deployment;

import org.camunda.bpm.engine.ProcessEngine;

public class SemVerDeploymentHandlerFactory implements DeploymentHandlerFactory {

  @Override
  public DeploymentHandler buildDeploymentHandler(ProcessEngine processEngine) {
    return new SemVerDeploymentHandler(processEngine);
  }

}
