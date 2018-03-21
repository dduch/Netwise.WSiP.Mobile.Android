package com.netwise.wsip.infastucture.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseDto {

    @SerializedName("userSchools")
    public List<SchoolDto> schools;
    @SerializedName("usersTeachers")
    public List<TeacherDto> teachers;
    @SerializedName("noteHeaders")
    public List<NoteHeaderDto> notes;



    public class BaseDTO{
        @SerializedName("crmEntityName")
        public String CrmEntityName;

        @SerializedName("id")
        public String Id;

        @SerializedName("stateCode")
        public int StateCode;

        @SerializedName("statusCode")
        public int StatusCode;

        @SerializedName("ownerId")
        public String OwnerId;

        @SerializedName("ownerName")
        public String OwnerName;
    }

    public class SchoolDto extends BaseDTO{
        @SerializedName("name")
        public String name;

        @SerializedName("schoolType")
        public String SchoolType;

        @SerializedName("schoolTypeName")
        public String SchoolTypeName;

        @SerializedName("street1")
        public String Street1;

        @SerializedName("street2")
        public String Street2;

        @SerializedName("postalCodeId")
        public String PostalCodeId;

        @SerializedName("postalCodeName")
        public String PostalCodeName;

        @SerializedName("city")
        public String City;

        @SerializedName("provinceId")
        public String ProvinceId;

        @SerializedName("provinceName")
        public String ProvinceName;

        @SerializedName("teachers")
        public List<TeacherDto> teachers;
    }

    public class TeacherDto extends BaseDTO{
        @SerializedName("firstname")
        public String firstName;

        @SerializedName("lastname")
        public String Lastname;

        @SerializedName("email")
        public String Email;

        @SerializedName("mobilePhone")
        public String MobilePhone;

        @SerializedName("isMainSchool")
        public Boolean IsMainSchool;

        @SerializedName("mainSchoolId")
        public String MainSchoolId;

        @SerializedName("mainSchoolName")
        public String MainSchoolName;

        @SerializedName("street1")
        public String Street1;

        @SerializedName("street2")
        public String Street2;

        @SerializedName("city")
        public String City;

        @SerializedName("postalCodeId")
        public String PostalCodeId;

        @SerializedName("postalCodeName")
        public String PostalCodeName;

        @SerializedName("provinceId")
        public String ProbinceId;

        @SerializedName("provinceName")
        public String ProvinceName;
    }

    public class NoteHeaderDto extends BaseDTO{
        @SerializedName("name")
        public String name;
    }
}
