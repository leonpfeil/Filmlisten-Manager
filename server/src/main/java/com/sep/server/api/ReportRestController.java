package com.sep.server.api;

import com.sep.server.dbaccess.AuthRepository;
import com.sep.server.model.Auth;
import com.sep.server.model.Movie;
import com.sep.server.model.Report;
import com.sep.server.model.User;
import com.sep.server.services.MovieService;
import com.sep.server.services.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

@RestController
public class ReportRestController {
    private ReportService reportService;

    public ReportRestController(ReportService reportService) {
        this.reportService=reportService;
    }

    @PostMapping(path = "report/add")
    public ResponseEntity<List<User>> addReport(@RequestParam(required = false, value = "param") String param, @RequestBody Report report){return reportService.createReport(report, param);}

    @PostMapping(path = "report/done")
    public ResponseEntity<String> closeReport(@RequestBody Report report){return reportService.setDoneToTrue(report);}

    @GetMapping(path = "report/getAll")
    public ResponseEntity<List<Report>> getAll(@RequestParam(required = false, value = "search") String searchVal) {
        return reportService.getAllReports();
    }

}

