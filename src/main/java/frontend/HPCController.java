package frontend;

import backend.Backend;
import backend.model.Job;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Component
@Controller
@RestController
public class HPCController{
	
	private Backend m_backend = new Backend();

    @RequestMapping("/state")
    public String hello() {
        m_backend.stateCheck();
        return "Executed test";
    }
    
    @RequestMapping(value = "/job", method = RequestMethod.GET)
    public Iterable<Job> list() {
        return m_backend.getAllJobs();
    }
    
    @RequestMapping(value = "/job", method = RequestMethod.POST, produces = "application/json; charset=utf-8", consumes=MediaType.APPLICATION_JSON_VALUE)
    public void addJob(@RequestBody Job job) {
    	m_backend.addJob(job);
    }

}
