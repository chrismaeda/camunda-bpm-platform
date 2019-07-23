package org.camunda.bpm.engine.impl.cmd.deployment;

import java.util.List;

import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.bpm.engine.repository.Resource;

/**
 * Implementations do not need to be thread-safe. This means, you can keep context in a field
 */
public interface DeploymentHandler {
  
  /**
   * This comes first
   */
  List<Resource> determineResourcesToDeploy(CandidateDeployment candidate);

  /**
   * This comes second
   */
  String determineDeploymentsToActivate(CandidateDeployment candidate, DeploymentWithDefinitions newDeployment);

  /**
   * This comes third
   */
  List<String> determineDeploymentsToResume(CandidateDeployment candidate, DeploymentWithDefinitions newDeployment);
}
