package secret_gift_exchange.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Participant {

    private final String firstName;
    private final String lastName;
    private final String email;

    private Participant(Builder builder) {
        firstName = builder.firstName;
        lastName = builder.lastName;
        email = builder.email;
    }

    @JsonIgnore
    public String getFirstName() {
        return firstName;
    }

    @JsonIgnore
    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public static boolean sameFamily(final Participant p1, final Participant p2) {
        return p1 != null && p1.lastName.equals(p2.lastName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Participant rhs = (Participant) obj;
        return new EqualsBuilder()
            .append(this.firstName, rhs.firstName)
            .append(this.lastName, rhs.lastName)
            .append(this.email, rhs.email)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(firstName)
            .append(lastName)
            .append(email)
            .toHashCode();
    }

    public static final class Builder {

        private String firstName;
        private String lastName;
        private String email;

        public Builder() {
        }

        public Builder(Participant copy) {
            firstName = copy.firstName;
            lastName = copy.lastName;
            email = copy.email;
        }

        public Builder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        public Builder data(final String[] data) {
            this.firstName = data[0];
            this.lastName = data[1];
            this.email = data[2];
            return this;
        }

        public Participant build() {
            return new Participant(this);
        }
    }
}
