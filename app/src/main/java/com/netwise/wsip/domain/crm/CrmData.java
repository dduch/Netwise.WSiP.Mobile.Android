package com.netwise.wsip.domain.crm;


import java.util.List;

//shift + f6
public class CrmData {
    private List<School> schools;
    private List<Teacher> teachers;
    private List<NoteHeader> notes;

    public CrmData(List<School> schools, List<Teacher> teachers, List<NoteHeader> notes) {
        this.schools = schools;
        this.teachers = teachers;
        this.notes = notes;
    }

    public List<School> getSchools() {
        return schools;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public List<NoteHeader> getNotes() {
        return notes;
    }

}
