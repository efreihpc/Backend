package backend.model.result;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import backend.system.GlobalState;

@Component
@Controller
@RestController
@RequestMapping("/result")
public class ResultController {

	ResultRepository m_resultRepository;
	
	public ResultController()
	{
		ResultPersistenceUnit persistence = GlobalState.get("GlobalPersistenceUnit");
		m_resultRepository = persistence.resultRepository();
	}
	
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Iterable<Result> serviceProviders() {
    	Iterable<Result> result =  m_resultRepository.findAll();
    	
    	return result;
    }
	
}

