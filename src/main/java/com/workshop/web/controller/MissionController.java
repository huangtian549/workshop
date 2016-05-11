package com.workshop.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.workshop.po.Student;
import com.workshop.po.Task;
import com.workshop.po.TaskExample;

@Controller
@RequestMapping("/mission/*")
public class MissionController {
	
	@Autowired
	private MissionMapper missionMapper;
	
	@Autowired
	private FreelancerMapper freelancerMapper;
	
	@Autowired
	private TaskMapper taskMapper;
	

	@RequestMapping("goAddMission")
	public String goAddMission(Mission mission, ModelMap modelMap) {
		FreelancerExample example = new FreelancerExample();
		List<Freelancer> list = freelancerMapper.selectByExample(example );
		modelMap.put("list", list);
		modelMap.put("studentId", mission.getStudentId());
		return "missionAdd";
	}
	
	@RequestMapping("addMission")
	public String addMission(Mission mission, Task task, ModelMap modelMap) {
		modelMap.put("studentId", mission.getStudentId());
		task.setType(mission.getTaskType());
		taskMapper.insert(task);
		mission.setTaskId(task.getId());
		missionMapper.insert(mission);
		return "missionAdd";
	}
	
	@RequestMapping("searchMission")
	public String searchMission(Student student, ModelMap modelMap) {
		
		List<Mission> list = missionMapper.selectByName(student);
		
		modelMap.put("list", list);
		modelMap.put("name", student.getName());
		if (StringUtils.hasLength(student.getName())) {
			return "missionListTimeline";
		}
		return "missionList";
	}
	
	@RequestMapping("deleteMission")
	public String deleteMission(Mission mission, ModelMap modelMap) {
		
		missionMapper.deleteByPrimaryKey(mission.getId());
		List<Mission> list = missionMapper.selectByName(null);
		modelMap.put("list", list);
		return "missionList";
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
