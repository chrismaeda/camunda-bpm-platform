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
package org.camunda.bpm.engine.rest.history;

import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.history.UserOperationLogEntryDto;
import org.camunda.bpm.engine.rest.dto.AnnotationDto;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Exposes the {@link org.camunda.bpm.engine.history.UserOperationLogQuery} as REST service.
 *
 * @author Danny Gräf
 */
@Path(UserOperationLogRestService.PATH)
public interface UserOperationLogRestService {

  public static final String PATH = "/user-operation";

  @GET
  @Path("/count")
  @Produces(MediaType.APPLICATION_JSON)
  CountResultDto queryUserOperationCount(@Context UriInfo uriInfo);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  List<UserOperationLogEntryDto> queryUserOperationEntries(@Context UriInfo uriInfo,
                                                           @QueryParam("firstResult") Integer firstResult,
                                                           @QueryParam("maxResults") Integer maxResults);

  @PUT
  @Path("/{operationId}/set-annotation")
  @Produces(MediaType.APPLICATION_JSON)
  void setAnnotation(@PathParam("operationId") String operationId, AnnotationDto annotationDto);

  @PUT
  @Path("/{operationId}/clear-annotation")
  @Produces(MediaType.APPLICATION_JSON)
  void clearAnnotation(@PathParam("operationId") String operationId);

}
