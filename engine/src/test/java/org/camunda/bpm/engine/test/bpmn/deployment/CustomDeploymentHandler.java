/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.test.bpmn.deployment;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.bpmn.deployer.BpmnDeployer;
import org.camunda.bpm.engine.impl.util.StringUtil;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentHandler;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.Resource;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomDeploymentHandler implements DeploymentHandler {

  protected int currentVersionTag;

  public CustomDeploymentHandler(int currentVersionTag) {
    this.currentVersionTag = currentVersionTag;
  }

  @Override
  public boolean shouldDeployResource(Resource newResource, Resource existingResource) {

    if (isBpmnResource(newResource)) {
      BpmnModelInstance existingModel = Bpmn.readModelFromStream(
          new ByteArrayInputStream(existingResource.getBytes())
      );
      BpmnModelInstance newModel = Bpmn.readModelFromStream(
          new ByteArrayInputStream(newResource.getBytes())
      );

      Process existingProcess = existingModel.getDefinitions()
          .getChildElementsByType(Process.class).iterator().next();
      Process newProcess = newModel.getDefinitions()
          .getChildElementsByType(Process.class).iterator().next();

      int existingVersion = existingProcess.getCamundaVersionTag() != null?
          Integer.valueOf(existingProcess.getCamundaVersionTag()) : 0;
      int newVersion = newProcess.getCamundaVersionTag() != null?
          Integer.valueOf(newProcess.getCamundaVersionTag()) : 1;

      return newVersion > existingVersion;
    }

    return false;
  }

  @Override
  public Collection<String> determineDeploymentsToResumeByDeploymentName(
      RepositoryService repositoryService,
      Deployment baseDeployment) {

    throw new ProcessEngineException("RESUME_BY_DEPLOYMENT_NAME is not supported with this Deployment Handler!");
  }

  @Override
  public Collection<String> determineDeploymentsToResumeByProcessDefinition(List<ProcessDefinition> processDefinitions) {
    Set<String> deploymentIds = new HashSet<>();
    for (ProcessDefinition processDefinition : processDefinitions) {
      if (Integer.valueOf(processDefinition.getVersionTag()) > currentVersionTag) {
        deploymentIds.add(processDefinition.getDeploymentId());
      }
    }

    return deploymentIds;
  }

  protected boolean isBpmnResource(Resource resource) {
    return StringUtil.hasAnySuffix(resource.getName(), BpmnDeployer.BPMN_RESOURCE_SUFFIXES);
  }
}
