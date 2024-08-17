package amirhs.de.stage.user;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class DateOfBirth {

    private int day;

    private int month;
    private int year;

    public DateOfBirth() {}

    public DateOfBirth(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateOfBirth that = (DateOfBirth) o;
        return day == that.day && month == that.month && year == that.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, month, year);
    }

    @Override
    public String toString() {
        return "DateOfBirth{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                '}';
    }

}
