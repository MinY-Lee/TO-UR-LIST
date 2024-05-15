package com.eminyidle.tour.application.dto;

import com.eminyidle.tour.exception.InvalidUserNameException;
import com.eminyidle.tour.exception.InvalidUserNicknameException;
import com.eminyidle.tour.exception.NoSuchUserException;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node(primaryLabel = "USER")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User {
    @Id
    String userId;
    @Property
    String userName;
    @Property
    String userNickname;

    @Relationship(type = "ATTEND", direction = Relationship.Direction.OUTGOING)
    @Setter
    List<Attend> tourList;

    public void setUserId(String userId){
        if(userId==null) throw new NoSuchUserException();
        this.userId=userId;
    }

    public void setUserName(String userName){
        if(userName == null || userName.isBlank()) {
            throw new InvalidUserNameException("username is blank");
        }
        this.userName =userName.strip();
    }

    public void setUserNickname(String userNickname){
        if(userNickname == null || userNickname.isBlank()) {
            throw new InvalidUserNicknameException("username is blank");
        }
        this.userNickname=userNickname.strip();
    }
}
