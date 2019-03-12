package gwd.hibernate.controller;


import gwd.hibernate.model.Person;
import gwd.hibernate.respositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/add")
    public String show(ModelMap modelMap) {

        modelMap.put("person", new Person());
        return "form";
    }


    //obsuga formularza
    @PostMapping("/add")
    public String create(@Valid Person person, BindingResult bindingResult)
    //bindign to sprawddza błedy, wyświetla błędy, automagia @Valid,
    // //dostaje parametry i chcesz robić setage setname itp i ma automatycznie błędy
    //valid ustanoawia nam pola a binding result pomaga nam wyświetlac blędy do html, ona nam tworzy
    //Valid jest najlepszą odnotacja, bez nie niego musielibyśmy dać inną adnotacje np request parm czy coś z mapp
    {
        if (bindingResult.hasErrors()) {
            return "form";

        } else {
           // update uzytkownika możliwy bo dodalismy ten same
            personRepository.save(person); //za[isanie od bazy danych gdy ok
            return "redirect:/form";
        }
    }


    @GetMapping("/")
    public String findAll(ModelMap modelMap) {

        modelMap.put("people", personRepository.findAllsortById());
        return "all";
    }

    @GetMapping("/age123")
    public String findAge123(ModelMap modelMap) {
        modelMap.put("people", personRepository.findByAge(123)); //dodajemy nowa strone gdzie pokazuje nam tylko 123 latków, na podstawie poprzedniego zapytania
        return "all";
    }

    @GetMapping("/find/search/age")
    public String findByAge(@RequestParam Integer age, ModelMap modelMap) {

        modelMap.put("people", personRepository.findByAge(age));
        return "all";
    }


    @PostMapping("/find/search") //problem taki że widac paramtry a w Post maping ich nie widać  i nic nie trzeb było by zmieniać
    public String find(@RequestParam String option, ModelMap modelMap) {

        try {
            Integer age = Integer.parseInt(option);
            modelMap.put("people", personRepository.findByNameOrAge(option, age));
        }
        catch  (NumberFormatException e ) //można dać dokładnie konkretny wyjątek albo po prostu Exepction
        {
            modelMap.put("people", personRepository.findByNameOrAge(option, 0));
        }


        return "all";
    }



    //usuwanie elementów
    @GetMapping("people/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

        Person person = personRepository.findById(id).get();
        personRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("message", "Usunieto zawodnika " + person.getName() + " z bazy danych");

        return "redirect:/all";
    }

    @GetMapping("people/{id}/update")
    public String update(@PathVariable Integer id, ModelMap modelMap) {

        Person person = personRepository.findById(id).get();
        modelMap.put("person", person);
        return "form";
    }

//    @GetMapping("/form")
//    public String forUpdate(ModelMap modelMap) {
//
//        modelMap.put("person", new Person());
//        return "form";
//    }

}
