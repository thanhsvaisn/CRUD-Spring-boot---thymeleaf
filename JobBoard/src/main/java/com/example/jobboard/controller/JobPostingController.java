package com.example.jobboard.controller;

import com.example.jobboard.model.Company;
import com.example.jobboard.model.JobPosting;
import com.example.jobboard.model.JobType;
import com.example.jobboard.service.CompanyService;
import com.example.jobboard.service.JobPostingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobPostingController {

    @Autowired
    private JobPostingService jobPostingService;

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public String listJobs(Model model, @RequestParam(required = false) String keyword) {
        List<JobPosting> jobs;
        if (keyword != null && !keyword.trim().isEmpty()) {
            jobs = jobPostingService.searchByTitle(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            jobs = jobPostingService.findAll();
        }
        model.addAttribute("jobs", jobs);
        return "jobs/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        List<Company> companies = companyService.findAll();
        model.addAttribute("jobPosting", new JobPosting());
        model.addAttribute("companies", companies);
        model.addAttribute("formAction", "/jobs/new");
        model.addAttribute("jobTypes", JobType.values());
        return "jobs/form";
    }

    @PostMapping("/new")
    public String createJob(@Valid @ModelAttribute JobPosting jobPosting,
                            @RequestParam Long companyId, // THÊM
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("jobTypes", JobType.values());
            return "jobs/form";
        }
        Company company = companyService.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid company ID"));
        jobPosting.setCompany(company);
        jobPostingService.save(jobPosting);
        return "redirect:/jobs";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        JobPosting jobPosting = jobPostingService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
        model.addAttribute("jobPosting", jobPosting);
        model.addAttribute("companies", companyService.findAll());
        model.addAttribute("formAction", "/jobs/edit/" + id);
        model.addAttribute("jobTypes", JobType.values());
        return "jobs/form";
    }

    @PostMapping("/edit/{id}")
    public String updateJob(@PathVariable Long id, @Valid @ModelAttribute JobPosting jobPosting,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("companies", companyService.findAll());
            model.addAttribute("jobTypes", JobType.values());
            return "jobs/form";
        }
        jobPosting.setId(id);
        jobPostingService.save(jobPosting);
        return "redirect:/jobs";
    }

    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        jobPostingService.deleteById(id);
        return "redirect:/jobs";
    }

    @GetMapping("/detail/{id}")
    public String jobDetail(@PathVariable Long id, Model model) {
        JobPosting jobPosting = jobPostingService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
        model.addAttribute("job", jobPosting);
        return "jobs/detail";
    }
}