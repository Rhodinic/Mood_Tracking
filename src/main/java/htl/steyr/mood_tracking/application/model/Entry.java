package htl.steyr.mood_tracking.application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private Date date;

    @Column(nullable=false)
    private int mood;

    @Column(nullable=false)
    private boolean school;

    @Column
    private int schoolIntensity;

    @Column(nullable=false)
    private int amountOfSocialInteraction;

    @Column(nullable=false)
    private boolean specialEvent;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "weather", referencedColumnName = "id", nullable = false)
    private Weather weather;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user", nullable=false)
    private User user;

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public boolean isSchool() {
        return school;
    }

    public void setSchool(boolean school) {
        this.school = school;
    }

    public int getSchoolIntensity() {
        return schoolIntensity;
    }

    public void setSchoolIntensity(int schoolIntensity) {
        this.schoolIntensity = schoolIntensity;
    }

    public int getAmountOfSocialInteraction() {
        return amountOfSocialInteraction;
    }

    public void setAmountOfSocialInteraction(int amountOfSocialInteraction) {
        this.amountOfSocialInteraction = amountOfSocialInteraction;
    }

    public boolean isSpecialEvent() {
        return specialEvent;
    }

    public void setSpecialEvent(boolean specialEvent) {
        this.specialEvent = specialEvent;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
