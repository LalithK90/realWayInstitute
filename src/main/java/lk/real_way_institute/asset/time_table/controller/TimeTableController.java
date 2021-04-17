package lk.real_way_institute.asset.time_table.controller;


import lk.real_way_institute.asset.batch.entity.Batch;
import lk.real_way_institute.asset.batch.entity.enums.ClassDay;
import lk.real_way_institute.asset.batch.service.BatchService;
import lk.real_way_institute.asset.batch_student.service.BatchStudentService;
import lk.real_way_institute.asset.common_asset.model.DateTimeTable;
import lk.real_way_institute.asset.common_asset.model.enums.LiveDead;
import lk.real_way_institute.asset.student.entity.Student;
import lk.real_way_institute.asset.student.service.StudentService;
import lk.real_way_institute.asset.subject.service.SubjectService;
import lk.real_way_institute.asset.time_table.entity.TimeTable;
import lk.real_way_institute.asset.time_table.service.TimeTableService;
import lk.real_way_institute.asset.user_management.entity.User;
import lk.real_way_institute.asset.user_management.service.UserService;
import lk.real_way_institute.util.service.DateTimeAgeService;
import lk.real_way_institute.util.service.EmailService;
import lk.real_way_institute.util.service.MakeAutoGenerateNumberService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/timeTable" )
public class TimeTableController {
  private final TimeTableService timeTableService;
  private final SubjectService subjectService;
  private final BatchService batchService;
  private final BatchStudentService batchStudentService;
  private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;
  private final DateTimeAgeService dateTimeAgeService;
  private final UserService userService;
  private final StudentService studentService;
  private final EmailService emailService;


  public TimeTableController(TimeTableService timeTableService,
                             SubjectService subjectService,  BatchService batchService,
                             BatchStudentService batchStudentService,
                             MakeAutoGenerateNumberService makeAutoGenerateNumberService,
                             DateTimeAgeService dateTimeAgeService, UserService userService,
                             StudentService studentService, EmailService emailService) {
    this.timeTableService = timeTableService;
    this.subjectService = subjectService;
    this.batchService = batchService;
    this.batchStudentService = batchStudentService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
    this.dateTimeAgeService = dateTimeAgeService;
    this.userService = userService;
    this.studentService = studentService;
    this.emailService = emailService;
  }

  @GetMapping
  public String findAll(Model model) {
    model.addAttribute("timeTables",
                       timeTableService.findAll());
    return "timeTable/timeTable";
  }

  @GetMapping( "/byDate" )
  public String byDate(Model model) {
    List< TimeTable > timeTables = timeTableService.findAll();
    return common(timeTables, model);
  }

  @GetMapping( "/teacher" )
  public String byTeacher(Model model) {
    User authUser = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());

    List< TimeTable > timeTables = timeTableService.findAll();

