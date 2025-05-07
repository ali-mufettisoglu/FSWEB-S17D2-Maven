package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Experience;
import jakarta.annotation.PostConstruct;
import com.workintech.s17d2.model.Developer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.workintech.s17d2.tax.Taxable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/developers",produces = MediaType.APPLICATION_JSON_VALUE)
public class DeveloperController {
    public Map<Integer, Developer> developers ;
    Taxable developerTax;

    @Autowired
    public DeveloperController(Taxable developerTax) {
        this.developerTax = developerTax;
    }

    @PostConstruct
    public void init(){
        developers = new HashMap<>();
    }

    @GetMapping
    public List<Developer> get() {
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Developer get(@PathVariable int id) {
        return developers.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Developer post(@RequestBody Developer developer) {
        switch (developer.getExperience()) {
            case JUNIOR -> {
                 double salary = developer.getSalary() - (developer.getSalary() * developerTax.getSimpleTaxRate());
                 developers.put(developer.getId(),new Developer(developer.getId(),developer.getName(),salary,developer.getExperience()));
            }
            case MID -> {
                double salary = developer.getSalary() - (developer.getSalary() * developerTax.getMiddleTaxRate());
                developers.put(developer.getId(),new Developer(developer.getId(),developer.getName(),salary,developer.getExperience()));
            }
            case SENIOR -> {
                double salary = developer.getSalary() - (developer.getSalary() * developerTax.getUpperTaxRate());
                developers.put(developer.getId(),new Developer(developer.getId(),developer.getName(),salary,developer.getExperience()));
            }
        }
        return developer;
    }

    @PutMapping("/{id}")
    public Developer put(@PathVariable int id,@RequestBody Developer developer){
        developers.compute(id, (k, developer1) -> developer1);
        return developer;
    }

    @DeleteMapping("/{id}")
    public Developer delete(@PathVariable int id) {
        Developer developer = developers.get(id);
        developers.remove(id);
        return developer;
    }

}
