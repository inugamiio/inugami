package ${package}.infrastructure.database.repository;

import ${package}.infrastructure.database.entity.UserEntity;
import ${package}.infrastructure.database.entity.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, String>, QuerydslPredicateExecutor<UserEntity> {
    @Query("""
            select e.uid as uid,
                   e.firstName as firstName,
                   e.lastName as lastName,
                   e.email as email,
                   e.audit.createdBy as createdBy,
                   e.audit.createdDate as createdDate,
                   e.audit.lastModifiedBy as lastModifiedBy,
                   e.audit.lastModifiedDate as lastModifiedDate,
                   e.version as version
            from UserEntity e
            where e.uid =:uid
            """)
    Optional<UserProjection> findByUidProjected(final String uid);

    @Query("""
            select e.uid as uid,
                   e.firstName as firstName,
                   e.lastName as lastName,
                   e.email as email,
                   e.audit.createdBy as createdBy,
                   e.audit.createdDate as createdDate,
                   e.audit.lastModifiedBy as lastModifiedBy,
                   e.audit.lastModifiedDate as lastModifiedDate,
                   e.version as version
            from UserEntity e
            where e.uid in :uids
            """)
    List<UserProjection> findByUidsProjected(final Collection<String> uids);

    @Query("""
            select e.uid
            from UserEntity e
            where e.uid in :uids
            """)
    List<String> findUids(final Collection<String> uids);


    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE UserEntity u 
            SET u.firstName = :firstName,
                u.lastName = :lastName,
                u.email = :email,
                u.audit.lastModifiedBy =:lastModifiedBy,
                u.audit.lastModifiedDate =:lastModifiedDate,
                u.version = u.version+1
            WHERE u.uid = :uid
            """)
    void update(final String uid,
                final String firstName,
                final String lastName,
                final String email,
                final String lastModifiedBy,
                final LocalDateTime lastModifiedDate);


}
