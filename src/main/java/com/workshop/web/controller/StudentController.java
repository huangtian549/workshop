package com.workshop.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.workshop.dao.StudentMapper;
import com.workshop.po.Student;
import com.workshop.po.StudentExample;
import com.workshop.service.IStudentService;
import com.workshop.util.CalendarUtil;
import com.workshop.vo.StudentVo;
import com.workshop.web.BaseController;

@Controller
public class StudentController extends BaseController{
	
	@Autowired
	private IStudentService studentService;
	
	@Autowired
	private StudentMapper studentMapper;
	
	@RequestMapping(value="/student/goAddstudent")
	public String goAddStudent() {
		return "studentAdd";
	}
	
	
	@RequestMapping(value="/student/addStudent")
	public String addStudent(Student student) {
		
		student.setStatus(0);
		studentMapper.insertSelective(student);
		
		return "studentAdd";
	}
	
	@RequestMapping(value="/student/searchStudent")
	public String searchStudent(Student student, ModelMap modelMap) {
		List<Student> list = new ArrayList<>();
		StudentExample example = new StudentExample();
		example.setOrderByClause("create_date desc");
		if (StringUtils.hasLength(student.getName())) {
			example.createCriteria().andNameLike("%" + student.getName() + "%");
			list = studentMapper.selectByExample(example );
		}else if (StringUtils.hasLength(student.getQq())) {
			example.createCriteria().andQqEqualTo(student.getQq());
			list = studentMapper.selectByExample(example );
		}else if (student.getCategory() != null && student.getCategory() == 1 && student.getStatus() != null) {
			example.createCriteria().andCategoryEqualTo(student.getCategory()).andStatusEqualTo(student.getStatus());
			list = studentMapper.selectByExample(example );
		}else if (student.getCategory()!= null && student.getCategory() == 0 ) {
			example.createCriteria().andCategoryEqualTo(student.getCategory());
			list = studentMapper.selectByExample(example );
		}else if (student.getCategory() == null && student.getStatus() != null) {
			example.createCriteria().andStatusEqualTo(student.getStatus());
			list = studentMapper.selectByExample(example );
		}else {
			list = studentMapper.selectByExample(example );
		}
		
		List<StudentVo> voList = new ArrayList<>();
		
		if (list != null) {
			for (Student student2 : list) {
				StudentVo vo = processStudent(student2);
				
				voList.add(vo);
			}
		}
		
		modelMap.put("list", voList);
		modelMap.put("student", student);
		return "studentList";
	}
	
	@RequestMapping(value="/student/goEditStudent")
	public String goEditStudent(Student student, ModelMap modelMap) {
		
		Student student2 = studentMapper.selectByPrimaryKey(student.getId());
		modelMap.put("student", processStudent(student2));
		return "studentEdit";
	}
	
	
	@RequestMapping(value="/student/editStudent")
	public String editStudent(Student student) {
		//如果状态由咨询变成了付费，那么状态需要改为正在进行
		if (student.getCategory() !=null && student.getCategory() == 1) {
			Student oldStudent = studentMapper.selectByPrimaryKey(student.getId());
			if (oldStudent.getCategory() == 0) {
				student.setStatus(0);
			}
			
		}
		
		studentService.updateStudnet(student);
		return "redirect:/student/searchStudent";
	}
	
	@RequestMapping(value="/student/called")
	public String called(Student student, ModelMap modelMap) {
		student.setStatus(1);
		studentMapper.updateByPrimaryKeySelective(student);
		return "redirect:/student/searchStudent";
	}
	
	@RequestMapping(value="/student/goContact")
	public String goContact(Student student, ModelMap modelMap) {
		String image = student.getImage();
		if (StringUtils.hasLength(image)) {
			
			List<String> list = new ArrayList<>();
			String[] imageArray = image.split(",");
			for (String s : imageArray) {
				
			}
		}
//		modelMap.
		return "showContact";
	}
	
	
	private StudentVo processStudent(Student student2) {
		StudentVo vo = new StudentVo();
		BeanUtils.copyProperties(student2, vo);
		if (student2.getEndDate() != null) {
			String endDateString = CalendarUtil.getDateString(student2.getEndDate(), CalendarUtil.SHORT_DATE_FORMAT);
			vo.setEndDateString(endDateString);
		}
		if (student2.getCreateDate() != null) {
			String createDateString = CalendarUtil.getDateString(student2.getCreateDate(), CalendarUtil.SHORT_DATE_FORMAT);
			vo.setCreateDateString(createDateString);
		}
		return vo;
	}

}
