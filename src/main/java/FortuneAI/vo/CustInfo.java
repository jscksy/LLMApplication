package FortuneAI.vo;

public class CustInfo {

    public String getName() {
        return name;
    }

    public String getIdentityId() {
        return identityId;
    }

    public Integer getAge() {
        return age;
    }

    public String getJob() {
        return job;
    }

    public String getBalance() {
        return balance;
    }

    public Boolean getMarried() {
        return isMarried;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setMarried(Boolean married) {
        isMarried = married;
    }

    public CustInfo(String name, String identityId, Integer age, String job, String balance, Boolean isMarried) {
        this.name = name;
        this.identityId = identityId;
        this.age = age;
        this.job = job;
        this.balance = balance;
        this.isMarried = isMarried;
    }

    public CustInfo() {
    }

    public String name;
    public String identityId;
    public Integer age;
    public String job;
    public String balance;
    public Boolean isMarried;

    public boolean isCompleted(){
        return this.identityId!=null && this.age!=null && this.job!=null && this.balance!=null
                && this.isMarried!=null;
    }

    @Override
    public String toString() {
        return "CustInfo{" +
                "name='" + name + '\'' +
                ", identityId='" + identityId + '\'' +
                ", age=" + age +
                ", job='" + job + '\'' +
                ", balance=" + balance +
                ", isMarried=" + isMarried +
                '}';
    }

    public void update(CustInfo custInfo){
        if(custInfo.getIdentityId()!=null){
            this.setIdentityId(custInfo.getIdentityId());
        }
        if(custInfo.getMarried()!=null){
            this.setMarried(custInfo.getMarried());
        }
        if(custInfo.getAge()!=null){
            this.setAge(custInfo.getAge());
        }
        if(custInfo.getJob()!=null){
            this.setJob(custInfo.getJob());
        }
        if(custInfo.getBalance()!=null){
            this.setBalance(custInfo.getBalance());
        }
        if(custInfo.getName()!=null){
            this.setName(custInfo.getName());
        }
    }
}
