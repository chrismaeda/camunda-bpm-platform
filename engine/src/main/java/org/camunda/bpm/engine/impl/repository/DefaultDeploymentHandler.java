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
package org.camunda.bpm.engine.impl.repository;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.persistence.entity.ResourceEntity;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentHandler;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.Resource;
import org.camunda.bpm.engine.repository.ResumePreviousBy;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultDeploymentHandler implements DeploymentHandler {

  @Override
  public boolean shouldDeployResource(Resource newResource, Resource existingResource) {
    ResourceEntity newResourceEntity = (ResourceEntity) newResource;
    ResourceEntity existingResourceEntity = (ResourceEntity) existingResource;

    return resourcesDiffer(newResourceEntity, existingResourceEntity);
  }

  protected boolean resourcesDiffer(ResourceEntity resource, ResourceEntity existing) {
    byte[] bytes = resource.getBytes();
    byte[] savedBytes = existing.getBytes();
    return !Arrays.equals(bytes, savedBytes);
  }

  @Override
  public Collection<String> determineDeploymentsToResume(RepositoryService repositoryService,
    Deployment baseDeployment,
    List<ProcessDefinition> processDefinitions,
    String resumePreviousBy) {

    Set<String> deploymentIds = new HashSet<String>();
    switch (resumePreviousBy) {

      case ResumePreviousBy.RESUME_BY_DEPLOYMENT_NAME:
        List<Deployment> previousDeployments = repositoryService
          .createDeploymentQuery()
          .deploymentName(baseDeployment.getName())
          .list();

        for (Deployment deployment : previousDeployments) {
          deploymentIds.add(deployment.getId());
        }
        return deploymentIds;

      case ResumePreviousBy.RESUME_BY_PROCESS_DEFINITION_KEY:
      default:
        for (ProcessDefinition processDefinition : processDefinitions) {
          deploymentIds.add(processDefinition.getDeploymentId());
        }
        return deploymentIds;
    }
  }
}
