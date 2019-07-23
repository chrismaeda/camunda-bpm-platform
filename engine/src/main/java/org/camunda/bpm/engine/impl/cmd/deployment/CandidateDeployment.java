package org.camunda.bpm.engine.impl.cmd.deployment;

import java.util.List;

import org.camunda.bpm.engine.repository.Resource;

public interface CandidateDeployment {

  List<Resource> getResources();
  
  String getName();
}
