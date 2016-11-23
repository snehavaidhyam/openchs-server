package org.openchs.dao;

import org.joda.time.DateTime;
import org.openchs.domain.Gender;
import org.openchs.domain.Individual;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
@RepositoryRestResource(collectionResourceRel = "individual", path = "individual")
public interface IndividualRepository extends PagingAndSortingRepository<Individual, Long> {
    @RestResource(path = "lastModified", rel = "lastModified")
    Page<Individual> findByLastModifiedDateTimeGreaterThan(@Param("lastModifiedDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime lastModifiedDateTime, Pageable pageable);
}