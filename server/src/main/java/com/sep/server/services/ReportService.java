package com.sep.server.services;

import com.sep.server.dbaccess.MovieRepository;
import com.sep.server.dbaccess.ReportRepository;
import com.sep.server.dbaccess.UserRepository;
import com.sep.server.model.Report;
import com.sep.server.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private static ReportRepository reportRepository;
    private static EmailService emailService;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public ReportService(ReportRepository reportRepository, EmailService emailService, MovieRepository movieRepository, UserRepository userRepository){
        this.reportRepository=reportRepository;
        this.emailService = emailService;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }


    public ResponseEntity<List<User>> createReport(Report report, String movieName) {

        List<User> admins = userRepository.findUserByIsAdmin(true);

        if(movieRepository.existsByMovieName(movieName)) {
            report.setMovieName(movieName);
            report.setDone(false);
            reportRepository.save(report);
            emailService.sendReportEmail(admins, report.getReportID(),report.getMovieName(),report.getReportMessage());
            return new ResponseEntity("Created Report", HttpStatus.OK);
        } else {
            return new ResponseEntity("Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Report>> getAllReports(){
        List<Report> reports = reportRepository.findReportByDoneEquals(false);
        return new ResponseEntity(reports, HttpStatus.OK);
    }

    public ResponseEntity<String> setDoneToTrue(Report report){
        reportRepository.setReportToDone(report.getReportID());
        return new ResponseEntity("Status set to done.", HttpStatus.OK);
    }

}
