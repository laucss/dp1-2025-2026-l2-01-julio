package es.us.dp1.lx_xy_24_25.Escape_From_Elba.friendRequest;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import es.us.dp1.lx_xy_24_25.Escape_From_Elba.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.Escape_From_Elba.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "requests")
public class FriendRequest extends BaseEntity {
    
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(referencedColumnName = "id", name = "sender")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User sender;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(referencedColumnName = "id", name = "receiver")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User receiver;

    @NotNull
    @Column(name = "requestStatus")
    @Enumerated(EnumType.STRING)
    private StatusType status;

}