package com.schol.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.schol.sba.entity.AcademicProgram;
import com.schol.sba.entity.Subject;
import com.schol.sba.exception.AcademicProgramNorFoundException;
import com.schol.sba.exception.SubjectNotFoundException;
import com.schol.sba.repository.AcademicProgramRepository;
import com.schol.sba.repository.SubjectRepository;
import com.schol.sba.repository.UserRepository;
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
	private ResponseStructure<SubjectResponse> subjects;
	
	@Autowired
	private AcademicProgramServiceImpl academicService;
	
	@Autowired
	private ResponseStructure<List<SubjectResponse>> subStructure;

	
	public SubjectResponse mapToSubjectResponse(Subject subject) {
		return SubjectResponse.builder()
				.subjectId(subject.getSubjectId())
				.subjectName(subject.getSubjectName())
				.build();
	}


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


	@Override
	public ResponseEntity<ResponseStructure<List<SubjectResponse>>> findAllSubjects() {
		List<Subject> list = subjectRepo.findAll();
		
		List<SubjectResponse> subjectsResponse=list.stream()
				.map(u->mapToSubjectResponse(u))
				.collect(Collectors.toList());
		
		
		subStructure.setStatusCode(HttpStatus.FOUND.value());
		subStructure.setMessage("Fetched all subjects successfully!!!!");
		subStructure.setData(subjectsResponse);
		return new ResponseEntity<ResponseStructure<List<SubjectResponse>>>(subStructure,HttpStatus.FOUND);
			 
	}


	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> updateSubjectList(int programId,
			SubjectRequest request) {
		AcademicProgram academicProgram = academicRepo.findById(programId)
                .orElseThrow(() -> new RuntimeException("Academic program not found"));

        List<Subject> existingSubjects = academicProgram.getSubjects();

        for (String subjectnames :request.getSubjectName() ) {
        	Optional<Subject> existingSubject = subjectRepo.findBySubjectName(subjectnames);

            if (existingSubject.isPresent()) {
                if (!existingSubjects.contains(existingSubject.get())) {
                    existingSubjects.add(existingSubject.get());
                }
            } else {
                Subject newSubject = new Subject();
                newSubject.setSubjectName(subjectnames);
                subjectRepo.save(newSubject);
                existingSubjects.add(newSubject);
            }
        }

        academicRepo.save(academicProgram);
        structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("updated the subject list to academic program");
		structure.setData(academicService.mapToAcademicProgramResponse(academicProgram));
		return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure,HttpStatus.OK); 
    }


	@Override
	public ResponseEntity<ResponseStructure<SubjectResponse>> deleteSubject(int subjectId) {
		Subject subject = subjectRepo.findById(subjectId).orElseThrow(()-> new SubjectNotFoundException("Subject doesn't exist!!"));
		subjectRepo.delete(subject);
		
		subjects.setStatusCode(HttpStatus.OK.value());
		subjects.setMessage("Deleted the subject successfully");
		subjects.setData(mapToSubjectResponse(subject));
		return new ResponseEntity<ResponseStructure<SubjectResponse>>(subjects,HttpStatus.OK); 
    
	}
	

}


	


	


