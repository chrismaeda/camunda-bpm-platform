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
package org.camunda.bpm.engine.repository;

import org.camunda.bpm.engine.RepositoryService;

import java.util.Collection;
import java.util.List;

public interface DeploymentHandler {

  /**
   * @param newResource the resource to be deployed
   * @param existingResource most recently deployed resource that has the same resource name
   *   and deployment name
   * @return true, if the resource should be deployed
   */
  boolean shouldDeployResource(Resource newResource, Resource existingResource);

  /**
   * @param baseDeployment the new deployment, or, if no new resources are being deployed,
   *   the most recent deployment of the same name
   * @param processDefinitions the deployment Process Definitions for which to check if
   *   previous deployments need to be resumed.
   * @param resumePreviousBy the type of resuming previous deployment behavior.
   *   See {@link ResumePreviousBy} for the possible values.
   * @return the IDs of deployments that should be resumed (registered with the job executor
   *   and registered for the deploying process application)
   */
  Collection<String> determineDeploymentsToResume(RepositoryService repositoryService, Deployment baseDeployment, List<ProcessDefinition> processDefinitions, String resumePreviousBy);
}
