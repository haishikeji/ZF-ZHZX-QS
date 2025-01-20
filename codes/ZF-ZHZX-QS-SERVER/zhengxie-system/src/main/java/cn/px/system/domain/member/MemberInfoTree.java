package cn.px.system.domain.member;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * @@author 品讯科技
 * @Date: 2024/3/15 10:47
 * @Describe:
 */
public class MemberInfoTree implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private String userId;

    /** 节点名称 */
    private String name;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MemberInfo> children;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MemberInfo> getChildren() {
        return children;
    }

    public void setChildren(List<MemberInfo> children) {
        this.children = children;
    }
}
