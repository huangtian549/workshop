package com.workshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.dao.StudentMapper;
import com.workshop.po.Student;
import com.workshop.service.IStudentService;

@Service
public class StudentServiceImpl implements IStudentService {

	@Autowired
	private StudentMapper studentMapper;

	@Override
	public void addStudent(Student student) {
		studentMapper.insert(student);
		
	}

	@Override
	public int updateStudnet(Student student) {
		return studentMapper.updateByPrimaryKeySelective(student);
	}
	

	
	
	
}
