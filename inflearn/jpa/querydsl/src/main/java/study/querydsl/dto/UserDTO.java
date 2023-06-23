package study.querydsl.dto;

import lombok.ToString;

@ToString
public class UserDTO {

    private String name;
    private int age;

    public UserDTO(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
