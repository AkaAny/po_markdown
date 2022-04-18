package me.akaany.po_markdown.example.auth.info.po;

import me.akaany.po_markdown.ColumnDescription;
import me.akaany.po_markdown.TableDescription;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "user_auth")
@TableDescription(visibleName = "用户鉴权表")
public class UserAuthPO {
    @Id
    @Column(name = "staff_id")
    @ColumnDescription("用户账号")
    public String staffID;

    @Column(name = "created_at")
    @ColumnDescription("创建时间")
    public Date createdAt;

    @Column(name = "updated_at")
    @ColumnDescription("更新时间")
    public Date updatedAt;

    @Column(name = "staff_name")
    @ColumnDescription("用户名称")
    public String staffName;

    @Column(name = "password_hash")
    @ColumnDescription("SHA256(用户密码)")
    public String passwordHash;

    @Column(name = "blocked")
    @ColumnDescription("是否被封禁")
    public Boolean blocked;
}
