class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    void introduce() {
        System.out.println("Hello, my name is " + name + " and I am " + age + " years old.");
    }
}

public class ClassesObjects {
    public static void main(String[] args) {
        // Create objects of the Person class.
        Person person1 = new Person("Asha", 21);
        Person person2 = new Person("Rahul", 24);

        person1.introduce();
        person2.introduce();
    }
}
