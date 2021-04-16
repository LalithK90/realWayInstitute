package lk.real_way_institute.asset.common_asset.controller;


import lk.real_way_institute.asset.user_management.service.UserService;
import lk.real_way_institute.util.service.DateTimeAgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

  private final UserService userService;
  private final DateTimeAgeService dateTimeAgeService;

  @Autowired
  public UiController(UserService userService, DateTimeAgeService dateTimeAgeService) {
    this.userService = userService;
    this.dateTimeAgeService = dateTimeAgeService;
  }

  @GetMapping( value = {"/", "/index"} )
  public String index() {
    return "index";
  }

  @GetMapping( value = {"/home", "/mainWindow"} )
  public String getHome(Model model) {
    //do some logic here if you want something to be done whenever
        /*User authUser = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        Set<Petition> petitionSet = new HashSet<>();
        minutePetitionService
                .findByEmployeeAndCreatedAtBetween(authUser.getEmployee(),
                        dateTimeAgeService
                                .dateTimeToLocalDateStartInDay(LocalDate.now()),
                        dateTimeAgeService
                                .dateTimeToLocalDateEndInDay(LocalDate.now())).forEach(
                minutePetition -> {
                    petitionSet.add(petitionService.findById(minutePetition.getPetition().getId()));
                });
        model.addAttribute("petitions", petitionSet.toArray());*/
    return "mainWindow";
  }

}

