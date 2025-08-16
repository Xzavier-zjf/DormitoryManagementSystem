package System;

public class Student {
    private int studentId;
    private String studentNumber;
    private String name;
    private String gender;
    private int age;
    private String department;
    private int grade;
    private String phone;
    private String bedNumber; // 新增：床号属性
    private String feePaid; // 新增：是否缴费属性

    private int dormitoryId;

    public Student(int studentId, String studentNumber, String name, String gender, int age, String department, int grade, String phone,String bedNumber, String feePaid, int dormitoryId) {
        this.studentId = studentId;
        this.studentNumber = studentNumber;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.department = department;
        this.grade = grade;
        this.phone = phone;
        this.bedNumber = bedNumber;
        this.feePaid = feePaid;
        this.dormitoryId = dormitoryId;
    }



    // Getters and setters

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    public int getStudentId() {
        return studentId;
    }
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
    public String getStudentNumber() {
        return studentNumber;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getGender() {
        return gender;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public int getAge() {
        return age;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getDepartment() {
        return department;
    }
    public void setGrade(int grade) {
        this.grade = grade;
    }
    public int getGrade() {
        return grade;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setFeePaid(String feePaid) {
        this.feePaid = feePaid;
    }

    public String getFeePaid() { return feePaid; }

    public void setDormitoryId(int dormitoryId) {
        this.dormitoryId = dormitoryId;
    }

    public int getDormitoryId() {
        return dormitoryId;
    }

}
