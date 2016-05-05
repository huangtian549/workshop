package com.workshop.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.workshop.dao.FreelancerMapper;
import com.workshop.dao.MissionMapper;
import com.workshop.dao.StudentMapper;
import com.workshop.dao.TaskMapper;
import com.workshop.po.Freelancer;
import com.workshop.po.FreelancerExample;
import com.workshop.po.Mission;
import com.workshop.po.Student;
import com.workshop.po.Task;
import com.workshop.po.TaskExample;

@Controller
@RequestMapping("/freelancer/*")
public class FreelancerController {
	
	
	@Autowired
	private FreelancerMapper freelancerMapper;
	
	@Autowired
	private TaskMapper taskMapper;
	
	@Autowired
	private StudentMapper studentMapper;
	

	@RequestMapping("goAddFreelancer")
	public String goAddFreelancer(Freelancer freelancer, ModelMap modelMap) {
		return "freelancerAdd";
	}
	
	@RequestMapping("addFreelancer")
	public String addFreelancer(Freelancer freelancer, Task task, ModelMap modelMap) {
		freelancerMapper.insert(freelancer);
		
		return "freelancerAdd";
	}
	
	@RequestMapping("searchFreelancer")
	public String searchFreelancer(String name, ModelMap modelMap) {
		
		FreelancerExample example = new FreelancerExample();
		if (StringUtils.hasLength(name)) {
			example.createCriteria().andNameLike("%" + name + "%");
		}
		List<Freelancer> list = freelancerMapper.selectByExample(example );
		modelMap.put("list", list);
		return "freelancerList";
	}
	
	@RequestMapping("deleteFreelancer")
	public String deleteFreelancern(Freelancer freelancer, ModelMap modelMap) {
		
		freelancerMapper.deleteByPrimaryKey(freelancer.getId());
		FreelancerExample example = new FreelancerExample();
		List<Freelancer> list = freelancerMapper.selectByExample(example );
		modelMap.put("list", list);
		return "freelancerList";
	}
	
	@RequestMapping("goUpdateFreelancer")
	public String goUpdateFreelancer(Freelancer freelancer, ModelMap modelMap){
		
		Freelancer freelancer2 = freelancerMapper.selectByPrimaryKey(freelancer.getId());
		modelMap.put("freelancer", freelancer2);
		return "freelancerEdit";
		
	}
	
	@RequestMapping("updateFreelancer")
	public String updateFreelancer(Freelancer freelancer, ModelMap modelMap){
		
		freelancerMapper.updateByPrimaryKeySelective(freelancer);
		
		return "redirect:/freelancer/searchFreelancer";
		
	}
	
	@RequestMapping("goCaculateSalary")
	public String goCaculateSalary(Integer freelancerId, ModelMap modelMap){
		TaskExample example = new TaskExample();
		example.createCriteria().andFreelancerIdEqualTo(freelancerId).andHasPayIsNull();
		List<Task> list = taskMapper.selectByExample(example );
		Freelancer freelancer = freelancerMapper.selectByPrimaryKey(freelancerId);
		
		Map<Integer, String> map = new HashMap<>();
		if (list != null) {
			for (Task task : list) {
				Student student = studentMapper.selectByPrimaryKey(task.getStudentId());
				map.put(task.getStudentId(), student.getName());
			}
		}
		
		modelMap.put("map", map);
		modelMap.put("freelancer", freelancer);
		modelMap.put("list", list);
		return "freelancerCaculateSalary";
	}
	
	@RequestMapping("caculateSalary")
	@ResponseBody
	public Map<String, String> caculateSalary(@RequestBody List<LinkedHashMap> taskList){
		if (taskList != null && taskList.size() > 0) {
			for (LinkedHashMap map : taskList) {
				Task task = new Task();
				task.setId(Integer.parseInt((String)map.get("id")));
				task.setCaculateSalaryMethod(Integer.parseInt((String)map.get("caculateSalaryMethod")));
				task.setHasPay(1);
				taskMapper.updateByPrimaryKeySelective(task);
			}
		}
		Map<String, String> map = new HashMap<String, String>(1); 
		map.put("success", "true");  
	    return map; 
		
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
