package org.openchs.dao;

import org.openchs.domain.AddressLevelType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "addressLevelType", path = "addressLevelType")
public interface AddressLevelTypeRepository extends ReferenceDataRepository<AddressLevelType> {
    AddressLevelType findByNameAndOrganisationId(String name, Long organisationId);
}
