package lk.real_way_institute.util.service;


import org.springframework.stereotype.Service;

@Service
public class MakeAutoGenerateNumberService {
    private final DateTimeAgeService dateTimeAgeService;

    public MakeAutoGenerateNumberService(DateTimeAgeService dateTimeAgeService) {
        this.dateTimeAgeService = dateTimeAgeService;
    }

    public Integer numberAutoGen(String lastNumber) {
        int newNumber;
        int previousNumber;
        int newNumberFirstTwoCharacters;

        int currentYearLastTwoNumber =
                Integer.parseInt(String.valueOf(dateTimeAgeService.getCurrentDate().getYear()).substring(2, 4));

        if ( lastNumber != null) {
        //1.
            previousNumber = Integer.parseInt(lastNumber.substring(0, 6));
            newNumberFirstTwoCharacters = Integer.parseInt(lastNumber.substring(0, 2));

            if ( currentYearLastTwoNumber == newNumberFirstTwoCharacters ) {
                newNumber = previousNumber + 1;
            } else {
                newNumber = previousNumber + 10000;
            }
        } else {
            newNumber = Integer.parseInt(currentYearLastTwoNumber + "0000");
        }
        return newNumber;
    }

    // phone number length validator
    public String phoneNumberLengthValidator(String number) {
        if ( number.length() == 9 ) {
            number = "0".concat(number);
        }
        return number;
    }



}
