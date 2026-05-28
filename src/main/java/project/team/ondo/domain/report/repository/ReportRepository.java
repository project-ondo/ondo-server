package project.team.ondo.domain.report.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.report.constant.ReportTargetType;
import project.team.ondo.domain.report.entity.ReportEntity;
import project.team.ondo.domain.report.exception.ReportNotFoundException;

@Repository
public interface ReportRepository extends JpaRepository<@NonNull ReportEntity, @NonNull Long> {

    @Modifying
    @Query("DELETE FROM ReportEntity r WHERE r.targetType = :targetType AND r.targetId = :targetId")
    void deleteAllByTargetTypeAndTargetId(
            @Param("targetType") ReportTargetType targetType,
            @Param("targetId") Long targetId
    );

    default ReportEntity getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(ReportNotFoundException::new);
    }
}
