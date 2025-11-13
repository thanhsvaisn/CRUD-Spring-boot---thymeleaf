package com.example.jobboard.controller;

import com.example.jobboard.model.Company;
import com.example.jobboard.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public String listCompanies(Model model) {
        List<Company> companies = companyService.findAll();
        model.addAttribute("companies", companies);
        return "companies/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("company", new Company());
        model.addAttribute("formAction", "/companies/new");
        model.addAttribute("isEdit", false);
        return "companies/form";
    }

    @PostMapping("/new")
    public String createCompany(@Valid @ModelAttribute("company") Company company,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            // cần bơm lại biến form nếu template dùng
            model.addAttribute("formAction", "/companies/new");
            model.addAttribute("isEdit", false);
            return "companies/form";
        }
        companyService.save(company);
        return "redirect:/companies";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Company company = companyService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid company ID: " + id));
        model.addAttribute("company", company);
        model.addAttribute("formAction", "/companies/edit/" + id);
        model.addAttribute("isEdit", true);
        return "companies/form";
    }

    @PostMapping("/edit/{id}")
    public String updateCompany(@PathVariable Long id,
                                @Valid @ModelAttribute("company") Company company,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("formAction", "/companies/edit/" + id);
            model.addAttribute("isEdit", true);
            return "companies/form";
        }
        company.setId(id);
        companyService.save(company);
        return "redirect:/companies";
    }

    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable Long id) {
        companyService.deleteById(id);
        return "redirect:/companies";
    }
}