    return common(timeTables, model);
  }

  private String common(List< TimeTable > timeTables, Model model) {
    HashSet< LocalDate > classDates = new HashSet<>();
    timeTables.forEach(x -> classDates.add(x.getStartAt().toLocalDate()));

    List< DateTimeTable > dateTimeTables = new ArrayList<>();

    for ( LocalDate classDate : classDates ) {
      DateTimeTable dateTimeTable = new DateTimeTable();
      dateTimeTable.setDate(classDate);
      dateTimeTable.setTimeTables(timeTables.stream().filter(x -> x.getStartAt().toLocalDate().isEqual(classDate)).collect(Collectors.toList()));
      dateTimeTables.add(dateTimeTable);
    }

    model.addAttribute("timeTableMaps", dateTimeTables);
    return "timeTable/timeTableView";
  }


  @GetMapping( "/add" )
  public String form() {
    return "timeTable/dateChooser";
  }


  @PostMapping( "/add" )
  public String form(@RequestParam( "date" ) @DateTimeFormat( pattern = "yyyy-MM-dd" ) LocalDate date, Model model) {
    LocalDate today = LocalDate.now();
    //month
    Month month = today.getMonth();
//Day of week
    String dayOfWeek = date.getDayOfWeek().toString();

    return commonThing(model, date, true);
  }

  @GetMapping( "/view/{id}" )
  public String findById(@PathVariable Integer id, Model model) {
    TimeTable timeTable = timeTableService.findById(id);
    model.addAttribute("timeTableDetail", timeTable);
    List< Student > students = new ArrayList<>();
    timeTable.getBatch()
        .getBatchStudents()
        .stream()
        .filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE)).collect(Collectors.toList())
        .forEach(x -> students.add(x.getStudent()));
    model.addAttribute("students", students);
    model.addAttribute("studentRemoveBatch", false);
    return "timeTable/timeTable-detail";
  }

  @GetMapping( "/edit/{date}" )
  public String editGet(@PathVariable( "date" ) @DateTimeFormat( pattern = "yyyy-MM-dd" ) LocalDate date, Model model) {
    return commonThing(model, date, false);
  }

  @PostMapping( "/edit" )
  public String editPost(@RequestParam( "date" ) @DateTimeFormat( pattern = "yyyy-MM-dd" ) LocalDate date,
                         Model model) {
    return commonThing(model, date, false);
  }

  @PostMapping( "/save" )
  public String persist(@Valid @ModelAttribute Batch batch, BindingResult bindingResult, Model model) {
    if ( bindingResult.hasErrors() ) {
      System.out.println(bindingResult.toString());
      return commonThing(model, batch.getDate(), true);
    }

    for ( TimeTable timeTable : batch.getTimeTables() ) {
      if ( timeTable.getId() == null ) {
        TimeTable lastTimeTable = timeTableService.lastTimeTable();
        if ( lastTimeTable == null ) {
          timeTable.setCode("SSTM" + makeAutoGenerateNumberService.numberAutoGen(null).toString());
        } else {
          timeTable.setCode("SSTM" + makeAutoGenerateNumberService.numberAutoGen(lastTimeTable.getCode().substring(4)).toString());
        }
      }
     TimeTable timeTableDb = timeTableService.persist(timeTable);
      timeTableDb.getBatch().getBatchStudents().forEach(x->{
        Student student = studentService.findById(x.getId());
        if(student.getEmail()!=null){
          String message = "Dear "+ student.getFirstName()+"\n Your "+timeTableDb.getBatch().getName()+" class would be held from "+ timeTableDb.getStartAt()+" to "+ timeTableDb.getEndAt() +"\n Thanks \n Success Student";
          emailService.sendEmail(student.getEmail(), "Time Table - Notification", message);
        }
      });

    }

    return "redirect:/timeTable";

  }

  private String commonThing(Model model, LocalDate date, boolean addStatus) {

    LocalDateTime from = dateTimeAgeService.dateTimeToLocalDateStartInDay(date);
    LocalDateTime to = dateTimeAgeService.dateTimeToLocalDateEndInDay(date);


    List< Batch > batches = new ArrayList<>();

   Batch batchSend = new Batch();
//    if ( addStatus ) {
//      String dayOfWeek = date.getDayOfWeek().toString();
//      //Day of week
//      for ( Batch batch : batchService.findByClassDay(ClassDay.valueOf(dayOfWeek))
//          .stream()//filter by using batch and in timeTable
//          .filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE) && timeTableService.availableTimeTableCheck(from, to, x))
//          .collect(Collectors.toList()) ) {
//        batch.setCount(batchStudentService.countByBatch(batch));
//        batches.add(batch);
//      }
//      List< TimeTable > timeTables = new ArrayList<>();
//      for ( Batch batch1 : batches ) {
//        TimeTable timeTable = new TimeTable();
//        timeTable.setBatch(batch1);
//        timeTables.add(timeTable);
//      }
//      batchSend.setTimeTables(timeTables);
//    } else {
//      List< TimeTable > timeTables = timeTableService.findByCreatedAtIsBetween(from, to);
//      batchSend.setTimeTables(timeTables);
//    }


    model.addAttribute("batches", batchSend);
    model.addAttribute("addStatus", addStatus);
    model.addAttribute("date", date);
    model.addAttribute("liveDeads", LiveDead.values());
    return "timeTable/addTimeTable";
  }


}
