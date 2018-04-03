package com.netwise.wsip.infastucture;
import android.util.Base64;

import com.netwise.wsip.domain.crm.Attachment;
import com.netwise.wsip.domain.crm.CrmData;
import com.netwise.wsip.domain.crm.NoteHeader;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Teacher;
import com.netwise.wsip.infastucture.network.AttachmentRequestDto;
import com.netwise.wsip.infastucture.network.NoteHeaderDto;
import com.netwise.wsip.infastucture.network.ResponseDto;
import com.netwise.wsip.infastucture.network.SchoolDto;
import com.netwise.wsip.infastucture.network.TeacherDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by dawido on 14.03.2018.
 */

public class ModelMapper {
    public static CrmData mapDto(ResponseDto responseData) {
        List<School> schools = mapSchools(responseData.schools());
        List<Teacher> teachers = readTeachers(responseData.schools());
        List<NoteHeader> noteHeaders = mapNoteHeaders(responseData.notes());
        CrmData crmData = new CrmData(schools, teachers, noteHeaders);
        return crmData;
    }


    private static  List<School> mapSchools(List<SchoolDto> schoolsDto){
        List<School> schools = new ArrayList<>();
        for(SchoolDto schoolDto : schoolsDto){
            School school = new School();
            school.name = schoolDto.Name();
            school.schoolTypeName = schoolDto.SchoolTypeName();
            school.street1 = schoolDto.Street1();
            school.street2 = schoolDto.Street2();
            school.postalCode = schoolDto.PostalCodeName();
            school.city = schoolDto.City();
            school.provinceName = schoolDto.ProvinceName();
            school.itemId = schoolDto.Id();
            school.teachers = mapTeachers(schoolDto.Teachers(), schoolDto);
            schools.add(school);
        }
        return  schools;
    }

    private static List<Teacher> readTeachers(List<SchoolDto> schoolDtos){
        List<Teacher> teachers  = new ArrayList<>();
        for(SchoolDto school : schoolDtos){
           teachers.addAll(mapTeachers(school.Teachers(), school));
        }
        Collections.sort(teachers, (o1, o2) -> (o1.fullName.compareTo(o2.fullName)));
        return  teachers;
    }

    private static List<Teacher> mapTeachers(List<TeacherDto> teachersDto, SchoolDto school){
        List<Teacher> teachers  = new ArrayList<>();
        for(TeacherDto teacherDto : teachersDto){
            Teacher teacher = new Teacher();
            teacher.fullName = teacherDto.FullName();
            teacher.street1 = school.Street1();
            teacher.street2 = school.Street2();
            teacher.postalCodeName = school.PostalCodeName();
            teacher.city = school.City();
            teacher.mainSchoolName = school.Name();
            teacher.provinceName = school.ProvinceName();
            teacher.itemId = teacherDto.Id();
            teacher.mobilePhone = teacherDto.MobilePhone();
            teacher.email = teacherDto.Email();
            teachers.add(teacher);
        }
        return  teachers;
    }

    private static List<NoteHeader> mapNoteHeaders(List<NoteHeaderDto> noteHeadersDto){
        List<NoteHeader> headers  = new ArrayList<>();
        for(NoteHeaderDto header  : noteHeadersDto){
            NoteHeader nh = new NoteHeader();
            nh.name = header.name();
            headers.add(nh);
        }
        return  headers;
    }

    public static AttachmentRequestDto mapAttachmentData(Attachment attachmentData)
    {
        AttachmentRequestDto attachmentDto = new AttachmentRequestDto();
        attachmentDto.body = attachmentData.body;
        attachmentDto.crmEntityName = attachmentData.crmEntityName;
        attachmentDto.id = attachmentData.id;
        attachmentDto.fileName = attachmentData.fileName;
        attachmentDto.subject = attachmentData.subject;
        attachmentDto.mimeType = attachmentData.mimeType;
        attachmentDto.data = Base64.encodeToString(attachmentData.data, Base64.DEFAULT);

        return  attachmentDto;
    }
}
