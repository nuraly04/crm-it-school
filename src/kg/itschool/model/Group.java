package kg.itschool.model;

import java.time.LocalTime;

public final class Group extends BaseEntity{

    private String groupName;
    private LocalTime groupTime;
    private Course course;

    public Group() {
        super();
    }

    public Group(Long id, String groupName, LocalTime groupTime, Course course) {
        super(id);
        this.groupName = groupName;
        this.groupTime = groupTime;
        this.course = course;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalTime getGroupTime() {
        return groupTime;
    }

    public void setGroupTime(LocalTime groupTime) {
        this.groupTime = groupTime;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", dateCreated=" + dateCreated +
                ", groupName='" + groupName + '\'' +
                ", groupTime=" + groupTime +
                '}';
    }
}
