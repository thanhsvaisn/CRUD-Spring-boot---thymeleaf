package com.example.jobboard.service;

import com.example.jobboard.model.JobPosting;
import com.example.jobboard.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobPostingService {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    public List<JobPosting> findAll() {
        return jobPostingRepository.findAll();
    }

    public Optional<JobPosting> findById(Long id) {
        return jobPostingRepository.findById(id);
    }

    public JobPosting save(JobPosting jobPosting) {
        return jobPostingRepository.save(jobPosting);
    }

    public void deleteById(Long id) {
        jobPostingRepository.deleteById(id);
    }

    public List<JobPosting> searchByTitle(String keyword) {
        return jobPostingRepository.findByTitleContaining(keyword);
    }
}