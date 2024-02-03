package com.schol.sba.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.schol.sba.entity.ClassHour;
import com.schol.sba.repository.ClassHourRepository;
import com.schol.sba.requestdto.ClassHourDTO;
import com.schol.sba.requestdto.ClassHourRequest;
import com.schol.sba.requestdto.ExcelRequestDTO;
import com.schol.sba.responsedto.ClassHourResponse;
import com.schol.sba.service.ClassService;
import com.schol.sba.util.ResponseStructure;

@RestController
public class ClassHourController {

	@Autowired
	private ClassService service;

	@Autowired
	private ClassHourRepository classRepo;


	@PostMapping("/academic-program/{programId}/class-hours")
	public ResponseEntity<ResponseStructure<ClassHourResponse>> generateClassHour(@PathVariable int programId){
		return service.generateClassHour(programId);
	}

	@PutMapping("/class-hours")
	public ResponseEntity<String> updateClassHour(@RequestBody List<ClassHourDTO> request){
		service.updateClassHour(request);
		return ResponseEntity.ok("Class hours updated successfully!!");
	}

	@PostMapping("/class-hours/{programId}")
	public ResponseEntity<ResponseStructure<List<ClassHour>>> createClassHourForNextWeek(@PathVariable int programId){
		return service.createClassHourForNextWeek(programId);
	}

	//Works for standalone app bcoz file is in the same system
	@PostMapping("/academic-program/{programId}/class-hour/write-excel")
	public ResponseEntity<String> addClassHourToExcel(@PathVariable int programId, @RequestBody ExcelRequestDTO request) throws FileNotFoundException, IOException {

		LocalDate fromDate = request.getFromDate();
		LocalDate toDate = request.getToDate();

		List<ClassHour> allClassHours = classRepo.findAll(); // Fetch all class hours
		List<ClassHour> filteredClassHours = allClassHours.stream()

				.filter(classHour ->
				classHour.getAcademicProgram().getProgramId() == programId &&
				classHour.getBeginsAt().toLocalDate().isAfter(fromDate.minusDays(1)) &&
				classHour.getBeginsAt().toLocalDate().isBefore(toDate.plusDays(1))
            	)
				.collect(Collectors.toList());

		XSSFWorkbook workBook = new XSSFWorkbook();
		Sheet sheet = workBook.createSheet();
		int rowNo = 0;

		Row row = sheet.createRow(rowNo);
		row.createCell(0).setCellValue("Date");
		row.createCell(1).setCellValue("BeginTime");
		row.createCell(2).setCellValue("EndTime");
		row.createCell(3).setCellValue("Subject");
		row.createCell(4).setCellValue("Teacher");
		row.createCell(5).setCellValue("RoomNo");

		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		for (ClassHour classHour : filteredClassHours) {

			Row row1 = sheet.createRow(++rowNo);
			row1.createCell(0).setCellValue(dateFormatter.format(classHour.getBeginsAt().toLocalDate()));
			row1.createCell(1).setCellValue(timeFormatter.format(classHour.getBeginsAt().toLocalTime()));
			row1.createCell(2).setCellValue(timeFormatter.format(classHour.getEndsAt().toLocalTime()));

			if (classHour.getSubject() == null) {
				row1.createCell(3).setCellValue("no subject");
			} else {
				row1.createCell(3).setCellValue(classHour.getSubject().getSubjectName());
			}

			if (classHour.getUser() != null) {
				row1.createCell(4).setCellValue(classHour.getUser().getUserName());
			} else {
				row1.createCell(4).setCellValue("No user");
			}
			row1.createCell(5).setCellValue(classHour.getRoomNo());

		}
		workBook.write(new FileOutputStream(request.getFilePath()));

		return ResponseEntity.ok("Class hour data written to Excel successfully!");
	}
//
	@PostMapping("/academic-program/{programId}/class-hours/from/{fromDate}/to/{toDate}/write-excel")
	public ResponseEntity<?> writeToExcel(@RequestParam MultipartFile multipartFile,@PathVariable int programId,@PathVariable LocalDate fromDate,@PathVariable LocalDate toDate) throws IOException{

		LocalDateTime fromTime = fromDate.atTime(LocalTime.MIDNIGHT); 
		LocalDateTime toTime = toDate.atTime(LocalTime.MIDNIGHT);
		List<ClassHour> allClassHours = classRepo.findAll(); // Fetch all class hours
		List<ClassHour> filteredClassHours = allClassHours.stream()

				.filter(classHour ->
				classHour.getAcademicProgram().getProgramId() == programId &&
				classHour.getBeginsAt().toLocalDate().isAfter(fromDate.minusDays(1)) &&
				classHour.getBeginsAt().toLocalDate().isBefore(toDate.plusDays(1))
            	)
				.collect(Collectors.toList());
		XSSFWorkbook workBook=new XSSFWorkbook(multipartFile.getInputStream());

		workBook.forEach(sheet ->{
			int rowNum=0;
			Row row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue("Date");
			row.createCell(1).setCellValue("Begin Time");
			row.createCell(2).setCellValue("End Time");
			row.createCell(3).setCellValue("Subject");
			row.createCell(4).setCellValue("Teacher");
			row.createCell(5).setCellValue("Room No");

			DateTimeFormatter timeFormat=DateTimeFormatter.ofPattern("HH:MM");//to format the date because it comes up with date and time.
			DateTimeFormatter dateFormat=DateTimeFormatter.ofPattern("yyyy-mm-dd");

			for(ClassHour classHour:filteredClassHours) {
				Row row1=sheet.createRow(++rowNum);
				row1.createCell(0).setCellValue(dateFormat.format(classHour.getBeginsAt()));
				row1.createCell(1).setCellValue(timeFormat.format(classHour.getBeginsAt()));
				row1.createCell(2).setCellValue(timeFormat.format(classHour.getEndsAt()));
				if(classHour.getSubject()==null) {
					row1.createCell(3).setCellValue("No subject");
				}
				else {
					row1.createCell(3).setCellValue(classHour.getSubject().getSubjectName());
				}

				row1.createCell(4).setCellValue(classHour.getUser().getUserName());
				row1.createCell(5).setCellValue(classHour.getRoomNo());
			}

		});

		ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
		workBook.write(outputStream);
		workBook.close();
		
		byte[] byteArray = outputStream.toByteArray();
		
		return ResponseEntity.ok()
				.header("Content Disposition", "attachment;Filename="+multipartFile.getOriginalFilename())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(byteArray);
	}


}
