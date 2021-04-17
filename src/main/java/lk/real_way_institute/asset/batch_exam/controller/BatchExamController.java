package lk.real_way_institute.asset.batch_exam.controller;


import lk.real_way_institute.asset.batch.entity.Batch;
import lk.real_way_institute.asset.batch.service.BatchService;
import lk.real_way_institute.asset.batch_exam.entity.BatchExam;
import lk.real_way_institute.asset.batch_exam.service.BatchExamService;
import lk.real_way_institute.asset.common_asset.model.enums.LiveDead;
import lk.real_way_institute.asset.student.entity.Student;
import lk.real_way_institute.asset.student.service.StudentService;
import lk.real_way_institute.asset.user_management.entity.User;
import lk.real_way_institute.asset.user_management.service.UserService;
import lk.real_way_institute.util.service.EmailService;
import lk.real_way_institute.util.service.MakeAutoGenerateNumberService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/batchExam" )
public class BatchExamController {
  private final BatchService batchService;
  private final BatchExamService batchExamService;
  private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;

  private final UserService userService;
  private final StudentService studentService;
  private final EmailService emailService;

  public BatchExamController(BatchService batchService, BatchExamService batchExamService,
                             MakeAutoGenerateNumberService makeAutoGenerateNumberService, UserService userService, StudentService studentService, EmailService emailService) {
    this.batchService = batchService;
    this.batchExamService = batchExamService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
    this.userService = userService;
    this.studentService = studentService;
    this.emailService = emailService;
  }


  @GetMapping
  public String findAll(Model model) {
    model.addAttribute("batchExams",
                       batchExamService.findAll().stream().filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE)).collect(Collectors.toList()));
    return "batchExam/batchExam";
  }

  @GetMapping( "/teacher" )
  public String findByTeacher(Model model) {
    User user = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());


      model.addAttribute("batchExams",
                         batchExamService.findAll()
                             .stream()
                             .filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE))
                             .collect(Collectors.toList()));

    return "batchExam/batchExam";
  }

  @GetMapping( "/add/{id}" )
  public String addForm(@PathVariable Integer id, Model model) {
    Batch batch = batchService.findById(id);
    model.addAttribute("batchDetail", batch);
    BatchExam batchExam = new BatchExam();
    batchExam.setBatch(batch);
    batchExam.setStartAt(LocalDateTime.now());
    model.addAttribute("batchExams", batch.getBatchExams());
    model.addAttribute("batchExam", batchExam);
    return "batchExam/addBatchExam";
  }

  @GetMapping( "/view/{id}" )
  public String findById(@PathVariable Integer id, Model model) {
    BatchExam batchExam = batchExamService.findById(id);
   /* Teacher teacher = teacherService.findById(batchExam.getBatch().getTeacher().getId());
    model.addAttribute("teacherDetail", teacher);*/
    model.addAttribute("batchExamDetail", batchExam);
//    model.addAttribute("subjectDetail", teacher.getSubject());
    return "batchExam/batchExam-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    BatchExam batchExam = batchExamService.findById(id);
    model.addAttribute("batchExams", batchService.findById(batchExam.getBatch().getId()).getBatchExams());
    model.addAttribute("batchDetail", batchExam.getBatch());
    model.addAttribute("batchExam", batchExam);
    model.addAttribute("addStatus", true);
    return "batchExam/addBatchExam";
  }


  @PostMapping
  public String save(@ModelAttribute BatchExam batchExam, BindingResult bindingResult) {
    if ( bindingResult.hasErrors() ) {
      return "redirect:/batchExam/add" + batchExam.getBatch().getId();
    }
    if ( batchExam.getId() == null ) {
      BatchExam lastBatchExam = batchExamService.lastBatchExamDB();
      if ( lastBatchExam != null ) {
        String lastNumber = lastBatchExam.getCode().substring(3);
        batchExam.setCode("SSB" + makeAutoGenerateNumberService.numberAutoGen(lastNumber));
      } else {
        batchExam.setCode("SSB" + makeAutoGenerateNumberService.numberAutoGen(null));
      }
    }

    BatchExam batchExamDb = batchExamService.persist(batchExam);
    batchService.findById(batchExamDb.getBatch().getId()).getBatchStudents().forEach(x -> {
      Student student = studentService.findById(x.getStudent().getId());
      if ( student.getEmail() != null ) {
        String message = "Dear " + student.getName() + "\n Your " + batchService.findById( batchExamDb.getBatch().getId()).getName() + " exam " +
            "would be held from " + batchExamDb.getStartAt() + " to " + batchExamDb.getEndAt() + ".\n Thanks \n " +
            "Success Student";
        emailService.sendEmail(student.getEmail(), "Exam - Notification", message);
      }
    });
    return "redirect:/batchExam/teacher";
  }

  @GetMapping( "/delete/{id}" )
  public String delete(@PathVariable Integer id) {
    batchExamService.delete(id);
    return "redirect:/batchExam/teacher";
  }
}
