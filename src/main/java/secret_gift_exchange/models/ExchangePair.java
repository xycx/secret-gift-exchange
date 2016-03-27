package secret_gift_exchange.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ExchangePair {

    private final Participant gifter;
    private final Participant receiver;

    private ExchangePair(Builder builder) {
        gifter = builder.gifter;
        receiver = builder.receiver;
    }

    public Participant getGifter() {
        return gifter;
    }

    public Participant getReceiver() {
        return receiver;
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
        ExchangePair rhs = (ExchangePair) obj;
        return new EqualsBuilder()
            .append(this.gifter, rhs.gifter)
            .append(this.receiver, rhs.receiver)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(gifter)
            .append(receiver)
            .toHashCode();
    }

    public static final class Builder {

        private Participant gifter;
        private Participant receiver;

        public Builder() {
        }

        public Builder(ExchangePair copy) {
            gifter = copy.gifter;
            receiver = copy.receiver;
        }

        public Builder gifter(final Participant gifter) {
            this.gifter = gifter;
            return this;
        }

        public Builder receiver(final Participant receiver) {
            this.receiver = receiver;
            return this;
        }

        public ExchangePair build() {
            return new ExchangePair(this);
        }
    }
}
