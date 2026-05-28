package project.team.ondo.domain.report.service;

import project.team.ondo.domain.report.data.request.CreateReportRequest;
import project.team.ondo.domain.user.entity.UserEntity;

public interface CreateReportService {
    Long execute(UserEntity reporter, CreateReportRequest request);
}
