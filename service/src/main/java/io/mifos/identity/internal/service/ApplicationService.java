/*
 * Copyright 2017 The Mifos Initiative.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mifos.identity.internal.service;

import io.mifos.anubis.api.v1.domain.Signature;
import io.mifos.identity.internal.repository.ApplicationSignatureEntity;
import io.mifos.identity.internal.repository.ApplicationSignatures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Myrle Krantz
 */
@Service
public class ApplicationService {

  private final ApplicationSignatures repository;

  @Autowired
  public ApplicationService(final ApplicationSignatures repository) {
    this.repository = repository;
  }

  public List<String> findAll() {
    return repository.getAll().stream()
            .map(ApplicationSignatureEntity::getApplicationIdentifier)
            .collect(Collectors.toList());
  }

  public boolean exists(final String applicationIdentifier) {
    return repository.signaturesExistForApplication(applicationIdentifier);
  }

  public Optional<Signature> getSignatureForApplication(final String applicationIdentifier, final String timestamp) {
    return repository.get(applicationIdentifier, timestamp)
            .map(this::mapToSignature);
  }

  private Signature mapToSignature(final ApplicationSignatureEntity entity) {
    final Signature ret = new Signature();
    ret.setPublicKeyExp(entity.getPublicKeyExp());
    ret.setPublicKeyMod(entity.getPublicKeyMod());
    return ret;
  }
}
