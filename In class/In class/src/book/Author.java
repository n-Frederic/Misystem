package book;

public class Author {
    String name;
    String email;
    char gender;

    public Author(String name,String email,char gender){
        this.name=name;
        this.email=email;
        this.gender=gender;
    }

    public String toString() {
        return "Author[name=" + name + ", email=" + email + ", gender=" + gender + "]";
    }
}
