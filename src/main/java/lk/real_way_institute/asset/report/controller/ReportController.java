package lk.real_way_institute.asset.report.controller;

import lk.real_way_institute.asset.batch.entity.Batch;
import lk.real_way_institute.asset.batch.service.BatchService;
import lk.real_way_institute.asset.batch_exam.service.BatchExamService;
import lk.real_way_institute.asset.common_asset.model.TwoDate;
import lk.real_way_institute.asset.payment.entity.Payment;
import lk.real_way_institute.asset.payment.service.PaymentService;
import lk.real_way_institute.asset.report.model.BatchAmount;
import lk.real_way_institute.asset.report.model.StudentAmount;
import lk.real_way_institute.asset.student.entity.Student;
import lk.real_way_institute.asset.student.service.StudentService;
import lk.real_way_institute.util.service.DateTimeAgeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/report" )
public class ReportController {
  private final PaymentService paymentService;
  private final BatchExamService batchExamService;
  private final DateTimeAgeService dateTimeAgeService;
  private final BatchService batchService;
  private final StudentService studentService;


  public ReportController(PaymentService paymentService, BatchExamService batchExamService,
                          DateTimeAgeService dateTimeAgeService, BatchService batchService,
                          StudentService studentService) {
    this.paymentService = paymentService;
    this.batchExamService = batchExamService;
    this.dateTimeAgeService = dateTimeAgeService;
    this.batchService = batchService;
    this.studentService = studentService;
  }
  private String commonIncomeReport(Model model, LocalDate startDate, LocalDate endDate) {
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(startDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(endDate);

    List< Payment > payments = paymentService.findByCreatedAtIsBetween(startDateTime, endDateTime);

    List< BigDecimal > totalPaymentAmount = new ArrayList<>();
    List< Batch > batches = new ArrayList<>();
    List< Student > students = new ArrayList<>();
    payments.forEach(x -> {
      totalPaymentAmount.add(x.getAmount());
      batches.add(x.getBatchStudent().getBatch());
      students.add(x.getBatchStudent().getStudent());
    });
    List< BatchAmount > batchAmounts = new ArrayList<>();
    batches.stream().distinct().collect(Collectors.toList()).forEach(x -> {
      List< Payment > batchPayments =
          payments.stream().filter(y -> y.getBatchStudent().getBatch().equals(x)).collect(Collectors.toList());
      List< BigDecimal > batchPaymentAmounts = new ArrayList<>();
      batchPayments.forEach(y -> batchPaymentAmounts.add(y.getAmount()));
      BatchAmount batchAmount = new BatchAmount();
      batchAmount.setAmount(batchPaymentAmounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
      batchAmount.setCount(batchPayments.size());
      batchAmount.setBatch(batchService.findById(x.getId()));
      batchAmounts.add(batchAmount);
    });
    List< StudentAmount >  studentAmounts = new ArrayList<>();
    students.stream().distinct().collect(Collectors.toList()).forEach(x -> {
      List< Payment > studentPayments =
          payments.stream().filter(y -> y.getBatchStudent().getStudent().equals(x)).collect(Collectors.toList());
      List< BigDecimal > studentPaymentAmounts = new ArrayList<>();
      studentPayments.forEach(y -> studentPaymentAmounts.add(y.getAmount()));
      StudentAmount studentAmount = new StudentAmount();
      studentAmount.setAmount(studentPaymentAmounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
      studentAmount.setCount(studentPaymentAmounts.size());
      studentAmount.setStudent(studentService.findById(x.getId()));
      studentAmounts.add(studentAmount);
    });


    model.addAttribute("batchAmounts", batchAmounts);
    model.addAttribute("studentAmounts", studentAmounts);
    model.addAttribute("payments", payments);
    model.addAttribute("paymentCount", payments.size());
    model.addAttribute("paymentAmount", totalPaymentAmount.stream().reduce(BigDecimal.ZERO, BigDecimal::add));
    String message = "This report is belongs from " + startDate + " to " + endDate;
    model.addAttribute("message", message);
    return "report/incomeReport";
  }

  @GetMapping( "/income" )
  public String todayReportIncome(Model model) {
    return commonIncomeReport(model, LocalDate.now(), LocalDate.now());
  }

  @PostMapping( "/income" )
  public String todayReportIncome(@ModelAttribute TwoDate twoDate, Model model) {
    return commonIncomeReport(model, twoDate.getStartDate(), twoDate.getEndDate());
  }


}
