package com.netwise.wsip.infastucture;
import com.netwise.wsip.domain.crm.CrmData;
import com.netwise.wsip.domain.crm.NoteHeader;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Teacher;
import com.netwise.wsip.infastucture.network.ResponseDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawido on 14.03.2018.
 */

public class ModelMapper {
    public static CrmData mapDto(ResponseDto responseData) {
        List<School> schools = mapSchools(responseData.schools);
        List<Teacher> teachers = mapTeachers(responseData.teachers);
        List<NoteHeader> noteHeaders = mapNoteHeaders(responseData.notes);
        CrmData crmData = new CrmData(schools, teachers, noteHeaders);
        return crmData;
    }


    private static  List<School> mapSchools(List<ResponseDto.SchoolDto> schoolsDto){
        List<School> schools = new ArrayList<>();
        for(ResponseDto.SchoolDto schoolDto : schoolsDto){
            School school = new School();
            school.name = schoolDto.name;
            school.schoolTypeName = schoolDto.SchoolTypeName;
            school.street1 = schoolDto.Street1;
            school.street2 = schoolDto.Street2;
            school.postalCode = schoolDto.PostalCodeName;
            school.city = schoolDto.City;
            school.provinceName = schoolDto.ProvinceName;
            school.itemId = schoolDto.Id;
            school.teachers = mapTeachers(schoolDto.teachers);
            schools.add(school);
        }
        return  schools;
    }

    private static List<Teacher> mapTeachers(List<ResponseDto.TeacherDto> teachersDto){
        List<Teacher> teachers  = new ArrayList<>();
        for(ResponseDto.TeacherDto teacherDto : teachersDto){
            Teacher teacher = new Teacher();
            teacher.firstName = teacherDto.firstName;
            teacher.lastName = teacherDto.Lastname;
            teacher.street1 = teacherDto.Street1;
            teacher.street2 = teacherDto.Street2;
            teacher.postalCodeName = teacherDto.PostalCodeName;
            teacher.city = teacherDto.City;
            teacher.mainSchoolName = teacherDto.MainSchoolName;
            teacher.provinceName = teacherDto.ProvinceName;
            teacher.itemId = teacherDto.Id;
            teachers.add(teacher);
        }
        return  teachers;
    }

    private static List<NoteHeader> mapNoteHeaders(List<ResponseDto.NoteHeaderDto> noteHeadersDto){
        List<NoteHeader> headers  = new ArrayList<>();
        for(ResponseDto.NoteHeaderDto header  : noteHeadersDto){
            NoteHeader nh = new NoteHeader();
            nh.name = header.name;
        }
        return  headers;
    }
}
