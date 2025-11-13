package com.example.jobboard.config;

import com.example.jobboard.model.Company;
import com.example.jobboard.model.JobPosting;
import com.example.jobboard.model.JobType;
import com.example.jobboard.service.CompanyService;
import com.example.jobboard.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JobPostingService jobPostingService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // Thêm resource handler cho H2 console nếu cần
        registry.addResourceHandler("/h2-console/**")
                .addResourceLocations("classpath:/h2-console/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirect root to jobs page
        registry.addViewController("/").setViewName("redirect:/jobs");
    }

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            System.out.println("🌱 Seeding initial data...");
            seedCompaniesAndJobs();
        };
    }

    private void seedCompaniesAndJobs() {
        try {
            // Tạo các Company mẫu
            Company company1 = new Company();
            company1.setName("Tech Solutions Inc.");
            company1.setAddress("123 Main Street, Hanoi, Vietnam");
            company1.setEmail("contact@techsolutions.com");
            company1.setWebsite("https://techsolutions.com");
            companyService.save(company1);

            Company company2 = new Company();
            company2.setName("Software House JSC");
            company2.setAddress("456 Nguyen Trai, District 1, HCMC, Vietnam");
            company2.setEmail("hr@softwarehouse.com");
            company2.setWebsite("https://softwarehouse.com");
            companyService.save(company2);

            Company company3 = new Company();
            company3.setName("StartUp Vietnam");
            company3.setAddress("789 Innovation Road, Da Nang, Vietnam");
            company3.setEmail("info@startupvn.com");
            company3.setWebsite("https://startupvn.com");
            companyService.save(company3);

            // Tạo các Job Posting mẫu
            createJobPosting("Senior Java Developer",
                    "We are looking for an experienced Java Developer with Spring Boot knowledge.",
                    "Hanoi", "$1500 - $2000", JobType.FULL_TIME, company1);

            createJobPosting("Frontend React Developer",
                    "Join our team as a Frontend Developer specializing in React.js.",
                    "Ho Chi Minh City", "$1200 - $1800", JobType.REMOTE, company2);

            createJobPosting("DevOps Engineer",
                    "We need a DevOps Engineer to manage our cloud infrastructure.",
                    "Da Nang", "$1600 - $2200", JobType.FULL_TIME, company3);

            createJobPosting("Mobile App Developer Intern",
                    "Great opportunity for students to learn Flutter/Dart.",
                    "Hanoi", "$400 - $600", JobType.INTERNSHIP, company1);

            createJobPosting("Part-time QA Tester",
                    "Looking for a part-time QA tester to perform manual testing.",
                    "Ho Chi Minh City", "$8 - $12/hour", JobType.PART_TIME, company2);

            System.out.println("📈 Created: 3 companies and 5 job postings");

        } catch (Exception e) {
            System.err.println("❌ Error seeding data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createJobPosting(String title, String description, String location,
                                  String salary, JobType jobType, Company company) {
        JobPosting job = new JobPosting();
        job.setTitle(title);
        job.setDescription(description);
        job.setLocation(location);
        job.setSalary(salary);
        job.setJobType(jobType);
        job.setCompany(company);
        jobPostingService.save(job);
    }
}