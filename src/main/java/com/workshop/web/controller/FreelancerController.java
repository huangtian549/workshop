package com.workshop.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.workshop.dao.FreelancerMapper;
import com.workshop.dao.MissionMapper;
import com.workshop.dao.TaskMapper;
import com.workshop.po.Freelancer;
import com.workshop.po.FreelancerExample;
import com.workshop.po.Mission;
import com.workshop.po.Task;
import com.workshop.po.TaskExample;

@Controller
@RequestMapping("/freelancer/*")
public class FreelancerController {
	
	
	@Autowired
	private FreelancerMapper freelancerMapper;
	
	@Autowired
	private TaskMapper taskMapper;
	

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
		example.createCriteria().andFreelancerIdEqualTo(freelancerId).andHasPayEqualTo(0);
		List<Task> list = taskMapper.selectByExample(example );
		modelMap.put("list", list);
		return "freelancerCaculateSalary";
	}
	
	@RequestMapping("caculateSalary")
	public String caculateSalary(List<Task> list){
		if (list != null && list.size() > 0) {
			for (Task task : list) {
				taskMapper.updateByPrimaryKeySelective(task);
			}
		}
		
		return "redirect:/freelancer/searchFreelancer";
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
