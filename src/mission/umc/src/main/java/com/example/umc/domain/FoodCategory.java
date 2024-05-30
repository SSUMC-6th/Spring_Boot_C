package com.example.umc.domain;

import com.example.umc.common.BaseEntity;
import com.example.umc.domain.maaping.MemberAgree;
import com.example.umc.domain.maaping.MemberPrefer;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FoodCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "foodCategory", cascade = CascadeType.ALL)
    private List<MemberPrefer> categoryMemberPreferList = new ArrayList<>();
}
