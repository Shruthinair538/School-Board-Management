package com.schol.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.sym.Name;
import com.schol.sba.entity.Subject;
import com.schol.sba.exception.AcademicProgramNorFoundException;
import com.schol.sba.repository.AcademicProgramRepository;
import com.schol.sba.repository.SubjectRepository;
import com.schol.sba.requestdto.SubjectRequest;
import com.schol.sba.responsedto.AcademicProgramResponse;
import com.schol.sba.responsedto.SubjectResponse;
import com.schol.sba.service.SubjectService;
import com.schol.sba.util.ResponseStructure;

@Service
public class SubjectServiceImpl implements SubjectService{
	
	@Autowired
	private SubjectRepository subjectRepo;
	
	@Autowired
	private AcademicProgramRepository academicRepo;
	
	@Autowired
	private ResponseStructure<AcademicProgramResponse> structure;
	
	@Autowired
	private AcademicProgramServiceImpl academicService;


	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubjectList(int programId, SubjectRequest request) {
		return academicRepo.findById(programId).map(program->{
			List<Subject> subjects=(program.getSubjects()!= null) ? program.getSubjects() : new ArrayList<Subject>();
			
			//add subjects
			request.getSubjectName().forEach(name ->{
				boolean isPresent=false;
				for(Subject subject:subjects) {
					isPresent=(name.equalsIgnoreCase(subject.getSubjectName())) ? true : false;
					if(isPresent)
						break;
						
				}
				if(!isPresent)
					subjects.add(subjectRepo.findBySubjectName(name)
						.orElseGet(() ->subjectRepo.save(Subject.builder().subjectName(name).build())));
				
			});
			
			//remove subjects
			List<Subject> removeSubject= new ArrayList<Subject>();
			subjects.forEach(subject ->{
				boolean isPresent=false;
				for(String name:request.getSubjectName()) {
					isPresent=(subject.getSubjectName().equalsIgnoreCase(name)) ? true : false;
					if(isPresent)
						break;
						
				}
				if(!isPresent)
					removeSubject.add(subject);
				
			});
			subjects.removeAll(removeSubject);
			
			program.setSubjects(subjects);  //set subjects to academic program
			academicRepo.save(program);   //saving updated program to db
			structure.setStatusCode(HttpStatus.CREATED.value());
			structure.setMessage("Updated subject list successfully!!!!");
			structure.setData(academicService.mapToAcademicProgramResponse(program));
			return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure,HttpStatus.CREATED);
				
		}).orElseThrow(()-> new AcademicProgramNorFoundException("Academic Program not Found!!"));
		

	}


//	@Override
//	public ResponseEntity<ResponseStructure<SubjectResponse>> findAllSubjects() {
//		List<Subject> list = subjectRepo.findAll();
//		structure.setStatusCode(HttpStatus.FOUND.value());
//		structure.setMessage("Fetched all subjects successfully!!!!");
//		for(List<Subject> l :list) {
//			System.out.println(l);
//		}
//		return new ResponseEntity<ResponseStructure<SubjectResponse>>(HttpStatus.FOUND);
//			 
//	}

}
