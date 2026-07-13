package com.sep.server.dbaccess;

import com.sep.server.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report,String>{

    List<Report> findReportByDoneEquals(boolean bool);

    @Modifying
    @Transactional
    @Query("update Report r set r.done = true where r.reportID = ?1")
    void setReportToDone(long reportID);
}
