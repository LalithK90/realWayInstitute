package lk.real_way_institute.asset.batch.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import lk.real_way_institute.asset.batch.entity.Batch;
import lk.real_way_institute.asset.batch.entity.enums.Grade;
import lk.real_way_institute.asset.batch.service.BatchService;
import lk.real_way_institute.asset.batch_student.entity.BatchStudent;
import lk.real_way_institute.asset.batch_student.service.BatchStudentService;
import lk.real_way_institute.asset.common_asset.model.enums.LiveDead;
import lk.real_way_institute.asset.employee.entity.enums.Designation;
import lk.real_way_institute.asset.employee.service.EmployeeService;
import lk.real_way_institute.asset.instalment_date.entity.InstalmentDate;
import lk.real_way_institute.asset.student.service.StudentService;
import lk.real_way_institute.util.interfaces.AbstractController;
import lk.real_way_institute.util.service.MakeAutoGenerateNumberService;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/batch" )
public class BatchController implements AbstractController< Batch, Integer > {
  private final BatchService batchService;
  private final EmployeeService employeeService;
  private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;
  private final StudentService studentService;
  private final BatchStudentService batchStudentService;

  public BatchController(BatchService batchService, EmployeeService employeeService,
                         MakeAutoGenerateNumberService makeAutoGenerateNumberService,
                         StudentService studentService, BatchStudentService batchStudentService) {
    this.batchService = batchService;
    this.employeeService = employeeService;
    this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
    this.studentService = studentService;
    this.batchStudentService = batchStudentService;
  }


  @GetMapping
  public String findAll(Model model) {
    model.addAttribute("batchs",
                       batchService.findAll().stream().filter(x -> x.getLiveDead().equals(LiveDead.ACTIVE)).collect(Collectors.toList()));
    return "batch/batch";
  }

  private String commonMethod(Model model, Batch batch, boolean addStatus) {
    // model.addAttribute("employees",employeeService.findByDesignation(Designation.INSTRUCTOR));
    //todo : for demo remove above ones and delete below one
    model.addAttribute("employees", employeeService.findAll());
    model.addAttribute("batch", batch);
    model.addAttribute("addStatus", addStatus);
    model.addAttribute("liveDeads", LiveDead.values());
    return "batch/addBatch";
  }

  @GetMapping( "/add" )
  public String form(Model model) {
    return commonMethod(model, new Batch(), true);
  }

  @GetMapping( "/view/{id}" )
  public String findById(@PathVariable Integer id, Model model) {
    model.addAttribute("batchDetail", batchService.findById(id));
    return "batch/batch-detail";
  }

  @GetMapping( "/edit/{id}" )
  public String edit(@PathVariable Integer id, Model model) {
    return commonMethod(model, batchService.findById(id), false);
  }

  @PostMapping( "/save" )
  public String persist(@Valid @ModelAttribute Batch batch, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes, Model model) {
    System.out.println(batch.getName() + " " + batch.getStartAt() + " " + batch.getEndAt() + " " + batch.getStartAt() + " " + batch.getEndAt());
    if ( batch.getId() == null ) {
      Batch batchDbDayAndStartAndEndTime =
          batchService.findByNameAndStartAtIsBetweenAndEndAtIsBetween(batch.getName()
              , batch.getStartAt(), batch.getEndAt(), batch.getStartAt(), batch.getEndAt());


      if ( batchDbDayAndStartAndEndTime != null ) {
        ObjectError error = new ObjectError("batch",
                                            "This batch is already in the system. ");
        bindingResult.addError(error);
        return commonMethod(model, batch, true);
      }

      if ( bindingResult.hasErrors() ) {
        return commonMethod(model, batch, true);
      }

      // need to create auto generated registration number
      Batch lastBatch = batchService.lastBatchOnDB();
      if ( lastBatch != null ) {
        String lastNumber = lastBatch.getCode().substring(3);
        batch.setCode("RWB" + makeAutoGenerateNumberService.numberAutoGen(lastNumber));
      } else {
        batch.setCode("RWB" + makeAutoGenerateNumberService.numberAutoGen(null));
      }
    }

    if ( !batch.getInstalmentDates().isEmpty() ) {
      List< InstalmentDate > instalmentDates = new ArrayList<>();
      batch.getInstalmentDates().forEach(x -> {
        x.setBatch(batch);
        instalmentDates.add(x);
      });
      batch.setInstalmentDates(instalmentDates);
    }

    batchService.persist(batch);

    return "redirect:/batch";

  }

  @GetMapping( "/delete/{id}" )
  public String delete(@PathVariable Integer id, Model model) {
    batchService.delete(id);
    return "redirect:/batch";
  }

  @GetMapping( "/{grade}" )
  @ResponseBody
  public MappingJacksonValue findByGrade(@PathVariable( "grade" ) Grade grade) {
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(batchService.findById(1));

    SimpleBeanPropertyFilter forBatch = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "name");

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter("Batch", forBatch);

    mappingJacksonValue.setFilters(filters);

    return mappingJacksonValue;
  }


  @GetMapping( "/id/{id}" )
  @ResponseBody
  public MappingJacksonValue findById(@PathVariable( "id" ) Integer id) {
    MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(batchService.findById(id));

    SimpleBeanPropertyFilter forTeacher = SimpleBeanPropertyFilter
        .filterOutAllExcept("id", "firstName", "fee");

    FilterProvider filters = new SimpleFilterProvider()
        .addFilter("Teacher", forTeacher);

    mappingJacksonValue.setFilters(filters);

    return mappingJacksonValue;
  }
}