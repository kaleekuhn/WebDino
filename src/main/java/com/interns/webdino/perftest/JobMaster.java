package com.interns.webdino.perftest;

import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class JobMaster {

    private HashMap<String,Job> jobs = new HashMap<>();

    public Job getJob(String name){
        return jobs.get(name);
    }

    public void addJob(Job job){
        jobs.put(job.getName(), job);
    }

    public void removeJob(String name){
        jobs.remove(name);
    }

    public void runJob(String name){

        Job job = jobs.get(name);

        if(job != null){
            job.run();
        }
    }

    public JobStatus getJobStatus(String name){

        Job job = jobs.get(name);

        if(job != null){
            return job.getStatus();
        } else {
            return null;
        }
    }




}
