package com.hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String username;
    // 다대일 관계에서 연관관계의 주인은 fk 외래키가 있는곳으로 설정해야 한다.
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public Period getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

    public List<Address> getAddresseHistories() {
        return addresseHistories;
    }

    public void setAddresseHistories(List<Address> addresseHistories) {
        this.addresseHistories = addresseHistories;
    }

    @Embedded
    private Period workPeriod;
    @Embedded
    // 임베디드타입은 불변객체로 만들것 참조값을 변경하다보면 사이드이펙트 발생 (수정자 제거)
    private Address homeAddress;

    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name =     "MEMBER_ID"))
    private List<Address> addresseHistories = new ArrayList<>();

//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "city", column = @Column(name = "work_city")),
//            @AttributeOverride(name = "street", column = @Column(name = "work_street")),
//            @AttributeOverride(name = "zipcode", column = @Column(name = "work_zipcode"))})
    private Address workAddress;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    public void changeTeam(Team team) {
        this.team = team;
//        team.getMembers().add(this);
    }
}
