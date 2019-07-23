package org.camunda.bpm.engine.impl.cmd.deployment;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.Resource;

public class SemVerDeploymentHandler implements DeploymentHandler {

  private List<ProcessDefinition> definitionsWithSameVersionTag;
  
  private RepositoryService repositoryService;
  
  public SemVerDeploymentHandler(ProcessEngine engine) {
    repositoryService = engine.getRepositoryService();
  }
  
  @Override
  public List<Resource> determineResourcesToDeploy(CandidateDeployment candidate) {
    
    List<Resource> resources = candidate.getResources();
    
    definitionsWithSameVersionTag = new ArrayList<ProcessDefinition>();
    List<Resource> resourcesToDeploy = new ArrayList<Resource>();
    
    for (Resource resource : resources) {
      String processDefinitionKey = extractProcessDefinitionKey(resource);
      String versionTag = extractVersionTag(resource);
      ProcessDefinition latestVersion = repositoryService.createProcessDefinitionQuery()
          .processDefinitionKey(processDefinitionKey)
          .latestVersion()
          .singleResult();
      
      if (hasHigherVersion(versionTag, latestVersion.getVersionTag())) {
        resourcesToDeploy.add(resource);
      }
      
      definitionsWithSameVersionTag.addAll(repositoryService.createProcessDefinitionQuery()
          .processDefinitionKey(processDefinitionKey)
          .versionTag(versionTag)
          .list());
    }
    
    
    return resourcesToDeploy;
  }

  private boolean hasHigherVersion(String versionTag, String versionTag2) {
    return false;
  }

  private String extractProcessDefinitionKey(Resource resource) {
    return null;
  }

  private String extractVersionTag(Resource resource) {
    return null;
  }

  @Override
  public String determineDeploymentsToActivate(CandidateDeployment candidate, DeploymentWithDefinitions newDeployment) {
    if (newDeployment != null) {
      return newDeployment.getId();
    }
    else {
      return definitionsWithSameVersionTag.iterator().next().getDeploymentId();
    }
  }

  @Override
  public List<String> determineDeploymentsToResume(CandidateDeployment candidate, DeploymentWithDefinitions newDeployment) {
    List<String> deploymentsToResume = new ArrayList<String>();
    
    if (newDeployment != null) {
      deploymentsToResume.add(newDeployment.getId());
    }
    
    for (ProcessDefinition processDefinition : definitionsWithSameVersionTag) {
      deploymentsToResume.add(processDefinition.getDeploymentId());
    }
    
    return deploymentsToResume;
  }

}
