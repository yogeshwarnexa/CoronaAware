package com.example.coronaaware.model;

public class PatientRegisterModel {
    private String username, age, bloodGroup, district, contact_no, aadhaar_no, uid;
    private String imageId;

    public String getAadharImage() {
        return aadharImage;
    }

    public void setAadharImage(String aadharImage) {
        this.aadharImage = aadharImage;
    }

    private String aadharImage;


    public PatientRegisterModel() {
    }

    public PatientRegisterModel(String username, String age, String bloodGroup,
                                String district, String contact_no,
                                String aadhaar_no, String uid, String imageId, String aadharImage) {
        this.username = username;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.district = district;
        this.contact_no = contact_no;
        this.aadhaar_no = aadhaar_no;
        this.uid = uid;
        this.imageId = imageId;
        this.aadharImage = aadharImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getAadhaar_no() {
        return aadhaar_no;
    }

    public void setAadhaar_no(String aadhaar_no) {
        this.aadhaar_no = aadhaar_no;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
