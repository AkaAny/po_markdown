package me.akaany.po_markdown.example.auth.role.po;

import me.akaany.po_markdown.ColumnDescription;
import me.akaany.po_markdown.TableDescription;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_role_item",indexes = {@Index(columnList = "staff_id,role_id")})
@TableDescription(visibleName = "用户具有的角色表")
public class UserRoleItemPO {
    @Id
    @Column(name = "id")
    @ColumnDescription("ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    @Column(name = "created_at")
    @ColumnDescription("创建时间")
    public Date createdAt;

    @Column(name = "updated_at")
    @ColumnDescription("更新时间")
    public Date updatedAt;

    @Column(name = "staff_id")
    @ColumnDescription("用户账号")
    public String staffID;

    @Column(name = "role_id")
    @ColumnDescription("角色ID")
    public String roleID;
}
